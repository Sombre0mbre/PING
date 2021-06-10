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
        throw new UnsupportedOperationException("FIXME");
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
        if (folder.getType() != Node.Types.FOLDER)
            throw new IllegalArgumentException("folder node <" + folder.getPath() + "> is not a folder!");
        var childPath = folder.getPath().resolve(name);
        try {
            if (!childPath.toFile().createNewFile())
                throw new IllegalStateException();
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not create node: It could already exists");
        }
        return new NodeImplementation(childPath, type);
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
        throw new UnsupportedOperationException("Not finished");
    }
}
