package observer.implementation;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import observer.Notification;
import observer.Publisher;
import observer.Subscriber;


import java.util.ArrayList;
import java.util.List;
@Getter

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

    @SneakyThrows
    public void notifySubscribers(Notification notification) {
        if(notification == null || this.subscribers == null || this.subscribers.isEmpty())
        {
            this.subscribers.isEmpty();
            return;
        }

        for(Subscriber listener : subscribers){

            System.out.println("update");
            listener.update(notification);
        }
    }
}
