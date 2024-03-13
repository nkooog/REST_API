package client.restapi.event;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

}