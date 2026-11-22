package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConducteurRepository extends JpaRepository<Conducteur, Long> {
}

//« JpaRepository nous fournit des méthodes prédéfinies pour effectuer les opérations CRUD sans écrire les requêtes SQL manuellement.
// Cela réduit le code et facilite l'accès à la base de données. »