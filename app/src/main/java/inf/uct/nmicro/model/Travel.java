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
    private Stop startStop;
    private Stop endStop;
    private int price;
    private List<Instruction> instructions;

    public Travel() {}
    public Travel(String name, List<Route> routes){
        this.name=name;
        this.routes=routes;
    }

    public Travel(int id,String name,List<Route> rt,List<Instruction> ins,Stop st1,Stop st2){
        this.idTravel=id;
        this.name=name;
        this.routes=rt;
        this.instructions=ins;
        this.startStop=st1;
        this.endStop=st2;

    }
    public Travel(int id,String name,List<Route> rt){
        this.name=name;
        this.routes=rt;
        this.idTravel=id;
    }

    public Travel(int idTravel, String name, List<Route> routes, Stop start_stop, Stop end_stop, int price, List<Instruction> instructions) {
        this.idTravel = idTravel;
        this.name = name;
        this.routes = routes;
        this.startStop = start_stop;
        this.endStop = end_stop;
        this.price = price;
        this.instructions = instructions;
    }

    public String getname(){
        return name;

    }
    public void SetName(String g){
        this.name=g;
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

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public Stop getStartStop() {
        return startStop;
    }

    public Stop getEndStop() {
        return endStop;
    }

    public void setStartStop(Stop startStop) {
        this.startStop = startStop;
    }

    public void setEndStop(Stop endStop) {
        this.endStop = endStop;
    }
}
