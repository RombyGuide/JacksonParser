import model.Stations;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonParser {
    private static Set<Stations> stations = new TreeSet<>(Comparator.comparing(Stations::getName));
    private static Map<String, ArrayList<String>> linesAndStations = new HashMap<>();
    private static List<String> stationsWithConnections = new ArrayList<>();

    private static Document docHTML;
    static {
        try {
            docHTML = Jsoup.connect("https://skillbox-java.github.io/").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Document doc = Jsoup.parse(String.valueOf(docHTML));
    private static Elements elementsDiv =
            doc.getElementsByClass("js-metro-stations t-metrostation-list-table");

    public static void readHTML() {
//      add Map<String, ArrayList<String>> linesAndStations
        for (Element element : elementsDiv) {
            linesAndStations.put(element.attr("data-line"), new ArrayList<>());
            ArrayList<String> currentList = linesAndStations.get(element.attr("data-line"));
            Elements names = element.select(".name");
            names.forEach(name -> currentList.add(name.text()));
        }
//      add List<String> stationsWithConnections
        Elements connections = doc.select("p:has(.t-icon-metroln)").select(".name");
        connections.forEach(element -> stationsWithConnections.add(element.text()));
    }

    public static void addLineAndConnection() {
//      set Line in Station
        for (Stations currentStation : stations) {
            for (Map.Entry<String, ArrayList<String>> element : linesAndStations.entrySet()) {
                for (String s : element.getValue()) {
                    if (s.equals(currentStation.getName())) {
                        currentStation.setLine(element.getKey());
                    }
                }
            }
        }
//      set isConnection
        for (Stations currentStation : stations) {
            stationsWithConnections.forEach(s -> {
                if (s.equals(currentStation.getName())) {
                    currentStation.setHasConnection(true);
                }
            });
        }
    }

    public static void parseJSONOne(String file) throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(file);
        for (Object line : jsonArray) {
            JSONObject object = (JSONObject) line;
            Stations station = new Stations(object.get("name").toString());
            if (!stations.isEmpty()) {
                for (Stations currentStation : stations) {
                    if (currentStation.equalsName(station)) {
                        currentStation.setDepth(object.get("depth").toString());
                    }
                }
            }
            station.setDepth(object.get("depth").toString());
            stations.add(station);
        }
    }

    public static void parseJSONTwo(String file) throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(file);
        for (Object line : jsonArray) {
            JSONObject object = (JSONObject) line;
            Stations station = new Stations(object.get("name").toString());
            if (!stations.isEmpty()) {
                for (Stations currentStation : stations) {
                    if (currentStation.equalsName(station)) {
                        currentStation.setDate(object.get("date").toString());
                    }
                }
            }
            station.setDate(object.get("date").toString());
            stations.add(station);
        }
    }

    public static void parseJSONTree(String file) throws Exception {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(file);
        for (Object line : jsonArray) {
            JSONObject object = (JSONObject) line;
            Stations station = new Stations(object.get("station_name").toString());
            if (!stations.isEmpty()) {
                for (Stations currentStation : stations) {
                    if (currentStation.equalsName(station)) {
                        currentStation.setDepth(object.get("depth_meters").toString());
                    }
                }
            }
            station.setDepth(object.get("depth_meters").toString());
            stations.add(station);
        }
    }

    public Set<Stations> traverse(String dir) throws Exception {
        readHTML();
        addLineAndConnection();
        File file = new File(dir);
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; children != null && i < children.length; i++) {
                traverse(String.valueOf(new File(file, children[i])));
            }
        }
        if (file.isFile() && file.getAbsolutePath().endsWith(".csv")) {
            parserCSV(readFileInPath(dir));
        }
        if (file.isFile()) {
            if (file.getName().endsWith(".json")) {
                if (file.getName().equals("depths-1.json")) {
                    parseJSONOne(readFileInPath(dir));
                }
                if (file.getName().equals("dates-2.json")) {
                    parseJSONTwo(readFileInPath(dir));
                }
                if (file.getName().equals("depths-3.json")) {
                    parseJSONTree(readFileInPath(dir));
                }
            }
        }
        return stations;
    }

    public static void parserCSV(String file) {
        String[] lines = file.split("\n");

        if (lines[0].equals("Название,Глубина")) {
            for (int i = 1; i < lines.length; i++) {
                String[] data = lines[i].split(",", 2);
                Stations station = new Stations(data[0]);
                if (!stations.isEmpty()) {
                    for (Stations currentStation : stations) {
                        if (currentStation.equalsName(station)) {
                            currentStation.setDepth(data[1]);
                        }
                    }
                }
                station.setDepth(data[1]);
                stations.add(station);
            }
        }

        if (lines[0].equals("Название станции,Дата открытия")) {
            for (int i = 1; i < lines.length; i++) {
                String[] data = lines[i].split(",", 2);
                Stations station = new Stations(data[0]);
                if (!stations.isEmpty()) {
                    for (Stations currentStation : stations) {
                        if (currentStation.equalsName(station)) {
                            currentStation.setDate(data[1]);
                        }
                    }
                }
                station.setDate(data[1]);
                stations.add(station);
            }
        }
    }

    public static String readFileInPath(String path) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        lines.forEach(line -> stringBuilder.append(line + "\n"));
        return stringBuilder.toString();
    }
}