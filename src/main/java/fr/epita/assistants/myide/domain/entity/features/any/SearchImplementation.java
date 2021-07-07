package fr.epita.assistants.myide.domain.entity.features.any;

import com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer;
import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.validation.constraints.NotNull;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchImplementation implements Feature {

    interface SearchReport extends ExecutionReport {
        List<Document> getResult();
    }

    final SearchReport failedSearch = new SearchReport() {
        @Override
        public List<Document> getResult() {
            return null;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }
    };

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
                    var document = new Document();

                    document.add(new StringField("path", file.getPath(), Field.Store.YES));
                    document.add(new TextField("filename", file.getName(), Field.Store.YES));

                    if (n.isFile()) {
                        var fileReader = new FileReader(file);
                        document.add(new TextField("contents", fileReader));

                        writer.addDocument(document);
                        fileReader.close();
                    }
                    else {
                        writer.addDocument(document);
                    }


                } catch (IOException e) {
                    System.err.println("Failed indexing for " + n.getPath());
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
    public SearchReport execute(Project project, Object... params) {
        try {
            var directory = FSDirectory.open(configuration.tempFolder().resolve(project.getRootNode().getPath().getFileName()));
            var analyzer = new StandardAnalyzer();
            var config = new IndexWriterConfig(analyzer);
            var writer = new IndexWriter(directory, config);

            generateIndexes(project.getRootNode(), writer);
            System.out.println(writer);

            String fieldString;
            String queryString;
            if (params.length == 1) {
                fieldString = "contents";
                queryString = params[0].toString();
            } else if (params.length > 0) {
                fieldString = params[0].toString();
                queryString = params[1].toString();
            } else {
                return failedSearch;
            }


            IndexReader indexReader = DirectoryReader
                    .open(writer);
            IndexSearcher searcher = new IndexSearcher(indexReader);


            Query query = new QueryParser(fieldString, analyzer)
                    .parse(queryString);

            TopDocs topDocs = searcher.search(query, 10);
            System.out.println(query);
            System.out.println(topDocs.totalHits);

            var res = Arrays.stream(topDocs.scoreDocs)
                    .map(scoreDoc -> {
                        try {
                            return searcher.doc(scoreDoc.doc);
                        } catch (IOException e) {
                            throw new UnsupportedOperationException();
                        }
                    }).toList();

            writer.close();
            return new SearchReport() {
                @Override
                public List<Document> getResult() {
                    return res;
                }

                @Override
                public boolean isSuccess() {
                    return true;
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            return failedSearch;
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
