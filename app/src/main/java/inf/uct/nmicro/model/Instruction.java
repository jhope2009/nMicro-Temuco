package inf.uct.nmicro.model;

/**
 * Created by Javier on 15-11-2016.
 */

public class Instruction {

    private String indication;
    private Stop stop;
    private String hour;

    public Instruction() {
    }

    public Instruction(String indi, Stop parada){
        this.indication=indi;
        this.stop=parada;
    }

    public Instruction(String indication, Stop stop, String hour) {
        this.indication = indication;
        this.stop = stop;
        this.hour = hour;
    }

    public Stop getStop() {
        return stop;
    }

    public String getHour() {
        return hour;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
