package BP_JDBC.src.main.java.observer.implementation;

import BP_JDBC.src.main.java.observer.Notification;
import BP_JDBC.src.main.java.observer.Publisher;
import BP_JDBC.src.main.java.observer.Subscriber;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

public class PublisherImplementation implements Publisher {


    private List<Subscriber> subscribers;


    public void addSubscriber(Subscriber sub) {
        if(sub == null)
            return;
        if(this.subscribers ==null)
            this.subscribers = new ArrayList<>();
        if(this.subscribers.contains(sub))
            return;
        this.subscribers.add(sub);
    }

    public void removeSubscriber(Subscriber sub) {
        if(sub == null || this.subscribers == null || !this.subscribers.contains(sub))
            return;
        this.subscribers.remove(sub);
    }

    public void notifySubscribers(Notification notification) {
        if(notification == null || this.subscribers == null || this.subscribers.isEmpty())
            return;

        for(Subscriber listener : subscribers){
            listener.update(notification);
        }
    }
}
