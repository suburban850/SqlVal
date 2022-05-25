package BP_JDBC.src.main.java.database;

import BP_JDBC.src.main.java.resource.DBNode;
import BP_JDBC.src.main.java.resource.data.Row;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data

public class DatabaseImplementation implements Database {

    private Repository repository;

    public DatabaseImplementation(Repository repository) {
        this.repository = repository;
    }

    @Override
    public DBNode loadResource() {
        return repository.getSchema();
    }

    @Override
    public List<Row> readDataFromTable(String tableName) {
        return repository.get(tableName);
    }
}
