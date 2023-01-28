https://tutorcs.com
WeChat: cstutorcs
QQ: 749389476
Email: tutorcs@163.com

/*
 * Item - an abstract class that is for different items that make up a scene
 * Eric McCreath 2019
 */


public abstract class Item {
	P3D position;
	Material material;
	public abstract Intersect intersect(Ray ray);
}
