package Application;

import Controller.*;
import Model.Jeu;
import Model.Personnage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Jeu jeuInstance;
    private static Stage primaryStage;
    private static Scene sceneAccueil;
    private static Scene sceneJeu;
    private static Scene sceneNouvellePartie;
    private static Scene sceneMap;
    private static Scene sceneAudio;

    private boolean modeDifficile;

    public static Personnage momie = new Personnage("Momie", 3, 5, 10);
    public static Personnage egyptien = new Personnage("Egyptien", 5, 3, 14);


    @Override
    public void start(Stage primaryStage) {

        try {
            this.primaryStage = primaryStage;

            // Charger et préparer la scène d'accueil
            FXMLLoader loaderAccueil = new FXMLLoader(getClass().getResource("../View/EcranAccueil.fxml"));
            Parent rootAccueil = loaderAccueil.load();
            sceneAccueil = new Scene(rootAccueil);

            // Charger et préparer la scène de nouvelle partie
            loaderAccueil = new FXMLLoader(getClass().getResource("../View/EcranNouvellePartie.fxml"));
            rootAccueil = loaderAccueil.load();
            sceneNouvellePartie = new Scene(rootAccueil);

            // Charger et préparer la scène audio
            loaderAccueil = new FXMLLoader(getClass().getResource("../View/EcranAudio.fxml"));
            rootAccueil = loaderAccueil.load();
            sceneAudio = new Scene(rootAccueil);

            // Définir la scène d'accueil comme scène initiale
            primaryStage.setScene(sceneAccueil);
            primaryStage.setTitle("ToutEnCanon");
            primaryStage.show();

            // Initialiser les contrôleurs
            Controller controller = new Controller(this);
            ControllerAccueil controllerAccueil = new ControllerAccueil(this);
            ControllerJeu controllerJeu = new ControllerJeu(this);
            ControllerNouvellePartie controllerNouvellePartie = new ControllerNouvellePartie(this);
            ControllerMap controllerMap = new ControllerMap(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setSceneJeu() {
        primaryStage.setScene(sceneJeu);
        primaryStage.show();
    }

    public static void setSceneMap() {
        primaryStage.setScene(sceneMap);
        primaryStage.show();
    }

    public static void setSceneAccueil() {
        primaryStage.setScene(sceneAccueil);
        primaryStage.show();
    }

    public static void  setSceneAudio(){
        primaryStage.setScene(sceneAudio);
        primaryStage.show();
    }

    public static void setSceneNouvellePartie() {
        primaryStage.setScene(sceneNouvellePartie);
        primaryStage.show();
    }

    public static Jeu getJeuInstance() {
        return jeuInstance;
    }

    public static void setJeuInstance(Jeu jeu) {
        jeuInstance = jeu;
    }

    public static void modifierSceneJeu(Scene sceneJeu) {
        App.sceneJeu = sceneJeu;
    }

    public static void modifierSceneMap(Scene sceneMap) {
        App.sceneMap = sceneMap;
    }
}