import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String pathSource = "https://skillbox-java.github.io/";
    private static final String pathTarget = "src/main/resources";
    private static final JsonCreate jsonCreate = new JsonCreate();

    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect(pathSource).get();
        getLinesStationsConnections(document);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(new File(pathTarget+"\\map.json"), jsonCreate);

        JsonNode rootNode = objectMapper.readTree(new File(pathTarget+"\\map.json"));
        JsonNode stationsNode = rootNode.path("stations");
        Map<String, List> stationsMap = objectMapper.convertValue(stationsNode, new TypeReference<Map<String, List>>() {});
        stationsMap.forEach((o1, o2) -> System.out.println("stations: " + o1 + " count " + o2.size()));

        System.out.println("количество станций с переходами: " + jsonCreate.getConnections().size());
    }

    private static void getLinesStationsConnections (Document document) {
        Elements parsedLines = document.select("span.js-metro-line");

        for (Element line : parsedLines) {
            String lineNumber = line.attr("data-line");
            jsonCreate.setLines(lineNumber, line.text());

            ArrayList<String> stationsList= new ArrayList<>();
            String parserIndex = "div.js-metro-stations[data-line=" + lineNumber + "] > p > span.";
            Elements parsedStations = document.select(parserIndex+ "name");
            for (Element station : parsedStations) {
                stationsList.add(station.text());
            }
            jsonCreate.setStations(lineNumber, stationsList);

            Elements parsedConnections = document.select(parserIndex + "t-icon-metroln");
            for (Element connection : parsedConnections){
                jsonCreate.setConnections(connection.attr("title"), lineNumber);
            }
        }
    }
}
