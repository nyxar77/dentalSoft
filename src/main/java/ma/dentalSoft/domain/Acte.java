package ma.dentalSoft.domain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Acte {

    private int idA;
    private String nom;
    private String categorie;
    private double prixDeBase;

    public Acte() {
    }

    public Acte(int idA, String nom, String categorie, double prixDeBase) {
        this.idA = idA;
        this.nom = nom;
        this.categorie = categorie;
        this.prixDeBase = prixDeBase;
    }
}