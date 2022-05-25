package BP_JDBC.src.main.java.observer;

public interface Publisher {
    void addSubscriber(Subscriber sub);
    void removeSubscriber(Subscriber sub);
    void notifySubscribers(Notification notification);
}
