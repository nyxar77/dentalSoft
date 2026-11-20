
package ma.dentalSoft.domain;

import java.util.Objects;

public abstract class User {


    private Long id;
    private String nom;
    private String email;
    private String motDePasse;
    private Role role;


    protected User() {
    }


    protected User(Long id, String nom, String email,
                   String motDePasse, Role role) {

        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter et Setter de nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }


    public Role getRole() {
        return role;
    }

    protected void setRole(Role role) {
        this.role = role;
    }

    // Comparaison de deux utilisateurs
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;

        return id != null && id.equals(user.id);
    }

    // Calcul du hashCode
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
