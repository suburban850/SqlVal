package observer;

import java.io.IOException;

public interface Subscriber {
    void update(Notification notification) throws IOException;
}
