package xyz.n7mn.dev.survivalsystem.event.eventsystem;

public class EventManager {

    //TODO: ADD EVENT SYSTEM!

    public interface Event {
        void onEvent();
    }

    public enum EventEnum {
        BREAK,
        PLACE,
    }
}