
package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Passager;
import ma.youcode.covoiturage.repository.PassagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassagerServiceImpl implements PassagerService {

    private final PassagerRepository passagerRepository;

    public PassagerServiceImpl(PassagerRepository passagerRepository) {
        this.passagerRepository = passagerRepository;
    }

    @Override
    public Passager creer(Passager passager) {
        return passagerRepository.save(passager);
    }

    @Override
    public List<Passager> lister() {
        return passagerRepository.findAll();
    }

    @Override
    public Passager chercherParId(Long id) {
        return passagerRepository.findById(id).orElse(null);
    }

    @Override
    public Passager modifier(Long id, Passager passager) {
        Passager existant = chercherParId(id);

        if (existant == null) {
            return null;
        }

        existant.setNom(passager.getNom());
        existant.setPrenom(passager.getPrenom());
        existant.setEmail(passager.getEmail());
        existant.setTelephone(passager.getTelephone());

        return passagerRepository.save(existant);
    }

    @Override
    public void supprimer(Long id) {
        Passager passager = chercherParId(id);

        if (passager != null) {
            passagerRepository.delete(passager);
        }
    }
}
