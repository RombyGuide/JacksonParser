import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final String pathTarget = "src/main/resources";
    private static final JsonCreate jsonCreate = new JsonCreate();

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File(pathTarget+"\\map.json"), jsonCreate);

        JsonParser jsonParser = new JsonParser();

        jsonParser.traverse("data");

        FileInputStream inputStream = new FileInputStream("src/main/resources/map.json");
        try {
            String everything = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(everything);
        } finally {
            inputStream.close();
        }
    }
}
