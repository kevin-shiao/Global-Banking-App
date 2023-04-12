package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

public class EventTest {
    private Event event;
    private Date date;

    @BeforeEach
    public void runBefore() {
        event = new Event("User added to Bank");
        date = Calendar.getInstance().getTime();
    }

    @Test
    public void testEvent() {
        assertEquals("User added to Bank", event.getDescription());
    }

    @Test
    public void testToString() {
        assertEquals(date.toString() + "\n" + "User added to Bank", event.toString());
    }

}
