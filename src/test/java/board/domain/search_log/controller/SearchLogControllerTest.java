package board.domain.search_log.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.domain.search_log.controller.SearchLogController;
import com.kjh.boardback.domain.search_log.dto.response.GetPopularListResponseDto;
import com.kjh.boardback.domain.search_log.dto.response.GetRelationListResponseDto;
import com.kjh.boardback.domain.search_log.entity.SearchLog;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.global.exception.GlobalExceptionHandler;
import com.kjh.boardback.domain.search_log.service.SearchLogService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class SearchLogControllerTest {

    @InjectMocks
    private SearchLogController searchLogController;

    @Mock
    private SearchLogService searchLogService;

    private MockMvc mockMvc;

    private final String commonUrl = "/api/v1/search";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void build() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(searchLogController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    @DisplayName(value = "인기 검색어 리스트 조회 성공")
    void getPopularList() throws Exception {
        String url = commonUrl + "/popular-list";
        List<SearchLog> searchLogList = List.of();
        GetPopularListResponseDto getPopularListResponseDto = new GetPopularListResponseDto(searchLogList);
        ResponseDto responseDto = ResponseDto.success(getPopularListResponseDto);

        doReturn(getPopularListResponseDto).when(searchLogService).getPopularList();

        MvcResult mvcResult = perform(url, null, HttpStatus.OK.value(), responseDto);

        String content = mvcResult.getResponse().getContentAsString();
        JsonNode dataNode = objectMapper.readTree(content).path("data");

        if(!dataNode.isNull()){
            GetPopularListResponseDto extractedData = objectMapper.convertValue(dataNode,
                    GetPopularListResponseDto.class);
            assertThat(extractedData.getPopularWordList()).isEqualTo(searchLogList);
        }
    }

    @Test
    @DisplayName(value = "연관 검색어 리스트 조회 성공")
    void getRelationList() throws Exception {
        String url = commonUrl + "/test/relation-list";
        String pathVariable = "test";

        List<String> relationWordList = List.of();
        GetRelationListResponseDto getRelationListResponseDto = new GetRelationListResponseDto(relationWordList);
        ResponseDto responseDto = ResponseDto.success(getRelationListResponseDto);

        doReturn(getRelationListResponseDto).when(searchLogService).getRelationList(pathVariable);

        perform(url,pathVariable,HttpStatus.OK.value(),responseDto);
    }

    MvcResult perform(String url, String pathVariable, int status, ResponseDto responseDto) throws Exception {
        return mockMvc.perform(
                        MockMvcRequestBuilders.get(url, pathVariable)
                )
                .andExpect(status().is(status))
                .andExpect(jsonPath("$.code").value(responseDto.getCode()))
                .andExpect(jsonPath("$.message").value(responseDto.getMessage()))
                .andReturn();
    }
}
