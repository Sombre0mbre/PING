package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementationTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

class DistImplementationTest {

    @Test
    void execute() throws IOException, InterruptedException {
        var project = new ProjectServiceImplementationTest().setUpGit();
        System.out.println(project);
        Files.createFile(project.getRootNode().getPath().resolve(".myideignore"));
        Files.createDirectory(project.getRootNode().getPath().resolve("empty"));
        var feature = project.getFeature(Mandatory.Features.Any.DIST);
        assert feature.isPresent();
        assert feature.get().execute(project).isSuccess();

    }
}