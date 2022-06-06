package database;


import resource.DBNode;
import resource.data.Row;
import resource.implementation.Entity;

import java.sql.SQLException;
import java.util.List;
//skladiste
public interface Repository {

    DBNode getSchema();

    List<Row> get(String from);

    //ako dodje do promene baze ostace implementacija za citanje csv file-a
    void bulkrep(List<String[]> rows, Entity entity);

    List<Row> check(String sql);
}
