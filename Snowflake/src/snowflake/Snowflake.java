package snowflake;

import org.lwjgl.util.vector.Vector2f;

public class Snowflake {
	
	private int numTimesToSubdivide = 5;
	private int numFlakes = (int)Math.pow(4, numTimesToSubdivide);
	private int numVertices = 3*numFlakes;
	
	
	private class KochSet{
		Vector2f l = null;
		Vector2f m = null;
		Vector2f n = null;
		Vector2f o = null;
		Vector2f p = null;
	}
	
	double deg60 = 60.0f * Math.PI/180.0;
	private int index = 0;
	
	Vector2f[] point = new Vector2f[numVertices];
	
	public KochSet computeKochSet(Vector2f s, Vector2f d){
		KochSet k = new KochSet();
		
		k.l = s;
		k.m = new Vector2f(s.x + (d.x - s.x)/4.0f, s.y + (d.y - s.y)/4.0f);
		k.o = new Vector2f(s.x + (d.x - s.x)*3/4.0f, s.y + (d.y - s.y)*3/4.0f);
		float px = (float)(Math.cos(deg60) * (k.o.x - k.m.x) - Math.sin(deg60) * (k.o.y - k.m.y) + k.m.x);
		float py = (float)(Math.sin(deg60) * (k.o.x - k.m.x) - Math.cos(deg60) * (k.o.y - k.m.y) + k.m.x);
		k.n = new Vector2f(px, py);
		k.p = d;
		
		return k;
	}
	
	public void divideSide(Vector2f a, Vector2f b, int count){
		
		if(count > 0){
			KochSet kab = computeKochSet(a,b);
			
			divideSide(kab.l, kab.m, count-1);
			divideSide(kab.m, kab.n, count-1);
			divideSide(kab.n, kab.o, count-1);
			divideSide(kab.o, kab.p, count-1);
		} else {
			point[index++] = a;
		}
		
	}
	
	public void divideTriangle(Vector2f a, Vector2f b, Vector2f c, int count){
		divideSide(a,b,count);
		divideSide(b,c,count);
		divideSide(c,a,count);
	}
	
	public void initSnowflake(){
		Vector2f[] vertices = {
				new Vector2f (-0.6f, -0.6f),
				new Vector2f (0, 0.6f),
				new Vector2f (0.6f, -0.6f)
		};
		
		index = 0;
		divideTriangle(vertices[0], vertices[1], vertices[2], numTimesToSubdivide);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
