import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class JsonParser {
    JsonCreate jsonCreateJson = new JsonCreate();

    public void parseJsonFile(String path) {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(path);
            JSONArray array = (JSONArray) parser.parse(reader);
            for (Object obj : array) {
                JSONObject stationJsonObject = (JSONObject) obj;
                String nameStation = (String) stationJsonObject.get("name");
                String depthStation = String.valueOf(stationJsonObject.get("depth"));
                String dateStation = (String) stationJsonObject.get("date");
                String nameStation1 = (String) stationJsonObject.get("station_name");
                String depthMetersStations = String.valueOf(stationJsonObject.get("depth_meters"));
                jsonCreateJson.setStations(nameStation, depthStation,
                        dateStation, nameStation1, depthMetersStations);
//                System.out.println(nameStation);
//                System.out.println(depthStation);
//                System.out.println(dateStation);
//                System.out.println(nameStation1);
//                System.out.println(depthMetersStations);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void traverse(String dir){
        File file = new File(dir);
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; children != null && i < children.length; i++) {
                traverse(String.valueOf(new File(file, children[i])));
            }
        }
        if (file.isFile()) {
            if (file.getName().endsWith(".json")) {
                parseJsonFile(dir);
            }
        }
    }
}