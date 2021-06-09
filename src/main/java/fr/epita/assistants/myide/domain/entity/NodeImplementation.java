package fr.epita.assistants.myide.domain.entity;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NodeImplementation implements Node {
    Path path;
    Node.Type type;
    List<Node> children;

    public NodeImplementation(@NotNull Path path, @NotNull Node.Type type) {
        this.path = path;
        this.type = type;
        this.children = new ArrayList<>();
    }

    public NodeImplementation(@NotNull Path path, @NotNull Type type, @NotNull List<Node> children) {
        this.path = path;
        this.type = type;
        this.children = children;
    }

    /**
     * @return The Node path.
     */
    @Override
    public Path getPath() {
        return path;
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
}
