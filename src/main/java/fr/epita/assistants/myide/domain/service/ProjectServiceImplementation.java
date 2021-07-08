package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.entity.aspects.AnyAspect;
import fr.epita.assistants.myide.domain.entity.aspects.GitAspect;
import fr.epita.assistants.myide.domain.entity.aspects.MavenAspect;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

public class ProjectServiceImplementation implements ProjectService {
    NodeServiceImplementation nodeService = new NodeServiceImplementation(this);

    MyIde.Configuration configuration;

    public ProjectServiceImplementation() {
        File folder = new File(".myIde");
        File file = new File(folder, "index");
        try {
            if (!folder.isDirectory())
                Files.createDirectory(folder.toPath());
            if (!file.isFile())
                Files.createFile(file.toPath());
        }
        catch (Exception ignored) {}
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
        nodeService.generateChildren(n);
        var aspects = new HashSet<Aspect>();
        aspects.add(new AnyAspect(configuration));

        // Build cache for search
        // TODO
        // Detect Git and Maven and add it to aspects
        File pom = new File(root.toString(), "pom.xml");
        if (pom.exists())
            aspects.add(new MavenAspect());
        /*File[] root_contents = root.toFile().listFiles();
        for (var content : root_contents) {
            if (content.getName() == "pom.xml") {
                aspects.add(new MavenAspect());
                break;
            }
        }*/
        // TODO
        File gitFile = root.resolve(".git/").toFile();
        if (gitFile.exists() && gitFile.isDirectory()) {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository;
            try {
                repository = builder.setGitDir(root.resolve(".git").toFile())
                        .readEnvironment()
                        .findGitDir()
                        .build();
                aspects.add(new GitAspect(new Git(repository)));
            } catch (IOException ignored) {
            }
        }


        return new ProjectImplementation(n, aspects);
    }

    public Project create(Path root) throws IOException {
        if (root.toFile().isDirectory())
            throw new IllegalArgumentException("Path already exists");

        var path = Files.createDirectory(root);
        return load(path);
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

