package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.entity.aspects.AnyAspect;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class ProjectServiceImplementation implements ProjectService {
    NodeService nodeService = new NodeServiceImplementation(this);

    MyIde.Configuration configuration = null;

    public ProjectServiceImplementation() {
        File folder = new File(".myIde");
        File file = new File(folder, "index");

        configuration = new MyIde.Configuration(file.toPath(), folder.toPath());
    }

    public ProjectServiceImplementation(MyIde.Configuration configuration) {
        this.configuration = configuration;
    }

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
        Node n = new NodeImplementation(root, Node.Types.FOLDER, null);
        // Build cache for search
        // TODO
        // Detect Git and Maven and add it to aspects
        // TODO
        return new ProjectImplementation(n, Set.of(new AnyAspect()));
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

