package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementationTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

class CleanupImplementationTest {
    @Test
    void execute() throws IOException {
        var project = new ProjectServiceImplementationTest().setUpDummy();
        System.out.println(project);
        var feature = project.getFeature(Mandatory.Features.Any.CLEANUP);
        assert feature.isPresent();
        assert !feature.get().execute(project).isSuccess();
        Files.createFile(project.getRootNode().getPath().resolve("toto.txt"));
        var ide = project.getRootNode().getPath().resolve(".myideignore").toFile();
        Files.write(ide.toPath(), "toto.txt\n".getBytes(), StandardOpenOption.CREATE_NEW);
        assert feature.get().execute(project).isSuccess();
        var root = project.getRootNode().getPath().toFile();
        for (File file : Objects.requireNonNull(root.listFiles())) {
            System.out.println(file);
        }
        assert Objects.requireNonNull(root.listFiles()).length == 2;

    }
}