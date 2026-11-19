
package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Passager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassagerRepository extends JpaRepository<Passager, Long> {
}
