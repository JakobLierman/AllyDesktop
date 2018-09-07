import domain.DomainController;
import domain.ProductManager;
import gui.HomeScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class StartUp extends Application {

    private static ResourceBundle resourceBundle;

    @Override
    public void start(Stage primaryStage) {
        // DomainController creation
        DomainController domainController = new DomainController(new ProductManager(), resourceBundle);
        // Set initial scene
        Scene scene = new Scene(new HomeScreen<>(domainController, "Product"));
        primaryStage.setScene(scene);

        // Set title and show app
        primaryStage.setTitle(domainController.getResourceBundle().getString("Title"));
        primaryStage.show();

        // Add stylesheets and fonts
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Open+Sans");

        // Close app
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Closed");
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        // Set language
        Locale locale;
        if (args.length < 1)
            locale = Locale.getDefault();
        else
            locale = new Locale(args[0]);
        resourceBundle = ResourceBundle.getBundle("Bundle.properties", locale);

        // Start GUI
        Application.launch(StartUp.class, args);
    }

}
