package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.service.ProjectServiceImplementationTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;


class SearchImplementationTest {

    public void printTree(Node n) {
        System.out.println(n.getPath());
        for (var i : n.getChildren()) {
            printTree(i);
        }
    }

    @Test
    void execute() throws IOException {
        var project = new ProjectServiceImplementationTest().setUpDummy();
        System.out.println(project);
        printTree(project.getRootNode());

        var featureOpt = project.getFeature(Mandatory.Features.Any.SEARCH);
        assert featureOpt.isPresent();
        var feature = featureOpt.get();

        var tmp = (SearchImplementation.SearchReport) feature.execute(project, "contents", "Test*");
        System.out.println("Got:");
        tmp.getResult().forEach(System.out::println);
    }
}