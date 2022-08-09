import java.util.ArrayList;

public class JsonCreate {
    private ArrayList<Stations> stations = new ArrayList<>();

    public ArrayList<Stations> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Stations> stations) {
        this.stations = stations;
    }

    public void setStations(String name, String line,
                            String date, String depth, String hasConnection) {
        Stations station = new Stations(name, line, date, depth, hasConnection);
        this.stations.add(station);
    }
}
