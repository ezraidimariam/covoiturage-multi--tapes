
package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    java.util.List<Vehicule> findByConducteur_Id(Long conducteurId);

    boolean existsByImmatriculation(String immatriculation);

    boolean existsByImmatriculationAndIdNot(String immatriculation, Long id);
}
