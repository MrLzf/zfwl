package cn.iocoder.yudao.module.tutor.service.history;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.tutor.dal.mysql.history.TutorBrowseHistoryMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

class TutorBrowseHistoryServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private TutorBrowseHistoryServiceImpl browseHistoryService;

    @Mock
    private TutorBrowseHistoryMapper browseHistoryMapper;

    @Test
    void recordBrowseHistory_deletesPreviousTargetBeforeInsert() {
        browseHistoryService.recordBrowseHistory(100L, "demand", 11L, 200L, "title", "440100", "广州");

        verify(browseHistoryMapper).deleteByUserIdAndTarget(100L, "demand", 11L);
    }

    @Test
    void clearMyBrowseHistory_deletesCurrentUserRecords() {
        browseHistoryService.clearMyBrowseHistory(100L);

        verify(browseHistoryMapper).deleteByUserId(100L);
    }

    @Test
    void deleteMyBrowseHistory_deletesOnlyOwnedRecord() {
        browseHistoryService.deleteMyBrowseHistory(100L, 22L);

        verify(browseHistoryMapper).deleteByIdAndUserId(22L, 100L);
    }
}
