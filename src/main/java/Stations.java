public class Stations {
    private String name;
    private String line;
    private String date;
    private String depth;
    private String hasConnection;

    public Stations(String name, String line, String date, String depth, String hasConnection) {
        this.name = name;
        this.line = line;
        this.date = date;
        this.depth = depth;
        this.hasConnection = hasConnection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getHasConnection() {
        return hasConnection;
    }

    public void setHasConnection(String hasConnection) {
        this.hasConnection = hasConnection;
    }
}
