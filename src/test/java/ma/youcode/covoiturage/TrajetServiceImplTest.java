package ma.youcode.covoiturage;

import ma.youcode.covoiturage.entity.PointArret;
import ma.youcode.covoiturage.entity.StatutTrajet;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.repository.PointArretRepository;
import ma.youcode.covoiturage.repository.ReservationRepository;
import ma.youcode.covoiturage.repository.TrajetRepository;
import ma.youcode.covoiturage.service.TrajetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrajetServiceImplTest {

    @Mock private TrajetRepository trajetRepository;
    @Mock private PointArretRepository pointArretRepository;
    @Mock private ReservationRepository reservationRepository;
    @InjectMocks private TrajetServiceImpl trajetService;

    @Test
    void publierValideUnItineraireEtChangeLeStatut() {
        LocalDateTime depart = LocalDateTime.now().plusDays(2);
        Trajet trajet = trajet(depart, null);
        PointArret premier = point(trajet, "Rabat", 0, depart, "0.00");
        PointArret dernier = point(trajet, "Marrakech", 1, depart.plusHours(4), "120.00");
        when(trajetRepository.findById(7L)).thenReturn(Optional.of(trajet));
        when(pointArretRepository.findAll()).thenReturn(List.of(premier, dernier));
        when(trajetRepository.save(trajet)).thenReturn(trajet);

        Trajet publie = trajetService.publier(7L);

        assertThat(publie.getStatut()).isEqualTo(StatutTrajet.PUBLIE);
        verify(trajetRepository).save(trajet);
    }

    @Test
    void refuserPublierSansDeuxPointsArret() {
        Trajet trajet = trajet(LocalDateTime.now().plusDays(2), null);
        when(trajetRepository.findById(7L)).thenReturn(Optional.of(trajet));
        when(pointArretRepository.findAll()).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> trajetService.publier(7L)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(trajetRepository, never()).save(any(Trajet.class));
    }

    @Test
    void refuserTerminerAvantLeDepart() {
        Trajet trajet = trajet(LocalDateTime.now().plusHours(2), StatutTrajet.PUBLIE);
        when(trajetRepository.findById(7L)).thenReturn(Optional.of(trajet));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> trajetService.terminer(7L)
        );

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(trajetRepository, never()).save(any(Trajet.class));
    }

    private Trajet trajet(LocalDateTime depart, StatutTrajet statut) {
        Trajet trajet = new Trajet();
        trajet.setDateDepart(depart);
        trajet.setVilleDepart("Rabat");
        trajet.setVilleArrivee("Marrakech");
        trajet.setStatut(statut);
        return trajet;
    }

    private PointArret point(Trajet trajet, String ville, int ordre,
                             LocalDateTime heure, String prix) {
        PointArret point = mock(PointArret.class);
        when(point.getTrajet()).thenReturn(trajet);
        when(point.getVille()).thenReturn(ville);
        when(point.getOrdre()).thenReturn(ordre);
        when(point.getHeurePassage()).thenReturn(heure);
        when(point.getPrixDepuisDepart()).thenReturn(new BigDecimal(prix));
        return point;
    }
}
