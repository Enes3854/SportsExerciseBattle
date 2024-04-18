import com.seb.dao.PushupRecordDao;
import com.seb.model.PushupRecord;
import com.seb.model.Tournament;
import com.seb.service.PushupService;
import com.seb.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PushupServiceTest {
    private PushupService pushupService;
    private PushupRecordDao pushupRecordDaoMock;
    private TournamentService tournamentServiceMock;

    @BeforeEach
    void setUp() {
        pushupRecordDaoMock = Mockito.mock(PushupRecordDao.class);
        tournamentServiceMock = Mockito.mock(TournamentService.class);
        pushupService = new PushupService(pushupRecordDaoMock, tournamentServiceMock);
    }

    @Test
    void addPushupRecord_success() {
        Tournament activeTournament = new Tournament("t1", LocalDateTime.now(), LocalDateTime.now().plusMinutes(2), "Active", null);
        Mockito.when(tournamentServiceMock.findActiveTournament()).thenReturn(activeTournament);
        Mockito.when(pushupRecordDaoMock.addRecord(Mockito.any(PushupRecord.class))).thenReturn(true);

        boolean result = pushupService.addPushupRecord(50, 1L, 120, LocalDateTime.now());

        assertTrue(result);
    }

}
