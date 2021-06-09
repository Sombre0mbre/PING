package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectServiceImplementation implements ProjectService {
    NodeService nodeService = new NodeServiceImplementation();

    /**
     * Load a {@link Project} from a path.
     *
     * @param root Path of the root of the project to load.
     * @return New project.
     */
    @Override
    public Project load(Path root) {
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("root is not a folder");
        }
        Node n = new NodeImplementation(root, Node.Types.FOLDER);
        return new ProjectImplementation(n);
    }

    /**
     * Execute the given feature on the given project.
     *
     * @param featureType Type of the feature to execute.
     * @param params      Parameters given to the features.
     * @return Execution report of the feature.
     */
    @Override
    public Feature.ExecutionReport execute(Project project, Feature.Type featureType, Object... params) {
        var featureOptional = project.getFeature(featureType);
        if (featureOptional.isEmpty()) {
            throw new IllegalArgumentException("Feature unimplemented or does not exists");
        }
        return featureOptional.get().execute(project, params);
    }

    /**
     * @return The {@link NodeService} associated with your {@link ProjectService}
     */
    @Override
    public NodeService getNodeService() {
        return nodeService;
    }
}

