package BP_JDBC.src.main.java.app;

import BP_JDBC.src.main.java.database.Database;
import BP_JDBC.src.main.java.database.DatabaseImplementation;
import BP_JDBC.src.main.java.database.MYSQLrepository;
import BP_JDBC.src.main.java.database.settings.Settings;
import BP_JDBC.src.main.java.database.settings.SettingsImplementation;
import BP_JDBC.src.main.java.gui.table.TableModel;
import BP_JDBC.src.main.java.observer.Notification;
import BP_JDBC.src.main.java.observer.enums.NotificationCode;
import BP_JDBC.src.main.java.observer.implementation.PublisherImplementation;

import BP_JDBC.src.main.java.resource.implementation.InformationResource;
import BP_JDBC.src.main.java.tree.Tree;
import BP_JDBC.src.main.java.tree.implementation.TreeImplementation;
import BP_JDBC.src.main.java.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.swing.tree.DefaultTreeModel;

@Getter
@Setter
public class AppCore extends PublisherImplementation {

    private Database database;
    private Settings settings;
    private TableModel tableModel;
    private DefaultTreeModel defaultTreeModel;
    private Tree tree;

    public AppCore() {
        this.settings = initSettings();
        this.database = new DatabaseImplementation(new MYSQLrepository(this.settings));
        this.tableModel = new TableModel();
        this.tree = new TreeImplementation();

    }

    private Settings initSettings() {
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mysql_ip", Constants.MYSQL_IP);
        settingsImplementation.addParameter("mysql_database", Constants.MYSQL_DATABASE);
        settingsImplementation.addParameter("mysql_username", Constants.MYSQL_USERNAME);
        settingsImplementation.addParameter("mysql_password", Constants.MYSQL_PASSWORD);
        return settingsImplementation;
    }


    public DefaultTreeModel loadResource(){
        InformationResource ir = (InformationResource) this.database.loadResource();
        return this.tree.generateTree(ir);
    }

    public void readDataFromTable(String fromTable){

        tableModel.setRows(this.database.readDataFromTable(fromTable));

        //Zasto ova linija moze da ostane zakomentarisana?
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }




}
