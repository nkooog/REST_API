package client.restapi.event;


import junitparams.Parameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.Parameterized;
import org.springframework.util.Assert;

class EventTest {


    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Spring REST API")
                .description("REST API development with Spring")
                .build();
        Assertions.assertNotNull(event);
    }

    @Test
    public void javaBean() {
        Event event = new Event();
        event.setName("Event");
        event.setDescription("REST API");

        String value = "Event";
        Assertions.assertEquals(event.getName(), value);
    }

    private Object[] paramsForTestFree() {
        return new Object[][] {
            new Object[] {0, 0, true}
          , new Object[] {100, 0, false}
          , new Object[] {0, 100, false}
          , new Object[] {100, 100, false}
        };
    }

    @ParameterizedTest
    @CsvSource({
                "0, 0, true"
            ,   "100, 0, false"
            ,   "0, 100, false"
    })
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // When
        event.update();

        // Then
        Assertions.assertEquals(event.isFree(), isFree);
    }

    @ParameterizedTest
    @CsvSource({
            "강남역 네이버 D2 스타텁 팩토리, true"
            ,   ", false"
    })
    public void testOffline() {
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();
        // When
        event.update();

        // Then
        Assertions.assertTrue(event.isOffline());

        // Given
        event = Event.builder()
                .build();
        // When
        event.update();

        // Then
        Assertions.assertFalse(event.isOffline());
    }

}