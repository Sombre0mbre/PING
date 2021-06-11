package fr.epita.assistants.myide.domain.entity.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstallImplementation implements Feature {

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        var param = new ArrayList<>(List.of("mvn", "install"));

        Arrays.stream(params).forEach((e) -> param.add(e.toString()));
        ProcessBuilder pb = new ProcessBuilder(param);
        pb.directory(new File(project.getRootNode().getPath().toString()));
        try {
            Process process = pb.start();
            process.waitFor();
            if (process.exitValue() == 0)
                return () -> true;
            else
                return () -> false;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return () -> false;
        }
        //throw new UnsupportedOperationException("FIXME");
        /*
        int returnCode = exec("mvn", "compile");
        return () -> (returnCode == 0);
        */
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Maven.INSTALL;

    }
}
