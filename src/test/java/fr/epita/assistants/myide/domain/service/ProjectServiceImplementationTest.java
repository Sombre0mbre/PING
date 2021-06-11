package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.aspects.AnyAspect;
import fr.epita.assistants.myide.domain.entity.aspects.GitAspect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

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

    private Project setUpGit() throws IOException, InterruptedException {
        var pb = new ProcessBuilder("git", "init");
        var root = Files.createTempDirectory("IDE_TEST_GIT_");
        pb.directory(root.toFile());
        pb.inheritIO();
        var proc = pb.start();
        proc.waitFor();
        var service = new ProjectServiceImplementation();
        return service.load(root);
    }

    @Test
    void loadGitFolder() throws IOException, InterruptedException {
        var project = setUpGit();

        System.out.println();
        System.out.println("--- Project is ---");
        System.out.println(project);
        System.out.println("------------------");

        assert project.getAspects().size() == 2;
        var tmp = project.getAspects().stream().map(Aspect::getClass).collect(Collectors.toSet());
        assert tmp.contains(AnyAspect.class);
        assert tmp.contains(GitAspect.class);

    }

    @Test
    void execute() throws IOException, InterruptedException{
        var project = setUpGit();
        project.getFeature(Mandatory.Features.Git.PULL).get().execute(project);
    }
}