package fr.epita.assistants.myide.domain.entity.features;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.IOException;
import java.util.List;

public interface ProcessFeature extends Feature {
    default ExecutionReport executeProcess(Project project, List<String> args) {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(project.getRootNode().getPath().toFile());
        try {
            Process process = pb.start();
            return () -> {
                try {
                    process.waitFor();
                    return process.exitValue() == 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            };
        } catch (IOException e) {
            return () -> false;
        }

    }
}
