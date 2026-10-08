package Model;

import javafx.beans.property.DoubleProperty;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Monstre {
    private int vie;
    private DoubleProperty posX ;
    private DoubleProperty posY ;
    private int attaque;
    private int vitesse;
    private int hitbox;

    public Monstre(Integer vie,Integer attaque,Integer vitesse,Integer hitbox,Integer posX,Integer posY){
        this.vie = vie;
        this.posX = new SimpleDoubleProperty(posX);
        this.posY = new SimpleDoubleProperty(posY);
        this.attaque = attaque;
        this.hitbox = hitbox;
        this.vitesse =vitesse;
    }

    public int getHitbox() {
        return hitbox;
    }

    public double getPosX() {
        return posX.get();
    }

    public void setPosX(double posX) {
        this.posX.set(posX);
    }

    public DoubleProperty posXProperty() {
        return posX;
    }

    public double getPosY() {
        return posY.get();
    }

    public void setPosY(double posY) {
        this.posY.set(posY);
    }

    public DoubleProperty posYProperty() {
        return posY;
    }

    public int getVie() {
        return vie;
    }

    public void setVie(int vie) {
        this.vie = vie;
    }

    public void updateLife(){
        this.vie--;
    }

    public ImageView getMonstreImageView() {
        if (this.getVie()==1){return new ImageView("/monstre1vie.png");}
        else if (this.getVie()==2){return new ImageView("/monstre2vie.png");}
        else{return new ImageView("/monstre3vie.png");}
    }

    public void deplacementMonstre(double posXJoueur,double posYJoueur){
        double monstrePosX = this.getPosX();
        double monstrePosY = this.getPosY();

        // Calcul de la direction du mouvement
        double deltaX = posXJoueur - monstrePosX;
        double deltaY = posYJoueur - monstrePosY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Normalisation du vecteur de déplacement
        double moveX = (deltaX / distance) * 1;
        double moveY = (deltaY / distance) * 1;

        // Mise à jour des positions du monstre
        this.setPosX(monstrePosX + moveX);
        this.setPosY(monstrePosY + moveY);
    }
}

