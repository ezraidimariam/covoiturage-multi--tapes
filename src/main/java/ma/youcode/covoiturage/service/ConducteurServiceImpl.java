package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.entity.Conducteur;
import ma.youcode.covoiturage.repository.ConducteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConducteurServiceImpl implements ConducteurService {

    private final ConducteurRepository conducteurRepository;

    public ConducteurServiceImpl(ConducteurRepository conducteurRepository) {
        this.conducteurRepository = conducteurRepository;
    }

    @Override
    public Conducteur creer(Conducteur conducteur) {
        return conducteurRepository.save(conducteur);
    }

    @Override
    public List<Conducteur> lister() {
        return conducteurRepository.findAll();
    }

    @Override
    public Conducteur chercherParId(Long id) {
        return conducteurRepository.findById(id).orElse(null);
    }

    @Override
    public Conducteur modifier(Long id, Conducteur conducteur) {
        Conducteur existant = chercherParId(id);

        if (existant == null) {
            return null;
        }

        existant.setNom(conducteur.getNom());
        existant.setPrenom(conducteur.getPrenom());
        existant.setEmail(conducteur.getEmail());
        existant.setTelephone(conducteur.getTelephone());
        existant.setNumeroPermis(conducteur.getNumeroPermis());

        return conducteurRepository.save(existant);
    }

    @Override
    public void supprimer(Long id) {
        conducteurRepository.deleteById(id);
    }
}