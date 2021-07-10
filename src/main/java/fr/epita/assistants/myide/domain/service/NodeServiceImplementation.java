package fr.epita.assistants.myide.domain.service;


import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.NodeImplementation;
import org.apache.commons.io.FileUtils;

import javax.validation.constraints.NotNull;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class NodeServiceImplementation implements NodeService {
    public void generateChildren(@NotNull Node n) {
        if (n.isFile())
            return;
        var list = n.getPath().toFile().listFiles();
        if (list == null)
            return;
        for (var i : list) {
            Node.Type type;
            if (i.isDirectory())
                type = Node.Types.FOLDER;
            else if (i.isFile())
                type = Node.Types.FILE;
            else
                continue;
            var child = new NodeImplementation(i.toPath(), type, n);
            n.getChildren().add(child);
            generateChildren(child);
        }
    }

    public void updateChildrenPath(@NotNull Node n) {
        if (n.isFile())
            return;
        for (var i : n.getChildren()) {
            ((NodeImplementation) i).setPath(n.getPath().resolve(i.getPath().getFileName()));
            updateChildrenPath(i);
        }
    }

    public String getContent(Node n) {
        try {
            return Files.readString(n.getPath());
        } catch (IOException e) {
            throw new UnsupportedOperationException("Could not read file");
        }
    }


    public Node setText(Node node, byte[] content) {
        if (!node.isFile())
            throw new IllegalArgumentException("Node is not a file!");
        try {
            Files.write(node.getPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new UnsupportedOperationException("Could not write to node");
        }
        return node;
    }

    /**
     * Update the content in the range [from, to[.
     *
     * @param node            Node to update (must be a file).
     * @param from            Beginning index of the text to update.
     * @param to              Last index of the text to update (Not included).
     * @param insertedContent Content to insert.
     * @return The node that has been updated.
     * @throws Exception upon update failure.
     */
    @Override
    public Node update(Node node, int from, int to, byte[] insertedContent) {
        if (!node.isFile())
            throw new IllegalArgumentException("Node is not a file!");
        try {
            var reader = Files.readString(node.getPath());
            var file = new BufferedOutputStream(new FileOutputStream(node.getPath().toFile()));
            file.write(reader.substring(0, from).getBytes());
            file.write(insertedContent);
            file.write(reader.substring(to).getBytes());
            file.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not write to file");
        }
        return node;
    }

    /**
     * Delete the node given as parameter.
     *
     * @param node Node to remove.
     * @return True if the node has been deleted, false otherwise.
     */
    @Override
    public boolean delete(Node node) {
        try {
            if (node.isFolder())
                FileUtils.deleteDirectory(node.getPath().toFile());
            else if (node.isFile())
                Files.delete(node.getPath());
            var parent = ((NodeImplementation) node).getParentNode();
            if (parent != null)
                parent.getChildren().remove(node);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Create a new node.
     *
     * @param folder Parent node of the new node.
     * @param name   Name of the new node.
     * @param type   Type of the new node.
     * @return Node that has been created.
     * @throws Exception upon creation failure.
     */
    @Override
    public Node create(Node folder, String name, Node.Type type) {
        Path childPath;
        if (folder != null) {
            if (!folder.isFolder())
                throw new IllegalArgumentException("folder node <" + folder.getPath() + "> is not a folder!");
            childPath = folder.getPath().resolve(name);
        } else {
            childPath = new File(name).toPath();
        }

        try {
            if (type == Node.Types.FILE) {
                Files.createFile(childPath);
            } else if (type == Node.Types.FOLDER) {
                Files.createDirectory(childPath);
            } else
                throw new IllegalStateException("Unknown type");
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not create node: It could already exists");
        }
        var res = new NodeImplementation(childPath, type, folder);
        if (folder != null)
            folder.getChildren().add(res);
        return res;
    }

    /**
     * Move node from source to destination.
     *
     * @param nodeToMove        Node to move.
     * @param destinationFolder Destination of the node.
     * @return The node that has been moved.
     * @throws Exception upon move failure.
     */
    @Override
    public Node move(Node nodeToMove, Node destinationFolder) {
        if (nodeToMove == null || destinationFolder == null)
            throw new IllegalArgumentException("move between null: " + nodeToMove + " -> " + destinationFolder);
        if (destinationFolder.getType() != Node.Types.FOLDER)
            throw new IllegalArgumentException("dest folder <" + destinationFolder.getPath() + "> is not a folder!");
        var nodeTemp = (NodeImplementation) nodeToMove;
        try {
            var name = nodeTemp.getPath().getFileName();
            FileUtils.moveToDirectory(nodeTemp.getPath().toFile(), destinationFolder.getPath().toFile(), false);
            nodeTemp.setPath(destinationFolder.getPath().resolve(name));
            updateChildrenPath(nodeTemp);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not move directory");
        }
        var parentNode = nodeTemp.getParentNode();
        if (parentNode != null)
            parentNode.getChildren().remove(nodeTemp);
        nodeTemp.setParentNode(destinationFolder);
        destinationFolder.getChildren().add(nodeTemp);
        return nodeTemp;
    }
}
