package BP_JDBC.src.main.java.database;



import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.data.Row;

import java.util.List;

public interface Database{

    DBNode loadResource();

    List<Row> readDataFromTable(String tableName);


}
