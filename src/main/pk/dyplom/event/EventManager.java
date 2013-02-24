package pk.dyplom.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventManager {

    private final Map<Class, List<EventHandler>> handlers = new HashMap<Class, List<EventHandler>>();

    public EventHandler addHandler(Class eventType, EventHandler handler) {
        List<EventHandler> eventHandlers = handlers.get(eventType);
        if (eventHandlers == null) {
            eventHandlers = new LinkedList<EventHandler>();
            handlers.put(eventType, eventHandlers);
        }
        eventHandlers.add(handler);
        return handler;
    }

    public void triggerEvent(Object event) {
        List<EventHandler> eventHandlers = handlers.get(event.getClass());
        if  (eventHandlers == null) {
            return;
        }
        for (EventHandler h : eventHandlers) {
            h.handle(event);
        }
    }

    public void removeHandlers(Class eventType) {
        handlers.remove(eventType);
    }
}
