package resource.implementation;

import gui.MainFrame;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import observer.Notification;
import observer.Publisher;
import observer.Subscriber;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import resource.DBNode;
import resource.DBNodeComposite;
import tree.TreeItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString(callSuper = true)
//workspace
public class InformationResource extends DBNodeComposite implements Publisher {


    public InformationResource(String name) {
        super(name, null);
        this.addSubscriber(MainFrame.getInstance());
    }

    @Override
    public void addChild(DBNode child) {
        if(child!=null) {
            if (child != null && child instanceof Entity) {
                if(!this.getChildren().contains(child)) {
                    Entity entity = (Entity) child;
                    this.getChildren().add(entity);

                }
            }
        }
    }
    public void importChild(DBNode child)
    {
        if(child!=null) {
            if (child != null && child instanceof Entity) {
                if(!this.getChildren().contains(child)) {
                    Entity entity = (Entity) child;
                    this.addChild(entity);
                    //<DBNode> node = new TreeItem<DBNode>(entity);
                    System.out.println("3");
                    Notification notification = new Notification(NotificationCode.CREATE_NODE,entity);
                    notifySubscribers(notification);
                }
            }
        }
    }
}
