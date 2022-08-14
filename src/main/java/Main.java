import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import json.JsonCreateMap;
import json.JsonCreateStations;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String pathTargetStations = "src/main/resources/stations.json";
    private static final String pathTargetMap = "src/main/resources/";
    private static final String pathSource = "https://skillbox-java.github.io/";
    private static final JsonCreateStations jsonCreateStations = new JsonCreateStations();
    private static final JsonCreateMap jsonCreateMap = new JsonCreateMap();

    public static void main(String[] args) throws Exception {
        Document document = Jsoup.connect(pathSource).get();
        getLinesStationsConnections(document);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File(pathTargetMap+"\\map.json"), jsonCreateMap);

        JsonParser jsonParser = new JsonParser();

        String resultStationsJson = objectMapper.writeValueAsString(
                jsonCreateStations.setStations(jsonParser.traverse("data")));
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(pathTargetStations),
                jsonCreateStations);

        FileInputStream inputStream = new FileInputStream("src/main/resources/stations.json");
        try {
            String everything = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//            System.out.println(everything);
        } finally {
            inputStream.close();
        }

        JsonNode rootNode = objectMapper.readTree(new File(pathTargetMap+"\\map.json"));
        JsonNode stationsNode = rootNode.path("stations");
        Map<String, List> stationsMap = objectMapper.convertValue(stationsNode, new TypeReference<>() {
        });

        stationsMap.forEach((o1, o2) -> System.out.println("Кол-во станций на линии №" + o1.concat(": ") + o2.size()));

//        System.out.println("Кол-во станций с переходами: " + jsonCreateMap.getConnections().size());

    }

    private static void getLinesStationsConnections (Document document) {
        Elements parsedLines = document.select("span.js-metro-line");

        for (Element line : parsedLines) {
            String lineNumber = line.attr("data-line");
            jsonCreateMap.setLines(lineNumber, line.text());

            ArrayList<String> stationsList= new ArrayList<>();
            String parserIndex = "div.js-metro-stations[data-line=" + lineNumber + "] > p > span.";
            Elements parsedStations = document.select(parserIndex+ "name");
            for (Element station : parsedStations) {
                stationsList.add(station.text());
            }
            jsonCreateMap.setStations(lineNumber, stationsList);

            Elements parsedConnections = document.select(parserIndex + "t-icon-metroln");
            for (Element connection : parsedConnections){
                jsonCreateMap.setConnections(connection.attr("title"), lineNumber);
            }
        }
    }
}
