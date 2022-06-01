package app;


import database.Database;
import database.DatabaseImplementation;
import database.MYSQLrepository;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import gui.table.TableModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import resource.implementation.InformationResource;
import tree.Tree;
import tree.implementation.TreeImplementation;
import utils.Constants;

import javax.swing.tree.DefaultTreeModel;
import java.sql.Connection;

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

    public void pulltrig()
    {
        Notification notification = new Notification(NotificationCode.RUN,1);
        this.notifySubscribers(notification);
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

    public void smt(String query)
    {
        if(database instanceof DatabaseImplementation)
        {
            DatabaseImplementation di = (DatabaseImplementation) database;
            di.check(query);
        }
    }



}
