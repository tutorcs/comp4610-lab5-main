https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
import java.awt.Color;

/**
 * Sphere - this is the basic element that makes up a scene.
 * Eric McCreath 2009
 */


public class Sphere extends Item {
	double radius;
	public Sphere(P3D center, double radius, Color color) {
		super();
		this.position = center;
		this.radius = radius;
		this.material = new Material();
		this.material.color = color;
	}
	
	
	// intersect - returns either intersect object or null if the ray misses
	public Intersect intersect(Ray ray) {

		// see http://en.wikipedia.org/wiki/Ray_tracing_(graphics)
		
		P3D v = ray.position.sub(position);
		double vdotd = v.dot(ray.direction);
		double insqrt = vdotd*vdotd - (v.dot(v) - radius*radius);
		if (insqrt < 0.0) return null;
		
		double t = -vdotd - Math.sqrt(insqrt);  // we just take the smallest
		if (t < 0.0) return null;  // only in a positive direction of shooting out from the ray
		
		P3D pos = ray.position.add(ray.direction.scale(t));
		P3D norm = pos.sub(position).normalize();
		return new Intersect(t, pos, norm, this, material.color);
	}
	
	public String toString() {
		return "sphere center : " + position + " radius : " + radius + " color : " + material.color;
	}
	
}
