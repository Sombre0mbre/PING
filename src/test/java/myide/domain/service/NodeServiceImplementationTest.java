package myide.domain.service;

import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.ProjectService;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

class NodeServiceImplementationTest {
    ProjectService projectService = new ProjectServiceImplementation();
    Project project = null;

    @BeforeEach
    void setUp() {
        Path root;
        try {
            root = Files.createTempDirectory("PING_TEST_");
            System.out.println("Test directory: " + root.toString());
        } catch (IOException e) {
            throw new UnsupportedOperationException();
        }
        this.project = projectService.load(root);
    }

    @Test
    void create() {
        final var nodeService = projectService.getNodeService();

        assert project.getRootNode().getChildren().size() == 0;
        var tmp = nodeService.create(project.getRootNode(), "tempDir1", Node.Types.FOLDER);
        assert tmp.getChildren().size() == 0;
        System.out.println("\n-> PATH: " + project.getRootNode().getPath());
        project.getRootNode().getChildren().forEach(System.out::println);
        System.out.println("END");
        assert project.getRootNode().getChildren().size() == 1;
        assert Files.isDirectory(tmp.getPath());

        var tmp1 = nodeService.create(tmp, "tempDir1", Node.Types.FILE);
        assert tmp1.getChildren().size() == 0;
        System.out.println("\n-> PATH: " + tmp.getPath());
        tmp.getChildren().forEach(System.out::println);
        System.out.println("END");
        assert tmp.getChildren().size() == 1;
        assert Files.isRegularFile(tmp1.getPath());
    }

    @Test
    void move() {
        final var nodeService = projectService.getNodeService();

        assert project.getRootNode().getChildren().size() == 0;
        var dir1 = nodeService.create(project.getRootNode(), "tempDir1", Node.Types.FOLDER);
        var dir2 = nodeService.create(project.getRootNode(), "tempDir2", Node.Types.FOLDER);

        var file = nodeService.create(dir1, "hello", Node.Types.FILE);

        assert Objects.requireNonNull(dir1.getPath().toFile().listFiles()).length == 1;
        assert Objects.requireNonNull(dir2.getPath().toFile().listFiles()).length == 0;
        assert dir1.getChildren().size() == 1;
        assert dir2.getChildren().size() == 0;

        nodeService.move(file, dir2);

        assert dir1.getChildren().size() == 0;
        assert dir2.getChildren().size() == 1;
        assert Objects.requireNonNull(dir1.getPath().toFile().listFiles()).length == 0;
        assert Objects.requireNonNull(dir2.getPath().toFile().listFiles()).length == 1;


        file = nodeService.create(dir1, "hello", Node.Types.FILE);
        nodeService.move(file, null);
    }

    @Test
    void update() {

    }

    @Test
    void delete() {
        final var nodeService = projectService.getNodeService();

        assert project.getRootNode().getChildren().size() == 0;
        var dir = nodeService.create(project.getRootNode(), "tempDir", Node.Types.FOLDER);
        nodeService.create(dir, "tempFile1", Node.Types.FILE);

        var file = nodeService.create(project.getRootNode(), "tempFile2", Node.Types.FILE);
        assert Objects.requireNonNull(dir.getPath().toFile().listFiles()).length == 1;
        assert dir.getChildren().size() == 1;
        assert Objects.requireNonNull(project.getRootNode().getPath().toFile().listFiles()).length == 2;
        assert project.getRootNode().getChildren().size() == 2;
        nodeService.delete(dir);
        nodeService.delete(file);
        assert Objects.requireNonNull(project.getRootNode().getPath().toFile().listFiles()).length == 0;
        assert project.getRootNode().getChildren().size() == 0;
    }

}