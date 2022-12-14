package app;


import checker.Checker;
import database.Database;
import database.DatabaseImplementation;
import database.MYSQLrepository;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import gui.MainFrame;
import gui.table.TableModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import resource.DBNode;
import resource.data.Row;
import resource.implementation.Entity;
import resource.implementation.InformationResource;
import tree.Tree;
import tree.TreeItem;
import tree.implementation.TreeImplementation;
import utils.Constants;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.stream.events.EndElement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
//
@Getter
@Setter
public class AppCore extends PublisherImplementation{

    private Database database;
    private Settings settings;
    private TableModel tableModel;
    private DefaultTreeModel defaultTreeModel;
    private Tree tree;
    //brisanje mozda?
    private  TreeItem<DBNode> root;
    private Checker checker;
    public AppCore() {
        this.settings = initSettings();
        this.database = new DatabaseImplementation(new MYSQLrepository(this.settings));
        this.tableModel = new TableModel();
        this.tree = new TreeImplementation();
        this.checker = new Checker();

    }

    public void run(String sql, InformationResource ir)
    {
        checker.check(sql,ir);
        List<String> error_mesages = new ArrayList<>();
        while(!checker.getStack().isEmpty())
        {
            String s = checker.getStack().pop();
            System.out.println("SSS: " +s);
            if(!s.equals("true"))
            {
                error_mesages.add(s);
            }
        }
        if(error_mesages.isEmpty()){
            whyread(sql);
        }else{
            Notification notification = new Notification(NotificationCode.ERROR_MSG, error_mesages);
            notifySubscribers(notification);
        }
    }




    public void bulk(List<String[]> rows, Entity entity)
    {
        this.database.bulkImport(rows,entity);
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

    public DBNode getInformationResource(){
        InformationResource ir = (InformationResource) this.database.loadResource();
        return ir;
    }

    public void readDataFromTable(String fromTable){

        tableModel.setRows(this.database.readDataFromTable(fromTable));

        //Zasto ova linija moze da ostane zakomentarisana?
        //this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }
    public void whyread(String sql)
    {
        tableModel.setRows(this.database.checkDatabase(sql));
    }

}
