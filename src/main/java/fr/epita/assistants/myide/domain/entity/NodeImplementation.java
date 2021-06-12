package fr.epita.assistants.myide.domain.entity;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NodeImplementation implements Node {
    Path path;
    Node.Type type;
    List<Node> children;
    Node parentNode;

    public NodeImplementation(@NotNull Path path, @NotNull Node.Type type, Node parentNode) {
        this.path = path;
        this.type = type;
        this.children = new ArrayList<>();
        this.parentNode = parentNode;
    }

    public NodeImplementation(@NotNull Path path, @NotNull Type type, Node parentNode, @NotNull List<Node> children) {
        this.path = path;
        this.type = type;
        this.children = children;
        this.parentNode = parentNode;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * @return The Node path.
     */
    @Override
    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * @return The Node type.
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * If the Node is a Folder, returns a list of its children,
     * else returns an empty list.
     *
     * @return List of node
     */
    @Override
    public List<@NotNull Node> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "NodeImplementation{" +
                "path=<" + path +
                ">, type=<" + type +
                ">}";
    }
}
