package fr.epita.assistants.myide.domain.service;


import fr.epita.assistants.myide.domain.entity.Node;

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
        throw new UnsupportedOperationException("FIXME");
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
        throw new UnsupportedOperationException("FIXME");
    }
}
