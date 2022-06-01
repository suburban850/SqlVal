package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import resource.data.Row;

import java.io.*;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBReader {

    public List<Row> linebyline(Reader reader)
    {
        List<Row> rows = new ArrayList<>();
        List<String[]> lines = read(reader);
        for(String[] s : lines)
        {
            Row row = new Row();
            row.addField("N",s);
        }
        return null;
    }
    public List<String[]> read(Reader reader)
    {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> rows = new ArrayList<>();
        try {
            rows = csvReader.readAll();
            reader.close();
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public void csvWriteAll(ResultSet rs,File file)
    {
        try(CSVWriter writer = new CSVWriter(new FileWriter(file));) {
            writer.writeAll(rs,true);
        } catch (SQLException  | IOException e) {
            e.printStackTrace();
        }

    }

}
