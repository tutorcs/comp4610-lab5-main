https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com
import java.awt.Color;
import java.util.ArrayList;

/**
 * Scene - the list of items that make up a scene.
 * 
 * Eric McCreath 2009, 2019
 */

public class Scene extends ArrayList<Item> {

	Color background = Color.blue;

	public Color raytrace(Ray r) {
		
		Double mindis = null;
		Intersect intersect = null;

		// light
		P3D lightpos =  new P3D(25.0,30.0,-40.0);
		for (Item s : this) {
			Intersect i = s.intersect(r);
			if (i != null) {
				if (intersect == null || i.distance < mindis) {
					mindis = i.distance;
					intersect = i;
				}
			}
		}


		if (intersect != null) {
			P3D normal = intersect.hitNormal.normalize();
			P3D lightDir = lightpos.sub(intersect.hitPosition).normalize();
			P3D viewDir = r.position.sub(intersect.hitPosition).normalize();
			P3D reflectDir = normal.reflect(lightDir).normalize();

			double diffUse = Math.max(0, normal.dot(lightDir));
			double spec =  Math.pow(Math.max(0, viewDir.dot(reflectDir)), 32) * 0.8;

			double ambient = 0.2f;
			double val = Math.min(1, ambient + diffUse);

			Color color = intersect.color;
			float red = clamp(color.getRed()/255f * val + spec);
			float green = clamp(color.getGreen()/255f * val + spec);
			float blue = clamp(color.getBlue()/255f *val + spec);

			return new Color(red, green, blue);
		} else {
			return background;
		}
	}
	
	private float clamp(double r) {
		return (float)(r < 0.0 ? 0.0 : (r > 1.0 ? 1.0 : r));
	}

	

}
