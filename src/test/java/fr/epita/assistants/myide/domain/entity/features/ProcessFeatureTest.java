package fr.epita.assistants.myide.domain.entity.features;


import fr.epita.assistants.myide.domain.entity.aspects.MavenAspect;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementationTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ProcessFeatureTest {

    @Test
    void executeProcess() throws IOException {
        var project = new ProjectServiceImplementationTest().setUpMaven();

        var maven = project.getAspects().stream()
                .filter((i) -> MavenAspect.class.equals(i.getClass()))
                .findAny();

        assert maven.isPresent();

        for (var i : maven.get().getFeatureList()) {
            i.execute(project);
        }
    }
}