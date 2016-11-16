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

    private final int POSITION_DIAMETER = 150;
    private final int POSITION_POINT = 50;
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
        List<Route> candidato1 = GetRoutebyStartPoint(origen,companies);
        List<Route> candidato2 = GetRoutebyEndpoint(destino,companies);
        List<Route> finales1 = new ArrayList<>();
        List<Route> finales2 = new ArrayList<>();
        List<GeoPoint> intermedios = new ArrayList<>();
        //busqueda de puntos intermedios
        for(Route ruta_Origen : candidato1){
            outerloop:
            for(Route ruta_Destino : candidato2){
                for(Point punto_ruta_Origen : ruta_Origen.getPoints()) {
                    GeoPoint Punto_intermedio = new GeoPoint(punto_ruta_Origen.getLatitude(),punto_ruta_Origen.getLongitude());
                    if(ruta_Origen.getIdRoute() != ruta_Destino.getIdRoute()) {
                        if (DrawinMap.isRouteInArea(ruta_Destino, Punto_intermedio)) {
                            intermedios.add(Punto_intermedio);
                            break outerloop;
                        }
                    }
                }
            }
        }//fin de la busqueda de candidatos
        //inicia la busqueda de rutas que sirven
        for(Route Ruta_Origen :candidato1){
            for(GeoPoint Punto_Intermedio :intermedios){
                int posicion_PIntermedio_en_RutaOrigen  = DrawinMap.isRouteInArea2(Ruta_Origen,Punto_Intermedio);
                int posicion_POrigen_en_RutaOrigen      = DrawinMap.isRouteInArea2(Ruta_Origen,origen);
                if(posicion_POrigen_en_RutaOrigen < posicion_PIntermedio_en_RutaOrigen){
                    finales1.add(Ruta_Origen);
                }
            }
        }

        for(Route Ruta_destino : candidato2){
            for(GeoPoint Punto_Intermedio :intermedios){
                int posicion_PIntermedio_en_RutaDestino = DrawinMap.isRouteInArea2(Ruta_destino,Punto_Intermedio);
                int posicion_PDestino_en_RutaDestino    = DrawinMap.isRouteInArea2(Ruta_destino,destino);
                if(posicion_PDestino_en_RutaDestino > posicion_PIntermedio_en_RutaDestino){
                    finales2.add(Ruta_destino);
                }
            }
        }
        //elimino las rutas que se repiten en los arreglos ademas de eliminar los puntos intermedios que tambien se repiten.
        Set<Route> aux=new HashSet<Route>();
        aux.addAll(finales2);
        finales2.clear();
        finales2.addAll(aux);

        Set<Route> aux2=new HashSet<Route>();
        aux2.addAll(finales1);
        finales1.clear();
        finales1.addAll(aux2);

        Set<GeoPoint> aux3=new HashSet<GeoPoint>();
        aux3.addAll(intermedios);
        intermedios.clear();
        intermedios.addAll(aux3);


        List<Travel> viajes=GetFinalRoute(finales2,finales1,intermedios);
        return viajes;

    }//metodo de busqueda del viaje

public List<Travel> GetFinalRoute(List<Route> Rxorigen,List<Route> Rxdestino, List<GeoPoint> intermedios){
        List<Travel> viajes=new ArrayList<>();
        int pcr1=0;
        int prc2=0;
        List<Point> PuntosFinalesA=new ArrayList<>();
        List<Point> PuntosFinalesB=new ArrayList<>();
        List<Instruction> inst=new ArrayList<>();
        Instruction istr=new Instruction();
        for(Route Origen : Rxorigen) {
            for (Route destino : Rxdestino) {
                for(GeoPoint gp : intermedios){
                    if(DrawinMap.isRouteInArea(Origen,gp) && DrawinMap.isRouteInArea(destino,gp)){
                        istr.setIndication("Bajate En: "+String.valueOf(gp.getLatitude())+String.valueOf(gp.getLongitude()));
                        inst.add(istr);
                         pcr1=DrawinMap.isRouteInArea2(Origen,gp);
                         prc2=DrawinMap.isRouteInArea2(destino,gp);
                         for(int i=0;i==pcr1;i++){
                             PuntosFinalesA.add(Origen.getPoints().get(i));
                             Log.i("punto: ",String.valueOf(Origen.getPoints().get(i).getLatitude())+String.valueOf(Origen.getPoints().get(i).getLongitude()));
                         }
                         for(int j=prc2;j==destino.getPoints().size();j++){
                             PuntosFinalesB.add(Origen.getPoints().get(j));
                             Log.i("punto: ",String.valueOf(Origen.getPoints().get(j).getLatitude())+String.valueOf(Origen.getPoints().get(j).getLongitude()));
                             Route ruta1=new Route(Origen.getIdRoute(),Origen.getName(),PuntosFinalesB,Origen.getImg());
                             Route ruta2=new Route(destino.getIdRoute(),destino.getName(),PuntosFinalesB,destino.getImg());
                             List<Route> rutas=new ArrayList<>();
                             rutas.add(ruta1);
                             rutas.add(ruta2);
                             Travel viaje=new Travel(rutas);
                             viajes.add(viaje);
                         }
                    break;
                    }
                }

            }
        }
    return viajes;
}


}




