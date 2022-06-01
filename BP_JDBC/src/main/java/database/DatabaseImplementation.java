package database;

import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import resource.DBNode;
import resource.data.Row;

import java.io.File;
import java.util.List;

@Data
@Getter
@Setter
public class DatabaseImplementation implements Database {

    private Repository repository;

    public DatabaseImplementation(Repository repository) {
        this.repository = repository;
    }

    @Override
    public DBNode loadResource() {
        //uzmi strukturu baze
        return repository.getSchema();
    }

    @Override
    public List<Row> readDataFromTable(String tableName) {
        return repository.get(tableName);
    }

    public void check(String query)
    {
        if(this.repository instanceof MYSQLrepository)
        {
            MYSQLrepository ms = (MYSQLrepository) repository;
            ms.zabrisanje(query);
        }
    }
    //dbnode
    public void zabr(String s, File file)
    {
        if(this.repository instanceof MYSQLrepository)
        {
            MYSQLrepository ms = (MYSQLrepository) repository;
            ms.write(s,file);
        }
    }
}
