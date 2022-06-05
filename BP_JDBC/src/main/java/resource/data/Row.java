package resource.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
//name = regions
//Mapa<region_id 3, region_name asia>
//
//
@Data
//sadrzaj tabele
public class Row {
    //tabela
    private String name;
    //u kom polju je koja vrednost
    //country_name, brazil
    private Map<String, Object> fields;


    public Row() {
        this.fields = new HashMap<>();
    }

    public void addField(String fieldName, Object value) {
        this.fields.put(fieldName, value);
    }

    public void removeField(String fieldName) {
        this.fields.remove(fieldName);
    }

}
