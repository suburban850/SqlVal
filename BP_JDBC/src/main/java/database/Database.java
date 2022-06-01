package database;



import resource.DBNode;
import resource.data.Row;

import java.util.List;

public interface Database{
    //povuci podatke
    DBNode loadResource();
    //konkretni podaci vraca listu redova
    List<Row> readDataFromTable(String tableName);


}
