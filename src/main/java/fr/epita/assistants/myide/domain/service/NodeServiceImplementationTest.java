package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.NodeImplementation;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.ProjectImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void testMove() {
    }

}