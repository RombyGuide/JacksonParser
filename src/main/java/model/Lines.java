package model;

public class Lines {
    private String number;
    private String nameLine;

    public Lines(String number, String nameLine) {
        this.number = number;
        this.nameLine = nameLine;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNameLine() {
        return nameLine;
    }

    public void setNameLine(String nameLine) {
        this.nameLine = nameLine;
    }
}
