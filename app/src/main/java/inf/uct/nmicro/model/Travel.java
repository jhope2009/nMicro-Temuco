package inf.uct.nmicro.model;

import org.osmdroid.google.wrapper.GeoPoint;

import java.util.List;

/**
 * Created by Javier on 07-11-2016.
 */

public class Travel {

    private int idTravel;
    private String name;
    private List<Route> routes;
    private int price;
    private GeoPoint startStop;
    private GeoPoint endStop;
    private int totalTime;
    private String startHour;
    private String endHour;
    private List<Instruction> instructions;

    public Travel() {}

    public Travel(int id,String name,List<Route> rt,List<Instruction> ins){
        this.name=name;
        this.routes=rt;
        this.idTravel=id;
        this.instructions=ins;

    }
    public Travel(int id,String name,List<Route> rt){
        this.name=name;
        this.routes=rt;
        this.idTravel=id;
        }
    public Travel(List<Route> routes, int price, GeoPoint startStop, GeoPoint endStop, int totalTime, String startHour, String endHour, List<Instruction> instructions) {
        this.routes = routes;
        this.price = price;
        this.startStop = startStop;
        this.endStop = endStop;
        this.totalTime = totalTime;
        this.startHour = startHour;
        this.endHour = endHour;
        this.instructions = instructions;
    }


    public Travel(int idTravel, List<Route> routes, int price, GeoPoint endStop, GeoPoint startStop, int totalTime, String startHour, String endHour, List<Instruction> instructions) {
        this.idTravel = idTravel;
        this.routes = routes;
        this.price = price;
        this.endStop = endStop;
        this.startStop = startStop;
        this.totalTime = totalTime;
        this.startHour = startHour;
        this.endHour = endHour;
        this.instructions = instructions;
    }

    public String getname(){
        return this.name;

    }
    public int getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(int idTravel) {
        this.idTravel = idTravel;
    }

    public List<Route> getRoutes() {
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

    public void setRoutes(List<Route> routes) {
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
