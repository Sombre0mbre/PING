package fr.epita.assistants.myide.domain.entity;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.List;

public class NodeImplementation implements Node {
    /**
     * @return The Node path.
     */
    @Override
    public Path getPath() {
        throw new UnsupportedOperationException("FIXME");
    }

    /**
     * @return The Node type.
     */
    @Override
    public Type getType() {
        throw new UnsupportedOperationException("FIXME");
    }

    /**
     * If the Node is a Folder, returns a list of its children,
     * else returns an empty list.
     *
     * @return List of node
     */
    @Override
    public List<@NotNull Node> getChildren() {
        throw new UnsupportedOperationException("FIXME");
    }
}
