package client.restapi.event;

import client.restapi.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("EventController tdd")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception{
        EventDTO event = EventDTO.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 03, 13, 22, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 03, 14, 22, 00))
                .beginEventDateTime(LocalDateTime.of(2024, 03, 15, 22, 00))
                .endEventDateTime(LocalDateTime.of(2024, 03, 16, 22, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("인천 미추홀구")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문에 json을 담아 보냄
                        .accept(MediaTypes.HAL_JSON) // 응답받기 원한 타입
                        .content(objectMapper.writeValueAsString(event)) // 요청 본문은 json으로 변환하여 보내도록함
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception{
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 03, 13, 22, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 03, 14, 22, 00))
                .beginEventDateTime(LocalDateTime.of(2024, 03, 15, 22, 00))
                .endEventDateTime(LocalDateTime.of(2024, 03, 16, 22, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("인천 미추홀구")
                .free(true)
                .offline(false)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON) // 요청 본문에 json을 담아 보냄
                        .accept(MediaTypes.HAL_JSON) // 응답받기 원한 타입
                        .content(objectMapper.writeValueAsString(event)) // 요청 본문은 json으로 변환하여 보내도록함
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvnet_Bad_Request_Empty_Input() throws Exception{

        EventDTO eventDTO = EventDTO.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO))
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvnet_Bad_Request_Wrong_Input() throws Exception{

        EventDTO eventDTO = EventDTO.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 03, 16, 22, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 03, 15, 22, 00))
                .beginEventDateTime(LocalDateTime.of(2024, 03, 14, 22, 00))
                .endEventDateTime(LocalDateTime.of(2024, 03, 13, 22, 00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("인천 미추홀구")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDTO))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
gi
}
