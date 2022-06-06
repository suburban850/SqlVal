package database;



import resource.DBNode;
import resource.data.Row;
import resource.implementation.Entity;

import java.util.List;

public interface Database{
    //povuci podatke
    DBNode loadResource();
    //konkretni podaci vraca listu redova
    List<Row> readDataFromTable(String tableName);

    void bulkImport(List<String[]> rows, Entity entity);

    void checkDatabase(String sql);
}
