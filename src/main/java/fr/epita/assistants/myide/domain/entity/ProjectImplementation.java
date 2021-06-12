package fr.epita.assistants.myide.domain.entity;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.aspects.AnyAspect;

import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

public class ProjectImplementation implements Project {
    Node rootNode;
    Set<Aspect> aspects;

    public ProjectImplementation(Node rootNode, MyIde.Configuration configuration) {
        this.rootNode = rootNode;
        this.aspects = Set.of(new AnyAspect(configuration));
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
        return rootNode;
    }

    /**
     * @return The aspects of the project.
     */
    @Override
    public Set<Aspect> getAspects() {
        return aspects;
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
        for (var aspect : aspects) {
            for (var i : aspect.getFeatureList()) {
                if (i.type() == featureType)
                    return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        var array = new StringJoiner(", ", "[ ", " ]");
        aspects.forEach((n) -> array.add(n.toString()));
        return "ProjectImplementation{\n" +
                "\trootNode=" + rootNode +
                ", \n\taspects=" + array +
                "\n}";
    }
}
