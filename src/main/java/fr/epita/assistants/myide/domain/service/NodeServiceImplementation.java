package fr.epita.assistants.myide.domain.service;


import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.NodeImplementation;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;

public class NodeServiceImplementation implements NodeService {
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
        throw new UnsupportedOperationException("FIXME");
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
            e.printStackTrace();
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
        if (!folder.isFolder())
            throw new IllegalArgumentException("folder node <" + folder.getPath() + "> is not a folder!");
        var childPath = folder.getPath().resolve(name);
        try {
            if (type == Node.Types.FILE) {
                Files.createFile(childPath);
            } else if (type == Node.Types.FOLDER) {
                Files.createDirectory(childPath);
            } else
                throw new IllegalStateException("Unknown type");
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not create node: It could already exists");
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
            throw new IllegalArgumentException("Could not move directory");
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
