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
	private String icon;

	public Route(){}

	public Route(int idRoute, String name, List<Stop> stops, List<Point> points, Drawable img) {
		super();
		this.idRoute = idRoute;
		this.name = name;
		this.stops = stops;
		this.points = points;
		this.img = img;
	}

	public Route(int idRoute, String name, List<Stop> stops, List<Point> points, String icon) {
		super();
		this.idRoute = idRoute;
		this.name = name;
		this.stops = stops;
		this.points = points;
        this.icon = icon;
	}

	public Route(int idRoute, String name, List<Point> points, String icon) {
		super();
		this.idRoute = idRoute;
		this.name = name;
		this.points = points;
		this.icon = icon;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
