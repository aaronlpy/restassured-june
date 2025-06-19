package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class JSONDataLoader {


    @DataProvider(name="data")
    public static Iterator<Object[]> getData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File f = new File("src/main/resources/data.json");

        List<Object> list = mapper.readValue(f, List.class);
        return list.stream()
                .map(entry -> new Object[]{entry})
                .iterator();
    }

}
