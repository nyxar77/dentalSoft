package ma.dentalSoft.domain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Medicament {

    private int id;
    private String nom;
    private String fournisseur;
    private double prixEstime;

    public Medicament() {
    }

    public Medicament(int id, String nom, String fournisseur, double prixEstime) {
        this.id = id;
        this.nom = nom;
        this.fournisseur = fournisseur;
        this.prixEstime = prixEstime;
    }
}