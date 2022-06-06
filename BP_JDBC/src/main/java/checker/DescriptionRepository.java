package checker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.ExportAction;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Getter
@Setter
public class DescriptionRepository {
    private  ObjectMapper objectMapper = new ObjectMapper();
    private  List<DBpojo> POJOSSS = getPojos();
//
    public List<DBpojo> getPojos() {
        List<DBpojo> pojos = null;
        try {
            URL url = DescriptionRepository.class.getResource("/json/jsonrulez.json");
            File file = new File(url.toURI());
            pojos = Arrays.asList(objectMapper.readValue(file, DBpojo[].class));
        }catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("POJOS");
        for(DBpojo d : pojos)
            System.out.print(d.toString() + " ");

        return pojos;

    }

}
