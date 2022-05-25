package BP_JDBC.src.main.java.database;

import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.data.Row;

import java.util.List;

public interface Repository {

    DBNode getSchema();

    List<Row> get(String from);
}
