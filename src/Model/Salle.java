package Model;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.*;

public class Salle {
    private int idSalle;
    private boolean porteTOP;
    private boolean porteBOTTOM;
    private boolean porteLEFT;
    private boolean porteRIGHT;
    private boolean salleBoss;
    private boolean isPotion;
    private boolean isCle;
    private static int nbSalle = 0;

    private boolean visited;

    private List<Monstre> monstresSalle;

    /**Creer une instance de la classe salle initialisé avec au minimum 2 portes existantes
     *
     */
    public Salle() {

        this.idSalle = nbSalle;
        nbSalle++;

        List<String> portes = new ArrayList<>();
        portes.add("TOP");
        portes.add("BOTTOM");
        portes.add("LEFT");
        portes.add("RIGHT");

        // Nombre de portes à initialiser à true (entre 2 et 4)
        Random rand = new Random();
        int nombreDePortes = 2 + rand.nextInt(3); // nextInt(3) donne un nombre entre 0 et 2 inclus
        //Ajout d'autant de monstres que de porte ouvrable
        this.monstresSalle = new ArrayList<>();

        // Rajoute autant de monstres que de portes
        /*
        for (int i = 0; i < nombreDePortes; i++) {
            Monstre monstre = new Monstre(3,1,6,100,100+100*i,400); // Créer un monstre (à adapter selon votre structure)
            monstresSalle.add(monstre);
        }*/
        // Mélanger la liste des portes
        Collections.shuffle(portes);

        // Initialiser les portes à false par défaut
        this.porteTOP = false;
        this.porteBOTTOM = false;
        this.porteLEFT = false;
        this.porteRIGHT = false;
        this.salleBoss = false;
        this.visited = false;

        // Initialiser les portes aléatoires à true
        for (int i = 0; i < nombreDePortes; i++) {
            switch (portes.get(i)) {
                case "TOP":
                    this.porteTOP = true;
                    break;
                case "BOTTOM":
                    this.porteBOTTOM = true;
                    break;
                case "LEFT":
                    this.porteLEFT = true;
                    break;
                case "RIGHT":
                    this.porteRIGHT = true;
                    break;
            }
        }
    }

    public boolean isCle() {
        return isCle;
    }

    public boolean isPotion() {
        return isPotion;
    }

    public void setCle(boolean cle) {
        isCle = cle;
    }

    public void setPotion(boolean potion) {
        isPotion = potion;
    }

    public List<Monstre> getMonstresSalle() {
        return monstresSalle;
    }

    public boolean checkCollisionWithMonstres(Circle bille) {
        Iterator<Monstre> iterator = monstresSalle.iterator();
        while (iterator.hasNext()) {
            Monstre monstre = iterator.next();

            // Coordonnées et dimensions du rectangle du monstre
            double monstreX = monstre.getPosX();
            double monstreY = monstre.getPosY();
            double monstreWidth = 100; // Largeur du monstre
            double monstreHeight = 100; // Hauteur du monstre

            // Coordonnées et rayon de la bille
            double billeCenterX = bille.getCenterX();
            double billeCenterY = bille.getCenterY();
            double billeRadius = bille.getRadius();

            // Vérifier si le centre de la bille est à l'intérieur du rectangle du monstre
            if (billeCenterX + billeRadius >= monstreX &&
                    billeCenterX - billeRadius <= monstreX + monstreWidth &&
                    billeCenterY + billeRadius >= monstreY &&
                    billeCenterY - billeRadius <= monstreY + monstreHeight) {

                System.out.println("Position bille : (" + bille.getCenterX() + ", " + bille.getCenterY() + ")");
                System.out.println("Position monstre : (" + monstre.getPosX() + ", " + monstre.getPosY() + ")");
                System.out.println("Condition de collision : " + (monstre.getPosX() + monstre.getHitbox() >= bille.getCenterX() - bille.getRadius() &&
                        monstre.getPosX() - monstre.getHitbox() <= bille.getCenterX() + bille.getRadius() &&
                        monstre.getPosY() + monstre.getHitbox() >= bille.getCenterY() - bille.getRadius() &&
                        monstre.getPosY() - monstre.getHitbox() <= bille.getCenterY() + bille.getRadius()));
                monstre.updateLife();

                if (monstre.getVie() <= 0) {
                    iterator.remove();
                }

                return true;
            }
        }
        return false;
    }

    public void salleBoss() {
        // Initialiser les portes à false par défaut
        this.porteTOP = true;
        this.porteBOTTOM = true;
        this.porteLEFT = true;
        this.porteRIGHT = true;
        this.salleBoss = true;
    }


    public int getIdSalle() {
        return idSalle;
    }

    public boolean isPorteTOP() {
        return porteTOP;
    }

    public boolean isPorteBOTTOM() {
        return porteBOTTOM;
    }

    public boolean isPorteLEFT() {
        return porteLEFT;
    }

    public boolean isPorteRIGHT() {
        return porteRIGHT;
    }

    public boolean isSalleBoss() {return salleBoss;}

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public String toString(){
        return("Salle"+this.idSalle);
    }

    public ImageView getImagePotion() {
        if(isPotion){return new ImageView("/potion.png");}
        else{return new ImageView("/plus_potion.png");}
    }
}
