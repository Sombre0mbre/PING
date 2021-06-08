package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectServiceImplementation implements ProjectService {

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
     * @param project     Project for which the features is executed.
     * @param featureType Type of the feature to execute.
     * @param params      Parameters given to the features.
     * @return Execution report of the feature.
     */
    @Override
    public Feature.ExecutionReport execute(Project project, Feature.Type featureType, Object... params) {
        throw new UnsupportedOperationException("FIXME");
    }

    /**
     * @return The {@link NodeService} associated with your {@link ProjectService}
     */
    @Override
    public NodeService getNodeService() {
        throw new UnsupportedOperationException("FIXME");
    }
}
