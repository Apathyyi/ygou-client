package sy.bishe.ygou.web;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class EventManager {
    //存储
    private static final HashMap<String,Event> EVENT_HASH_MAP = new HashMap<>();

    public EventManager() {
    }
    private static class Holder{
        private static final EventManager INSTANCE = new EventManager();
    }
    public static EventManager getInstance(){
        return Holder.INSTANCE;
    }
    public EventManager addEvrnt(@NonNull String name,@NonNull Event event){
        EVENT_HASH_MAP.put(name,event);
        return this;
    }
    //创建
    public Event createEvent(String action){
        final Event event = EVENT_HASH_MAP.get(action);
        if (event == null){
            return new UndefinedEvent();
        }
        return event;
    }
}
