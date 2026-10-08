package Controller;

import Application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class ControllerAccueil {

    @FXML
    public Button btnContinuer;

    @FXML
    public Label errorMessageLabel;

    private App app;

    public ControllerAccueil(App app){
        this.app = app;
    }

    public ControllerAccueil(){}


    @FXML
    public void onPushBottonContinuer(ActionEvent actionEvent) throws IOException {
        if (App.getJeuInstance() == null) {
            // Afficher le message d'erreur si aucune instance de jeu n'existe
            errorMessageLabel.setText("Veuillez commencer une nouvelle partie avant de continuer.");
        } else {
            // Passez à la scène suivante si une instance de jeu existe
            App.setSceneJeu();
        }

    }

    public void onPushBtnNouvellePartie(ActionEvent actionEvent) {
        App.setSceneNouvellePartie();
    }

    public void onPushBtnAudio(ActionEvent actionEvent) {
        App.setSceneAudio();
    }

    public void onPushBtnQuitter(ActionEvent actionEvent) {
        System.exit(0);
    }
}
