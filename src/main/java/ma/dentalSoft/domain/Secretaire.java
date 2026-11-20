package ma.dentalSoft.domain;

public class Secretaire extends User {

    public Secretaire() {
        setRole(Role.SECRETAIRE);
    }

    public Secretaire(Long id, String nom, String email,
                      String motDePasse) {

        super(id, nom, email, motDePasse, Role.SECRETAIRE);
    }
}