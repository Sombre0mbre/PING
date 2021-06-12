package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.NodeImplementation;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.NodeServiceImplementation;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class CleanupImplementation implements Feature {
    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        try {
            File ignore = new File(".myideignore");
            Scanner myReader = new Scanner(ignore);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                File toDel = new File(data);
                if (toDel.isDirectory())
                    FileUtils.deleteDirectory(toDel);
                else if (toDel.isFile())
                    Files.delete(Path.of(data));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.err.println("File .myideignore not found");
            e.printStackTrace();
            return () -> false;
        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }
        return () -> true;
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Any.CLEANUP;
    }
}
