package database;


import database.settings.Settings;
import gui.MainFrame;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import resource.DBNode;
import resource.data.Row;
import resource.enums.AttributeType;
import resource.implementation.Attribute;
import resource.implementation.Entity;
import resource.implementation.InformationResource;
import utils.DBReader;


import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@Data
public class MYSQLrepository implements Repository{

    private Settings settings;
    private Connection connection;//!!!

    public MYSQLrepository(Settings settings) {
        this.settings = settings;
    }

    public void initConnection() throws SQLException, ClassNotFoundException{
        String ip = (String) settings.getParameter("mysql_ip");
        String database = (String) settings.getParameter("mysql_database");
        String username = (String) settings.getParameter("mysql_username");
        String password = (String) settings.getParameter("mysql_password");
        //Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+database,username,password);
    }

    public void closeConnection(){
        try{
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
    }

    //struktura i stab;p

    @Override
    public DBNode getSchema() {

        try{
            this.initConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            InformationResource ir = new InformationResource("tim68");

            String tableType[] = {"TABLE"};
            //sumiranje
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, tableType);//samo tabele
            //uzima sve tabele iz baze
            while (tables.next()){
                //nazivi redova
                String tableName = tables.getString("TABLE_NAME");
                if(tableName.contains("trace"))continue;
                //dodavanje tabele u korenski cvor
                Entity newTable = new Entity(tableName, ir);
                ir.addChild(newTable);

                //Koje atribute imaja ova tabela?

                ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, null);

                while (columns.next()){

                    // COLUMN_NAME TYPE_NAME COLUMN_SIZE ....

                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");

                    System.out.println(columnType);

                    int columnSize = Integer.parseInt(columns.getString("COLUMN_SIZE"));

//                    ResultSet pkeys = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
//
//                    while (pkeys.next()){
//                        String pkColumnName = pkeys.getString("COLUMN_NAME");
//                    }

                    //nazivi kolona i formatiranje
                    Attribute attribute = new Attribute(columnName, newTable,
                            AttributeType.valueOf(
                                    Arrays.stream(columnType.toUpperCase().split(" "))
                                    .collect(Collectors.joining("_"))),
                            columnSize);
                    newTable.addChild(attribute);//dodavanje naziva kolone

                }



            }


            //TODO Ogranicenja nad kolonama? Relacije?


            return ir;
            //String isNullable = columns.getString("IS_NULLABLE");
            // ResultSet foreignKeys = metaData.getImportedKeys(connection.getCatalog(), null, table.getName());
            // ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), null, table.getName());

        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        catch (ClassNotFoundException e2){ e2.printStackTrace();}
        finally {
            this.closeConnection();
        }

        return null;
    }

    @Override
    public List<Row> get(String from) {

        List<Row> rows = new ArrayList<>();


        try{
            this.initConnection();

            String query = "SELECT * FROM " + from;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            //cursor se pomera po redovima
            while (rs.next()){

                Row row = new Row();
                row.setName(from);

                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
           this.closeConnection();
        }

        return rows;
    }


    public void write(String name)
    {
        try {
            initConnection();
            String query = "select * from bp_tim68."+name;
            Statement preparedStatement = this.connection.createStatement();
            ResultSet rs = preparedStatement.executeQuery(query);
            if(rs == null) return;
            DBReader dbReader = new DBReader();
            dbReader.csvWriteAll(rs,name);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            closeConnection();
        }

    }

    public void zabrisanje(String query)
    {
        try {
            this.initConnection();
            Statement pp  = this.connection.createStatement();

            if(this.connection == null)
            {
                System.out.println("konekcija pale");
                return;
            }
            ResultSet rs = pp.executeQuery(query);
            if(rs != null){
                JOptionPane.showMessageDialog(MainFrame.getInstance(),"correct ", "Bulk import",JOptionPane.WARNING_MESSAGE);
                System.out.println("query je tacan");
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            JOptionPane.showMessageDialog(MainFrame.getInstance(),"Error: " + throwables.getMessage(), "Bulk import",JOptionPane.WARNING_MESSAGE);
            System.out.println("query je netacan");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            this.closeConnection();
        }
    }


}
