package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.aspects.AnyAspect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class ProjectServiceImplementationTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    Path setUpDummy() throws IOException {
        var dir = Files.createTempDirectory("IDE_TEST_DUMMY_");
        var sub = Files.createDirectory(dir.resolve("test"));
        for (int i = 0; i < 10; i++)
            Files.createFile(sub.resolve("Test_" + i + ".txt"));
        return dir;
    }

    @Test
    void loadDummyFolder() throws IOException {
        var root = setUpDummy();
        var service = new ProjectServiceImplementation();
        var project = service.load(root);

        System.out.println();
        System.out.println("--- Project is ---");
        System.out.println(project);
        System.out.println("------------------");

        assert project.getRootNode().getChildren().size() == 1;
        assert project.getRootNode().getChildren().get(0).getChildren().size() == 10;
        assert project.getAspects().size() == 1;
        assert project.getAspects().stream().toList().get(0).getClass().equals(AnyAspect.class);

    }

    @Test
    void execute() {
    }
}