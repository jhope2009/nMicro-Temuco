package inf.uct.nmicro.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Instruction;
import inf.uct.nmicro.model.Point;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Stop;
import inf.uct.nmicro.model.Travel;

/**
 * Created by Esteban Campos A on 11-11-2016.
 */
public class SearchTraveling extends Activity {

    private final int POSITION_DIAMETER = 250;
    private final int POSITION_POINT = 150;
    DrawInMap DrawinMap = new DrawInMap();

    public List<Route> GetRoutebyStartPoint(GeoPoint startPoint,List<Company> companies) {
        List<Route> candidatos = new ArrayList<>();
        for (Company c : companies) {
            for (Route ruta : c.getRoutes()) {
                if (DrawinMap.isRouteInArea(ruta, startPoint)) {candidatos.add(ruta);}
            }
        }
        return candidatos;
    }

    public List<Route> GetRoutebyEndpoint(GeoPoint endpoint,List<Company> companies){
            List<Route> utiles=new ArrayList<>();
            for(Company c : companies){
                for(Route ruta : c.getRoutes()){
                    if (DrawinMap.isRouteInArea(ruta, endpoint)){utiles.add(ruta);}
                }
            }
        return utiles;
    }

    public List<Stop> GetStops4Travel(List<Route> inicio, List<Route> finales, List<GeoPoint> puntos){
        List<Stop> paradas=new ArrayList<>();

        for(Route r :inicio) {
            for(Stop st : r.getStops()) {
                for (GeoPoint gp : puntos) {
                    int distance = new GeoPoint(st.getLatitude(), st.getLongitude()).distanceTo(gp);
                    if (distance < POSITION_DIAMETER) {
                        paradas.add(st);

                    }
                }
            }
        }
        for(Route ro : finales) {
            for (Stop st : ro.getStops()) {
                for (GeoPoint gp : puntos) {
                    int distance = new GeoPoint(st.getLatitude(), st.getLongitude()).distanceTo(gp);
                    if (distance < POSITION_DIAMETER) {
                        paradas.add(st);

                    }
                }
            }
        }
    return paradas;
    }
    public List<String> GetAdress4Intermedios(Geocoder geocoder,List<GeoPoint> pinteres) throws IOException {
        List<String> direction4points=new ArrayList<>();
        String direccion="";
        for(GeoPoint gp : pinteres){
            List<Address> ub0 = geocoder.getFromLocation(gp.getLatitude(),gp.getLongitude(),1);
            direccion=ub0.get(0).toString();
            direction4points.add(direccion);
            ub0.clear();
        }
        return direction4points;
    }

//terminar el metodo

    public List<Travel> GetTravel(List<Company> companies, GeoPoint origen, GeoPoint destino) {
        List<Route> candidato1 = GetRoutebyStartPoint(origen, companies);
        List<Route> candidato2 = GetRoutebyEndpoint(destino, companies);
        List<Route> finales1 = new ArrayList<>();
        List<Route> finales2 = new ArrayList<>();
        List<GeoPoint> intermedios = new ArrayList<>();
        //busqueda de puntos intermedios
        for (Route ruta_Origen : candidato1) {
            outerloop:
            for (Route ruta_Destino : candidato2) {
                for (Point punto_ruta_Origen : ruta_Origen.getPoints()) {
                    GeoPoint Punto_intermedio = new GeoPoint(punto_ruta_Origen.getLatitude(), punto_ruta_Origen.getLongitude());
                    if (ruta_Origen.getIdRoute() != ruta_Destino.getIdRoute()) {
                        if (DrawinMap.isRouteInArea(ruta_Destino, Punto_intermedio)) {
                            intermedios.add(Punto_intermedio);
                            break outerloop;
                        }
                    }
                }
            }
        }//fin de la busqueda de candidatos
        //inicia la busqueda de rutas que sirven
        for (Route Ruta_Origen : candidato1) {
            for (GeoPoint Punto_Intermedio : intermedios) {
                int posicion_PIntermedio_en_RutaOrigen = DrawinMap.isRouteInArea2(Ruta_Origen, Punto_Intermedio);
                int posicion_POrigen_en_RutaOrigen = DrawinMap.isRouteInArea2(Ruta_Origen, origen);
                if (posicion_POrigen_en_RutaOrigen < posicion_PIntermedio_en_RutaOrigen) {
                    finales1.add(Ruta_Origen);
                }
            }
        }
        for (Route Ruta_destino : candidato2) {
            for (GeoPoint Punto_Intermedio : intermedios) {
                int posicion_PIntermedio_en_RutaDestino = DrawinMap.isRouteInArea2(Ruta_destino, Punto_Intermedio);
                int posicion_PDestino_en_RutaDestino = DrawinMap.isRouteInArea2(Ruta_destino, destino);
                if (posicion_PDestino_en_RutaDestino > posicion_PIntermedio_en_RutaDestino) {
                    finales2.add(Ruta_destino);
                }
            }
        }
        //elimino las rutas que se repiten en los arreglos ademas de eliminar los puntos intermedios que tambien se repiten.
        Set<Route> aux = new HashSet<Route>();
        aux.addAll(finales2);
        finales2.clear();
        finales2.addAll(aux);
        Set<Route> aux2 = new HashSet<Route>();
        aux2.addAll(finales1);
        finales1.clear();
        finales1.addAll(aux2);
        Set<GeoPoint> aux3 = new HashSet<GeoPoint>();
        aux3.addAll(intermedios);
        intermedios.clear();
        intermedios.addAll(aux3);

        List<Travel> viajes = GetFinalTravels(finales2, finales1, intermedios);
        return viajes;

    }//metodo de busqueda del viaje

public List<Travel> GetFinalTravels(List<Route> Rxorigen,List<Route> Rxdestino, List<GeoPoint> intermedios){
        int aux=0;
        List<Travel> travels=new ArrayList<>();
        List<Route> rtravel=new ArrayList<>();
        List<Instruction> instr=new ArrayList<>();
        int a=0;
        Instruction inst=new Instruction();
        for(Route r1 :Rxorigen){
            aux++;
            for(Route r2 :Rxdestino){
                for(GeoPoint gp: intermedios){
                if(DrawinMap.isRouteInArea(r1,gp) && DrawinMap.isRouteInArea(r2,gp)){
                   // inst.setIndication("Bajete en:"+gp.getLongitude()+" - "+ gp.getLongitude());
                    instr.add(inst);
                    break;
                }

                }
                rtravel.add(r2);
                rtravel.add(r1);
                Log.i("Combinadas",r2.getName()+" - "+r1.getName());
                travels.add(new Travel(aux, r2.getName()+" - "+r1.getName(), rtravel,instr));
                rtravel.clear();

            }
            }

    return travels;

}



}




