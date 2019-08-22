// Ankith Kandikonda, avk393
// Donovan McCray, dom342

package assignment5;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    /* Gets the package name.  The usage assumes that Critter and its
       subclasses are all in the same package. */
    private static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    public static void main(String[] args) {
         launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        CritterWorld game = new CritterWorld();
        Stage secondStage = new Stage();
        game.setUpControlsView(primaryStage);
        game.initializeWorld(secondStage);

    }

}
