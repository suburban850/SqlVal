package BP_JDBC.src.main.java.database;


import BP_JDBC.src.main.java.database.settings.Settings;
import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.data.Row;
import BP_JDBC.src.main.java.resource.enums.AttributeType;
import BP_JDBC.src.main.java.resource.implementation.Attribute;
import BP_JDBC.src.main.java.resource.implementation.Entity;
import BP_JDBC.src.main.java.resource.implementation.InformationResource;
import lombok.Data;


import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
public class MYSQLrepository implements Repository{

    private Settings settings;
    private Connection connection;

    public MYSQLrepository(Settings settings) {
        this.settings = settings;
    }

    private void initConnection() throws SQLException, ClassNotFoundException{
        String ip = (String) settings.getParameter("mysql_ip");
        String database = (String) settings.getParameter("mysql_database");
        String username = (String) settings.getParameter("mysql_username");
        String password = (String) settings.getParameter("mysql_password");
        //Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+database,username,password);


    }

    private void closeConnection(){
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


    @Override
    public DBNode getSchema() {

        try{
            this.initConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            InformationResource ir = new InformationResource("RAF_BP_Primer");

            String tableType[] = {"TABLE"};
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, tableType);

            while (tables.next()){

                String tableName = tables.getString("TABLE_NAME");
                if(tableName.contains("trace"))continue;
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


                    Attribute attribute = new Attribute(columnName, newTable,
                            AttributeType.valueOf(
                                    Arrays.stream(columnType.toUpperCase().split(" "))
                                    .collect(Collectors.joining("_"))),
                            columnSize);
                    newTable.addChild(attribute);

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
}
