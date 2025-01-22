package board.global.security;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kjh.boardback.BoardBackApplication;
import com.kjh.boardback.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = BoardBackApplication.class)
@AutoConfigureMockMvc
public class AuthenticationPrincipalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockCustomUser(email = "test@test.com")
    @DisplayName("@AuthenticationPrincipal 동작 테스트")
    void authenticationPrincipal() throws Exception {

        doReturn(null).when(userService).getSignInUser("test@test.com");

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/user")
                )
                .andExpect(status().isOk());
    }
}
