package ma.dentalSoft.domain;

public class Medecin extends User {

    private String specialite;

    public Medecin() {
        setRole(Role.MEDECIN);
    }

    public Medecin(Long id, String nom, String email,
                   String motDePasse, String specialite) {

        super(id, nom, email, motDePasse, Role.MEDECIN);
        this.specialite = specialite;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}