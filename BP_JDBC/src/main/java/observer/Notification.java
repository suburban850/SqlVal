package BP_JDBC.src.main.java.observer;

import BP_JDBC.src.main.java.observer.enums.NotificationCode;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Notification {
    private NotificationCode code;
    private Object data;
}
