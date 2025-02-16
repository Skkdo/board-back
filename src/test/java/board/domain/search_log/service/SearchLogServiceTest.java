package board.domain.search_log.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.domain.search_log.dto.response.GetPopularListResponseDto;
import com.kjh.boardback.domain.search_log.dto.response.GetRelationListResponseDto;
import com.kjh.boardback.domain.search_log.entity.SearchLog;
import com.kjh.boardback.domain.search_log.repository.SearchLogRepository;
import com.kjh.boardback.domain.search_log.service.SearchLogService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class SearchLogServiceTest {

    @InjectMocks
    private SearchLogService searchLogService;

    @Mock
    private SearchLogRepository searchLogRepository;

    @Test
    @DisplayName(value = "연관 검색어 조회 성공")
    void getRelationList() {
        String searchWord = "searchWord";
        List<String> relationList = List.of();

        doReturn(relationList).when(searchLogRepository).getRelationList(searchWord);

        GetRelationListResponseDto responseDto = searchLogService.getRelationList(searchWord);

        assertThat(responseDto.getRelativeWordList()).isEqualTo(relationList);
        verify(searchLogRepository,times(1)).getRelationList(searchWord);
    }

    @Test
    @DisplayName(value = "인기 검색어 조회 성공")
    void getPopularList() {
        List<SearchLog> searchLogList = List.of();

        doReturn(searchLogList).when(searchLogRepository).getPopularList();

        GetPopularListResponseDto responseDto = searchLogService.getPopularList();

        assertThat(responseDto.getPopularWordList()).isEqualTo(searchLogList);
        verify(searchLogRepository,times(1)).getPopularList();
    }
}
