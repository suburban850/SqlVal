package database;

import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import resource.DBNode;
import resource.data.Row;
import resource.implementation.Entity;

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

    @Override
    public void bulkImport(List<String[]> rows, Entity entity) {
        this.repository.bulkrep(rows,entity);
    }

    @Override
    public List<Row> checkDatabase(String sql) {
       return this.repository.check(sql);
    }


    public void checkk(String query)
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
