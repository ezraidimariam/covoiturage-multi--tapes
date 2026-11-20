
package ma.youcode.covoiturage;

import ma.youcode.covoiturage.dto.ReservationDTO;
import ma.youcode.covoiturage.entity.*;
import ma.youcode.covoiturage.mapper.ReservationMapper;
import ma.youcode.covoiturage.repository.*;
import ma.youcode.covoiturage.service.ReservationServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PassagerRepository passagerRepository;

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private PointArretRepository pointArretRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void creerReservationAvecSucces() {

        Passager passager = mock(Passager.class);
        when(passager.getId()).thenReturn(1L);

        Trajet trajet = mock(Trajet.class);
        when(trajet.getId()).thenReturn(10L);
        when(trajet.getStatut()).thenReturn(StatutTrajet.PUBLIE);
        when(trajet.getPlacesProposees()).thenReturn(4);

        PointArret montee = mock(PointArret.class);
        when(montee.getTrajet()).thenReturn(trajet);
        when(montee.getOrdre()).thenReturn(0);
        when(montee.getPrixDepuisDepart())
                .thenReturn(new BigDecimal("0.00"));

        PointArret descente = mock(PointArret.class);
        when(descente.getTrajet()).thenReturn(trajet);
        when(descente.getOrdre()).thenReturn(1);
        when(descente.getPrixDepuisDepart())
                .thenReturn(new BigDecimal("100.00"));

        ReservationDTO dto = new ReservationDTO(
                null, 2, null, null, null,
                1L, 10L, 100L, 101L
        );

        when(passagerRepository.findById(1L))
                .thenReturn(Optional.of(passager));

        when(trajetRepository.findById(10L))
                .thenReturn(Optional.of(trajet));

        when(pointArretRepository.findById(100L))
                .thenReturn(Optional.of(montee));

        when(pointArretRepository.findById(101L))
                .thenReturn(Optional.of(descente));

        when(reservationRepository.existsByPassager_IdAndTrajet_IdAndStatut(
                1L, 10L, StatutReservation.CONFIRMEE
        )).thenReturn(false);

        when(reservationRepository.compterPlacesReservees(
                10L, StatutReservation.CONFIRMEE
        )).thenReturn(0L, 2L);

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationDTO resultat = new ReservationDTO(
                null, 2, new BigDecimal("200.00"), null,
                StatutReservation.CONFIRMEE,
                1L, 10L, 100L, 101L
        );

        when(reservationMapper.toDTO(any(Reservation.class)))
                .thenReturn(resultat);

        reservationService.creer(dto);

        ArgumentCaptor<Reservation> captor =
                ArgumentCaptor.forClass(Reservation.class);

        verify(reservationRepository).save(captor.capture());

        Reservation reservationSauvegardee = captor.getValue();

        assertThat(reservationSauvegardee.getMontantTotal())
                .isEqualByComparingTo("200.00");

        assertThat(reservationSauvegardee.getNombrePlaces())
                .isEqualTo(2);

        assertThat(reservationSauvegardee.getStatut())
                .isEqualTo(StatutReservation.CONFIRMEE);
    }

    @Test
    void refuserReservationSiPlacesInsuffisantes() {

        Passager passager = mock(Passager.class);
        when(passager.getId()).thenReturn(1L);

        Trajet trajet = mock(Trajet.class);
        when(trajet.getId()).thenReturn(10L);
        when(trajet.getStatut()).thenReturn(StatutTrajet.PUBLIE);
        when(trajet.getPlacesProposees()).thenReturn(4);

        PointArret montee = mock(PointArret.class);
        when(montee.getTrajet()).thenReturn(trajet);
        when(montee.getOrdre()).thenReturn(0);

        PointArret descente = mock(PointArret.class);
        when(descente.getTrajet()).thenReturn(trajet);
        when(descente.getOrdre()).thenReturn(1);

        ReservationDTO dto = new ReservationDTO(
                null, 2, null, null, null,
                1L, 10L, 100L, 101L
        );

        when(passagerRepository.findById(1L))
                .thenReturn(Optional.of(passager));

        when(trajetRepository.findById(10L))
                .thenReturn(Optional.of(trajet));

        when(pointArretRepository.findById(100L))
                .thenReturn(Optional.of(montee));

        when(pointArretRepository.findById(101L))
                .thenReturn(Optional.of(descente));

        when(reservationRepository.existsByPassager_IdAndTrajet_IdAndStatut(
                1L, 10L, StatutReservation.CONFIRMEE
        )).thenReturn(false);

        when(reservationRepository.compterPlacesReservees(
                10L, StatutReservation.CONFIRMEE
        )).thenReturn(3L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> reservationService.creer(dto)
        );

        assertThat(exception.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        assertThat(exception.getReason())
                .isEqualTo("Nombre de places insuffisant");

        verify(reservationRepository, never()).save(any(Reservation.class));
    }
