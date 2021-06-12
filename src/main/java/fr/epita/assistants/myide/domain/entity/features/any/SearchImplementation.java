package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class SearchImplementation implements Feature {

    final MyIde.Configuration configuration;

    public SearchImplementation(MyIde.Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        try {
            var directory = FSDirectory.open(configuration.tempFolder());

        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }
        throw new UnsupportedOperationException("FIXME");
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Any.SEARCH;
    }
}
