package database;


import com.mysql.cj.x.protobuf.MysqlxDatatypes;
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
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@Data
public class MYSQLrepository implements Repository{

    private Settings settings;
    private Connection connection;//!!!

    private InformationResource korentemp=null;

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
                //add new atribute?
                //Koje atribute imaja ova tabela?
                //
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
                    //ime kolone, objekat konkretnog podatka
                    //rs.getString(columnIndex:1)
                    //rs.getString(employeeId)
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

    private PreparedStatement buildPreparedStatement(List<String> columnNames,String tableName ) throws SQLException {
//        columnNames.addAll(cheat(tableName));

        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append("insert into "+ tableName +" ");
        List<String> types = getTypes(tableName);
        StringBuilder sb1 = new StringBuilder();
        sb1.append(" (");
        //PreparedStatement preparedStatement = connection.prepareStatement(sql);


        for(int i = 0; i<columnNames.size();i++) {
            sb1.append(columnNames.get(i) + ", ");
        }
        sb1.append(")");
        sb1.toString().trim();
        sb1.deleteCharAt(sb1.length()-3);
        sb.append(sb1.toString());
        sb.append(" values ");

        StringBuilder sb2 = new StringBuilder();
        sb2.append("(");

        for (int i = 0; i < columnNames.size(); i++) {
            sb2.append("?, ");
        }

        sb2.append(")");
        sb2.deleteCharAt(sb2.length()-3);
        sb.append(sb2.toString());
        String sql = sb.toString();
        System.out.println(sql);

        PreparedStatement ps = connection.prepareStatement(sql);

        return ps;
    }


    @Override
    public void bulkrep(List<String[]> rows,Entity entity) {
        try {
            this.initConnection();

            String[] tableType = {"TABLE"};
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet tables = meta.getTables(connection.getCatalog(), null, null, tableType);

            List<String> columnNames = getColumnNames(entity.getName());
            //prolazi kroz tabele u dok se ne nadje selektovana tabela
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                List<String> types = new ArrayList<>();
                //tipovi kolona
                types.addAll(getTypes(tableName));
                if (tableName.contains("trace")) continue;
                if (tableName.equals(entity.getName())) {

                    ResultSet columns = meta.getColumns(connection.getCatalog(), null, tableName, null);
                    int i = 1, k = 0;
                    //prolazi kroz tabele
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        String columnType = columns.getString("TYPE_NAME");
                        int columnSize = Integer.parseInt(columns.getString("COLUMN_SIZE"));
                        int cnt = 0, question = 1;
                        i = 1;
                        //prolazi kroz csv listu, pravi atribute i zove buildPreparedStatement
                        while (cnt < rows.get(0).length && k < rows.get(0).length && i < rows.size()) {
                            String cell = rows.get(i)[k];// i=1 k=0 i=2 k=0 i=3 k=0 ||

                            PreparedStatement ps = null;
                            Attribute attribute = new Attribute(cell, entity, AttributeType.valueOf(Arrays.stream(columnType.toUpperCase().split(" ")).collect(Collectors.joining("_"))), columnSize);
                            //vraca prepared statement sa values(?,?,?)
                            ps = buildPreparedStatement( columnNames, tableName);
                            for (int j = 1; j < rows.size(); j++) {//po redovima u csvu
                                question = 1;
                                int que = 0;
                                String s[] = rows.get(j);
                                for (int w = 0; w < s.length; w++) //po kolonama u csvu
                                {
                                    String sk = s[w];
                                    if (sk == null) break;
                                    if (que < types.size()) {
                                        if (que == types.size() - 1) que = 0;

                                        String stri = types.get(que);//tip podatka u koloni
                                        AttributeType a = AttributeType.valueOf(Arrays.stream((stri.toUpperCase().split(" "))).collect(Collectors.joining("_")));
                                        addAt(question, sk, a, ps);//dodavanje ps.setString...
                                        System.out.println(question + " " + sk + " " + a);
                                        question++;
                                        ++que;
                                    }

                                }
                                //dodavanje batcha i execute
                                ps.addBatch();
                                if (ps.execute()) System.out.println("true");
                                else System.out.println("false");
                            }
                            //dodavanje deteta modelu
                            entity.addChild(attribute);
                        }
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            System.out.println("connectionclossed");
            this.closeConnection();
        }
    }
    private ArrayList<Row> rsss= new ArrayList<>();

    @Override
    public List<Row> check(String sql) {
        String temp = sql.trim();
        String[] spliff = sql.split(" ");
        String name = "";
        int index= 0;
       for(int i = 0; i<spliff.length;i++)//select * from hr.employees
       {
           if(spliff[i].equals("from"))
           {
                  index = i+1;
           }
       }
        String split ="";
       if(index!=spliff.length-1) {
           split = spliff[index].split("\\.")[1];
       }
        System.out.println("NAME: " + name);
        List<Row> rows = new ArrayList<>();
        try {
            this.initConnection();
            String query = sql;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            //cursor se pomera po redovima
            while (rs.next()) {

                Row row = new Row();
                row.setName(split);

                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    //ime kolone, objekat konkretnog podatka
                    //rs.getString(columnIndex:1)
                    //rs.getString(employeeId)
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);
            }
            return rows;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return rows;
    }

    private List<String> getTypes( String tableName) {
        List<String> columnTypes = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String name = rsmd.getColumnTypeName(i);
                columnTypes.add(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("COLUMN TYPES");
        for(String s: columnTypes)
        {
            System.out.print(s + "  ");
        }
        return columnTypes;
    }

    //
    private void addAt(int q,String s,AttributeType at,PreparedStatement ps) {
        try {
            AttributeType type = AttributeType.valueOf(Arrays.stream(at.toString().toUpperCase().split(" ")).collect(Collectors.joining("_")));

            s = s.trim();
            if(type.equals(AttributeType.INT_UNSIGNED))
            {
                System.out.println("unsignedint" );
                s.trim();
                System.out.println(s);
                System.out.println();
                ps.setLong(q,Long.parseUnsignedLong(s));
                return;
            }
            switch (type) {
                case CHAR:
                case VARCHAR:
                case TEXT:
                case NVARCHAR:
                    System.out.println("varchar");
                    ps.setString(q, s);
                    break;
                case DATE:
                    Date date = new SimpleDateFormat("yyyy-mm-dd").parse(s);
                    ps.setDate(q, (java.sql.Date) date);
                    break;
                case FLOAT:
                    ps.setFloat(q, Float.parseFloat(s));
                    break;
                case REAL:
                case SMALLINT:
                    ps.setShort(q, Short.parseShort(s));
                    break;
                case BIT:
                    ps.setBoolean(q, Boolean.parseBoolean(s));
                    break;
                case BIGINT:
                    ps.setInt(q, Math.toIntExact(Long.parseLong(s)));
                    break;
                case NUMERIC:
                case DECIMAL:
                    ps.setDouble(q, Double.parseDouble(s));
                    break;
                case INT:
                    System.out.println("int");
                    ps.setInt(q, Integer.parseInt(s));
                    break;
                case IMAGE:
                    byte[] byteData = s.getBytes("UTF-8");//Better to specify encoding
                    Blob blobData = connection.createBlob();
                    blobData.setBytes(1, byteData);
                    ps.setBlob(q, blobData);
                    break;

            }
        }catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //todo getLastSelectedPathComponent
    public void write(String name, File file)
    {
        try {
            initConnection();
            String query = "select * from bp_tim68."+name;
            Statement preparedStatement = this.connection.createStatement();
            ResultSet rs = preparedStatement.executeQuery(query);
            if(rs == null) return;
            DBReader dbReader = new DBReader();
            dbReader.csvWriteAll(rs,file);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            this.closeConnection();
        }

    }


    private List<String> getColumnNames( String tableName) {
        List<String> columnNames = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++ ) {
                String name = rsmd.getColumnName(i);
                if(!columnNames.contains(name))
                    columnNames.add(name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("COLUMN NAMES");
        for(String s : columnNames)
        {
            System.out.print( " " + s);
        }
        return columnNames;
    }


    //privremeni queri checker cisto radi provere iskaza i podataka
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
