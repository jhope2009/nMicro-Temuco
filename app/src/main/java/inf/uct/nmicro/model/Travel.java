package inf.uct.nmicro.model;

import org.osmdroid.google.wrapper.GeoPoint;

import java.util.List;

/**
 * Created by Javier on 07-11-2016.
 */

public class Travel {

    private Route routes;
    private int price;
    private GeoPoint startStop;
    private GeoPoint endStop;
    private int totalTime;
    private String startHour;
    private String endHour;
    private List<Instruction> instructions;

    public Travel() {}

    public Travel(Route routes, int price, GeoPoint startStop, GeoPoint endStop, int totalTime, String startHour, String endHour, List<Instruction> instructions) {
        this.routes = routes;
        this.price = price;
        this.startStop = startStop;
        this.endStop = endStop;
        this.totalTime = totalTime;
        this.startHour = startHour;
        this.endHour = endHour;
        this.instructions = instructions;
    }

    public Route getRoutes() {
        return routes;
    }

    public int getPrice() {
        return price;
    }

    public GeoPoint getStartStop() {
        return startStop;
    }

    public GeoPoint getEndStop() {
        return endStop;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public String getStartHour() {
        return startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setRoutes(Route routes) {
        this.routes = routes;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStartStop(GeoPoint startStop) {
        this.startStop = startStop;
    }

    public void setEndStop(GeoPoint endStop) {
        this.endStop = endStop;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }
}
