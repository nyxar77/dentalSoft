package ma.dentalSoft.domain;

public class Admin extends User {

    public Admin() {
        setRole(Role.ADMIN);
    }

    public Admin(Long id, String nom, String email,
                 String motDePasse) {
        super(id, nom, email, motDePasse, Role.ADMIN);
    }
}