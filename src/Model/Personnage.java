package Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Personnage {
    private final String nomClasse;
    private int posx;
    private int posy;
    private int hitBox;
    private BooleanProperty cle;
    private IntegerProperty nbDeVieListener;
    private IntegerProperty nbDePotionsListener;
    private final int attaque;
    private final int vitesse;

    public Personnage(String classPerso, Integer attaque, Integer vie, Integer vitesse){
        this.posx = 400;
        this.posy = 400;
        this.hitBox = 100;//à revoir
        this.nbDePotionsListener = new SimpleIntegerProperty(1);
        this.cle = new SimpleBooleanProperty();
        this.nomClasse = classPerso;
        this.nbDeVieListener= new SimpleIntegerProperty(vie);
        this.attaque=attaque;
        this.vitesse=vitesse;
    }

    public boolean isCle() {
        return cle.get();
    }

    public void setCle(boolean cle) {
        this.cle.set(cle);
    }

    public BooleanProperty cleProperty() {
        return cle;
    }

    public int getPotion() {
        return nbDePotionsListener.get();
    }

    public int getAttaque() {
        return attaque;
    }

    public int getNbDeVie() {
        return nbDeVieListener.get();
    }

    public int getVitesse() {
        return vitesse;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    //ramasse soit un objet potion ou un objet cle
    public void ramasser(String objet){
        if(objet == "potion"){
            this.recupererPotion();
        }else{
            this.setCle(true);
        }
    }

    public IntegerProperty nbDePotionsProperty() {
        return nbDePotionsListener;
    }
    public void setNbDePotions(int nbDePotions) {
        this.nbDePotionsListener.set(nbDePotions);
    }

    public void utiliserPotion(){
        setNbDePotions(getPotion()-1);
    }
    public void recupererPotion(){
        setNbDePotions(getPotion()+1);

    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }
    public IntegerProperty nbDeVieProperty() {
        return nbDeVieListener;
    }
    public void setNbDeVie(int nbDeVie) {
        this.nbDeVieListener.set(nbDeVie);
    }
    public void decrementNbDeVie(Integer degat) {
        setNbDeVie(getNbDeVie() - 1);
    }

    public void incrementNbDeVie() {
        setNbDeVie(getNbDeVie() + 1);
    }

    //enlève de la vie si il en a
    public void prendreDegat(int degat){
        if(nbDeVieListener.get()>1){
            decrementNbDeVie(degat);
        }
        else{
            if(this.nbDeVieListener.get()<1){Jeu.findujeu();}
        }
    }

    //soigne la vie et utilise une potion
    public void seSoigner(){
        if(nbDeVieListener.get()<=4 && this.getPotion()>=1) {
            this.incrementNbDeVie();
            System.out.println("nombre de potion mtn :"+this.getPotion());
            this.utiliserPotion();
            System.out.println("vie récupéré2, nombre de vie mtn :"+this.getPotion());
        }
        else{System.out.println("vie max ou pas de potion");}
    }

}
