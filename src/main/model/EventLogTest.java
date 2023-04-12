package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventLogTest {
    private Event e1;
    private Event e2;
    private Event e3;
    private Bank bank;
    private BankUser user;

    @BeforeEach
    public void loadEvents() {
        e1 = new Event("A1");
        e2 = new Event("A1");
        e3 = new Event("User added to bank");
        EventLog el = EventLog.getInstance();
        el.logEvent(e1);
        el.logEvent(e2);
        el.logEvent(e3);
        bank = new Bank("Test Bank");
        user = new BankUser(1, "Test User");
    }

    @Test
    public void testLogEvent() {
        List<Event> l = new ArrayList<>();

        EventLog el = EventLog.getInstance();
        for (Event event : el) {
            l.add(event);
        }

        assertTrue(l.contains(e1));
        assertTrue(l.contains(e2));
        assertTrue(l.contains(e3));
    }

    @Test
    public void testClear() {
        EventLog el = EventLog.getInstance();
        el.clear();
        Iterator<Event> iterator = el.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("Event log cleared.", iterator.next().getDescription());
        assertFalse(iterator.hasNext());
    }

}

