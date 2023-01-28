https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
import java.awt.*;

public class Plane extends Item{
    P3D normal;
    P3D position;
    public Plane(P3D pos, P3D normal) {
        super();
        this.position = pos;
        this.normal = normal;
    }

    // intersect - returns either intersect object or null if the ray misses
    @Override
    public Intersect intersect(Ray ray) {

        // see http://en.wikipedia.org/wiki/Ray_tracing_(graphics)
        double vdotp = ray.position.sub(position).dot(ray.direction);
        double vdotd = ray.direction.dot(normal);
        if (vdotd == 0.0) return null;

        double t = vdotp / vdotd;
        P3D hit = ray.position.add(ray.direction.scale(t));

        if (hit.z < 0) return null;

        return new Intersect(t, hit, normal, this, Color.gray);
    }

    public String toString() {
        return "Floor : " + position + " color : " + material.color;
    }
}
