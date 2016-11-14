package inf.uct.nmicro.fragments;

import android.app.Activity;
import android.location.Address;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import inf.uct.nmicro.model.Company;
import inf.uct.nmicro.model.Point;
import inf.uct.nmicro.model.Route;
import inf.uct.nmicro.model.Stop;

/**
 * Created by Esteban Campos A on 11-11-2016.
 */
public class SearchTraveling extends Activity {

    private final int POSITION_DIAMETER = 150;
    DrawInMap DrawinMap = new DrawInMap();

    public List<Route> GetRoutebyStartPoint(GeoPoint startPoint,List<Company> companies) {
        List<Route> candidatos = new ArrayList<>();
        for (Company c : companies) {
            for (Route ruta : c.getRoutes()) {
                if (DrawinMap.isRouteInArea(ruta, startPoint)) {
                    candidatos.add(ruta);
                }
            }
        }
        return candidatos;
    }

    public List<Route> GetRoutebyEndpoint(GeoPoint endpoint,List<Company> companies){
            List<Route> utiles=new ArrayList<>();
            for(Company c : companies){
                for(Route ruta : c.getRoutes()){
                    if (DrawinMap.isRouteInArea(ruta, endpoint)) {
                        utiles.add(ruta);
                    }
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

    public void GetTravel(List<Company> companies, GeoPoint origen, GeoPoint destino) {

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

        //List<Stop> Bajadas=GetStops4Travel(finales2,finales1,intermedios);
        //traer los paraderos de las rutas en los arreglos y aplicar is route in area;
        Log.i("las rutas combinadas: ","====");
        for(Route Ricial : finales2){
            for(Route Rfinal : finales1){
                Log.i("Primero Toma la: ",Ricial.getName() +" Luego toma la: "+Rfinal.getName());
            }
        }


    }

}




