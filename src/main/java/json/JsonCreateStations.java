package json;

import model.Stations;

import java.util.*;

public class JsonCreateStations {
    private Set<Stations> stations = new TreeSet<>();

    public Object setStations(Set<Stations> stations) {
        this.stations = stations;
        return stations;
    }

    public Set<Stations> getStations() {
        return stations;
    }
}