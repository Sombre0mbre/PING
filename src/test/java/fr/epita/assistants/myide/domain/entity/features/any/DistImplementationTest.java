package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementationTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DistImplementationTest {

    @Test
    void execute() throws IOException {
        var project = new ProjectServiceImplementationTest().setUpDummy();
        System.out.println(project);
        var feature = project.getFeature(Mandatory.Features.Any.DIST);
        assert feature.isPresent();
        assert feature.get().execute(project).isSuccess();

    }
}