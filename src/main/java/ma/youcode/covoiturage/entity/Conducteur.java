//« Ici, nous avons l'entité Conducteur.
// Elle représente les informations d'un conducteur dans notre plateforme de
// covoiturage, comme son nom, son prénom, son adresse e-mail, son numéro de
// téléphone et son numéro de permis. Cette entité est liée à la table conducteurs dans notre base de données. Elle contient également un
// identifiant généré automatiquement ainsi que les getters et setters nécessaires pour accéder aux attributs et les modifier. »



package ma.youcode.covoiturage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

// Cette annotation indique que la classe est une entité JPA.
@Entity

// Définit le nom de la table correspondante dans la base de données.
@Table(name = "conducteurs")
public class Conducteur {

    // Identifiant unique du conducteur (clé primaire).
    @Id

    // L'identifiant est généré automatiquement par la base de données.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nom du conducteur : obligatoire dans la base de données.
    @Column(nullable = false)
    private String nom;

    // Prénom du conducteur : obligatoire dans la base de données.
    @Column(nullable = false)
    private String prenom;

    // Email obligatoire et unique pour chaque conducteur.
    @Column(nullable = false, unique = true)
    private String email;

    // Numéro de téléphone du conducteur : obligatoire.
    @Column(nullable = false)
    private String telephone;

    // Numéro de permis obligatoire et unique.
    @Column(nullable = false, unique = true)
    private String numeroPermis;

    // Constructeur vide nécessaire pour JPA/Hibernate.
    public Conducteur() {
    }

    // Getter : permet de récupérer l'identifiant.
    public Long getId() {
        return id;
    }

    // Getter et Setter du nom.
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter et Setter du prénom.
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // Getter et Setter de l'email.
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter et Setter du téléphone.
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // Getter et Setter du numéro de permis.
    public String getNumeroPermis() {
        return numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }
}
