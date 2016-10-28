package inf.uct.nmicro.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.List;

/**
 * @author Javier
 *
 */
public class Route {

	private int idRoute;
	private String name;
	private List<Stop> stops;
	private List<Point> points;
	private Drawable img;
	private double signLatitude;
	private double signLongitude;

	public Route(){}

	public Route(int idRoute, String name, List<Stop> stops, List<Point> points, Drawable img, double signLatitude, double signLongitude) {
		super();
		this.idRoute = idRoute;
		this.name = name;
		this.stops = stops;
		this.points = points;
		this.img = img;
		this.signLatitude=signLatitude;
		this.signLongitude=signLongitude;
	}

	public Route(int idRoute, String name, List<Stop> stops, List<Point> points, double signLatitude, double signLongitude) {
		super();
		this.idRoute = idRoute;
		this.name = name;
		this.stops = stops;
		this.points = points;
		this.signLatitude=signLatitude;
		this.signLongitude=signLongitude;
	}

	public Route(int idRoute, String name, List<Point> points, double signLatitude, double signLongitude) {
		super();
		this.idRoute = idRoute;
		this.name = name;
		this.points = points;
		this.signLatitude=signLatitude;
		this.signLongitude=signLongitude;
	}

	public Route(int idRoute, String name, List<Point> points) {
		super();
		this.idRoute = idRoute;
		this.name = name;
		this.points = points;
	}

	public int getIdRoute() {
		return idRoute;
	}

	public String getName() {
		return name;
	}

	public List<Stop> getStops() {
		return stops;
	}

	public List<Point> getPoints() {
		return points;
	}

	public Drawable getImg() { return img;}

	public void setIdRoute(int idRoute) {
		this.idRoute = idRoute;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	public void setImg(Drawable img){ this.img = img; }

	public double getSignLatitude() { return signLatitude; }

	public double getSignLongitude() { return signLongitude; }
}
