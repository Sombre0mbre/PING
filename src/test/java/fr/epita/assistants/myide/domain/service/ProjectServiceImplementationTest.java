package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.aspects.AnyAspect;
import fr.epita.assistants.myide.domain.entity.aspects.GitAspect;
import fr.epita.assistants.myide.domain.entity.aspects.MavenAspect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class ProjectServiceImplementationTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    public Project setUpDummy() throws IOException {
        var dir = Files.createTempDirectory("IDE_TEST_DUMMY_");
        var sub = Files.createDirectory(dir.resolve("test"));
        for (int i = 0; i < 10; i++) {
            final var str = "Test_" + i + ".txt";
            Files.write(sub.resolve(str), str.getBytes());
        }
        var service = new ProjectServiceImplementation();
        return service.load(dir);
    }

    public Project setUpGit() throws IOException, InterruptedException {
        var pb = new ProcessBuilder("git", "init");
        var dir = Files.createTempDirectory("IDE_TEST_GIT_");
        pb.directory(dir.toFile());
        pb.inheritIO();
        var proc = pb.start();

        var sub = Files.createDirectory(dir.resolve("test"));
        for (int i = 0; i < 10; i++)
            Files.createFile(sub.resolve("Test_" + i + ".txt"));

        proc.waitFor();
        var service = new ProjectServiceImplementation();
        return service.load(dir);
    }

    public Project setUpMaven() throws IOException {
        var dir = Files.createTempDirectory("IDE_TEST_GIT_");
        Files.createFile(dir.resolve("pom.xml"));
        var sub = Files.createDirectory(dir.resolve("test"));
        for (int i = 0; i < 10; i++)
            Files.createFile(sub.resolve("Test_" + i + ".txt"));
        var service = new ProjectServiceImplementation();
        return service.load(dir);
    }

    @Test
    void loadDummyFolder() throws IOException {
        var project = setUpDummy();

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
    void loadGitFolder() throws IOException, InterruptedException {
        var project = setUpGit();

        System.out.println();
        System.out.println("--- Project is ---");
        System.out.println(project);
        System.out.println("------------------");

        assert project.getAspects().size() == 2;
        var tmp = project.getAspects().stream()
                .map(Aspect::getClass).collect(Collectors.toSet());
        assert tmp.contains(AnyAspect.class);
        assert tmp.contains(GitAspect.class);

    }

    @Test
    void loadMavenFolder() throws IOException {
        var project = new ProjectServiceImplementationTest().setUpMaven();
        var aspects = project.getAspects().stream().map(Aspect::getClass).collect(Collectors.toSet());

        assert aspects.contains(MavenAspect.class);
        assert aspects.contains(AnyAspect.class);
        assert project.getAspects().size() == 2;
    }

    @Test
    void execute() throws IOException, InterruptedException {
        var project = setUpGit();
        var feature = project.getFeature(Mandatory.Features.Git.PULL);
        assert feature.isPresent();
        feature.get().execute(project);
    }
}