package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import javax.validation.constraints.NotNull;
import java.io.FileReader;
import java.io.IOException;

public class SearchImplementation implements Feature {

    final MyIde.Configuration configuration;

    public SearchImplementation(MyIde.Configuration configuration) {
        this.configuration = configuration;
    }

    private void generateIndexes(@NotNull Node folder, IndexWriter writer) {
        if (!folder.isFolder())
            return;
        var files = folder.getChildren();

        for (var n : files) {
            try {
                var file = n.getPath().toFile();
                var fileReader = new FileReader(file);
                var document = new Document();

                document.add(new TextField("contents", fileReader));
                document.add(new StringField("path", file.getPath(), Field.Store.YES));
                document.add(new StringField("filename", file.getName(), Field.Store.YES));

                writer.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            generateIndexes(n, writer);
        }
    }

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        try {
            /*
            var directory = FSDirectory.open(configuration.tempFolder());
            var analyzer = new StandardAnalyzer();
            var config = new IndexWriterConfig(analyzer);
            var writer = new IndexWriter(directory, config);

            generateIndexes(project.getRootNode(), writer);

            String fieldString;
            String queryString;
            if (params.length == 1) {
                fieldString = "content";
                queryString = params[0].toString();
            } else if (params.length > 0) {
                fieldString = params[0].toString();
                queryString = params[1].toString();
            } else {
                return () -> false;
            }

            Query query = new QueryParser(fieldString, analyzer).parse(queryString);
            Directory indexDirectory = FSDirectory.open(project.getRootNode().getPath());
            IndexReader indexReader = DirectoryReader.open(indexDirectory);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 10);

            // To be returned somewhere
            var res = Arrays.stream(topDocs.scoreDocs).map(scoreDoc -> searcher.doc(scoreDoc.doc)).toList();

            writer.close();
             */
            return () -> true;
        } catch (Exception e) {
            e.printStackTrace();
            return () -> false;
        }
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Any.SEARCH;
    }
}
