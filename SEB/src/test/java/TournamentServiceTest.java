import com.seb.dao.TournamentDao;
import com.seb.model.Tournament;
import com.seb.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TournamentServiceTest {
    private TournamentService tournamentService;
    private TournamentDao tournamentDaoMock;

    @BeforeEach
    void setUp() {
        tournamentDaoMock = Mockito.mock(TournamentDao.class);
        tournamentService = new TournamentService(tournamentDaoMock);
    }

    @Test
    void createTournament_success() {
        Tournament tournament = new Tournament("t1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(2), "Active", null);
        Mockito.when(tournamentDaoMock.createTournament(Mockito.any(Tournament.class))).thenReturn(true);

        boolean result = tournamentService.createTournament(tournament);

        assertTrue(result);
    }

    @Test
    void findActiveTournament_found() {
        Tournament activeTournament = new Tournament("t1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(2), "Active", null);
        Mockito.when(tournamentDaoMock.findActiveTournament()).thenReturn(activeTournament);

        Tournament result = tournamentService.findActiveTournament();

        assertNotNull(result);
        assertEquals("Active", result.getStatus());
    }

    @Test
    void updateTournament_statusChange() {
        Tournament tournament = new Tournament("t1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(2), "Active", null);
        Mockito.when(tournamentDaoMock.updateTournament(Mockito.any(Tournament.class))).thenReturn(true);

        boolean result = tournamentService.updateTournament(tournament.getId(), tournament.getStartTime(), tournament.getEndTime(), "Completed", null);

        assertTrue(result);
    }
}
