package fr.epita.assistants.myide.domain.javafx.utils;

public class TutorialArrays {
    public final static TutorialElement[] gitElements = {
            new TutorialElement(
                    "Push",
                    "Description 1"
            ),
            new TutorialElement(
                    "Add",
                    """
                            La commande Add est utilisée pour ajouter des fichiers au dépôt.
                            Par exemple, la commande suivante ajoutera un fichier nommé temp.txt au répertoire local :

                            git add temp.txt
                            """
            ),
            new TutorialElement(
                    "Commit",
                    """
                            La commande Commit permet de valider les changements apportés.
                            Attention, il ne valide les changements que localement, sur votre ordinateur.
                            Il est habituellement accompagné d’un message de description :
                                                        
                            git commit –m “Description du commit”
                            """
            )
    };

    public final static TutorialElement[] mavenElements = {
            new TutorialElement(
                    "Compile",
                    "Description 4"
            ),
            new TutorialElement(
                    "Clean",
                    "Description 5"
            ),
            new TutorialElement(
                    "Exec",
                    "Description 6"
            ),
            new TutorialElement(
                    "Install",
                    "Description 7"
            ),
            new TutorialElement(
                    "Test",
                    "Description 8"
            ),
            new TutorialElement(
                    "Tree",
                    "Description 9"
            )
    };

    public final static TutorialElement[] javaElements = {
            new TutorialElement(
                    "Conditions",
                    "Description 10"
            ),
            new TutorialElement(
                    "Boucles",
                    "Description 11"
            ),
            new TutorialElement(
                    "Fonctions",
                    "Description 12"
            ),
            new TutorialElement(
                    "Classes",
                    "Description 13"
            )
    };
}
