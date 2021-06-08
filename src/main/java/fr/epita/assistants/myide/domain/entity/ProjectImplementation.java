package fr.epita.assistants.myide.domain.entity;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ProjectImplementation implements Project {
    Node rootNode;
    Set<Aspect> aspects;

    public ProjectImplementation(Node rootNode) {
        this.rootNode = rootNode;
        this.aspects = new HashSet<>();
    }

    public ProjectImplementation(Node rootNode, Set<Aspect> aspects) {
        this.rootNode = rootNode;
        this.aspects = aspects;
    }

    /**
     * @return The root node of the project.
     */
    @Override
    public Node getRootNode() {
        throw new UnsupportedOperationException("FIXME");
    }

    /**
     * @return The aspects of the project.
     */
    @Override
    public Set<Aspect> getAspects() {
        throw new UnsupportedOperationException("FIXME");
    }

    /**
     * Get an optional feature of the project depending
     * of its type. Returns an empty optional if the
     * project does not have the features queried.
     *
     * @param featureType Type of the feature to retrieve.
     * @return An optional feature of the project.
     */
    @Override
    public Optional<Feature> getFeature(Feature.Type featureType) {
        throw new UnsupportedOperationException("FIXME");
    }
}
