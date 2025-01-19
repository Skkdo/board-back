package board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.kjh.boardback.dto.response.search.GetPopularListResponseDto;
import com.kjh.boardback.dto.response.search.GetRelationListResponseDto;
import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.repository.SearchLogRepository;
import com.kjh.boardback.service.SearchService;
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
public class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private SearchLogRepository searchLogRepository;

    @Test
    @DisplayName(value = "연관 검색어 조회 성공")
    void getRelationList() {
        String searchWord = "searchWord";
        List<String> relationList = List.of();

        doReturn(relationList).when(searchLogRepository).getRelationList(searchWord);

        GetRelationListResponseDto responseDto = searchService.getRelationList(searchWord);

        assertThat(responseDto.getRelativeWordList()).isEqualTo(relationList);
        verify(searchLogRepository,times(1)).getRelationList(searchWord);
    }

    @Test
    @DisplayName(value = "인기 검색어 조회 성공")
    void getPopularList() {
        List<SearchLog> searchLogList = List.of();

        doReturn(searchLogList).when(searchLogRepository).getPopularList();

        GetPopularListResponseDto responseDto = searchService.getPopularList();

        assertThat(responseDto.getPopularWordList()).isEqualTo(searchLogList);
        verify(searchLogRepository,times(1)).getPopularList();
    }
}
