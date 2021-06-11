package fr.epita.assistants.myide.domain.service;


import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.NodeImplementation;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class NodeServiceImplementation implements NodeService {
    ProjectServiceImplementation projectService;

    public NodeServiceImplementation(ProjectServiceImplementation projectService) {
        this.projectService = projectService;
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
            file.write(reader.substring(0, from + 1).getBytes());
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
        if (destinationFolder.getType() != Node.Types.FOLDER)
            throw new IllegalArgumentException("dest folder <" + destinationFolder.getPath() + "> is not a folder!");
        var nodeFile = nodeToMove.getPath().toFile();
        try {
            FileUtils.moveToDirectory(nodeFile, destinationFolder.getPath().toFile(), false);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Could not move directory");
        }
        var nodeTemp = (NodeImplementation) nodeToMove;
        nodeTemp.setPath(nodeFile.toPath());
        var parentNode = nodeTemp.getParentNode();
        if (parentNode != null)
            parentNode.getChildren().remove(nodeTemp);
        nodeTemp.setParentNode(destinationFolder);
        destinationFolder.getChildren().add(nodeTemp);
        return nodeTemp;
    }
}
