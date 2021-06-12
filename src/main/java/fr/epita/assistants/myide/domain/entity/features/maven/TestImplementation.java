package fr.epita.assistants.myide.domain.entity.features.maven;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.features.ProcessFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestImplementation implements ProcessFeature {

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        var param = new ArrayList<>(List.of("mvn", "test"));

        Arrays.stream(params).forEach((e) -> param.add(e.toString()));
        return this.executeProcess(project, param);
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Maven.TEST;
    }
}
