package sierpinsky;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import textureMapper.TextureMapper;

public class Sierpinsky extends SkelGL{

	private int numTimesToSubdivide = 2;
	private int numTetra = (int) Math.pow(4.0, numTimesToSubdivide);
	 
	private int numTriangles = 4*numTetra;
	private int numVertices = 3*numTriangles;
	
	private Vector3f[] points = new Vector3f[numVertices];
	private Vector3f[] colors = new Vector3f[numVertices];
	private Vector3f[] normals = new Vector3f[numVertices];
	
	private int index = 0;
	
	private Vector3f[] vertices = {
			new Vector3f(0,0,-1),
			new Vector3f(0,1,.5f),
			new Vector3f(-1, -0.5f, 0.5f),
			new Vector3f(1, -.5f, .5f)
	};
	
	TextureMapper tmap = null;
	
	Vector2f[] textureBounds = {
		new Vector2f(0.25f, 0.0f),
		new Vector2f(0.75f, 0.0f),
		new Vector2f( 0.5f, 0.5f)
	};
	
	Vector2f[] textures = new Vector2f[numVertices]; 
	
	float rotation = 20.0f;
	
	private FloatBuffer matSpecular;
	private FloatBuffer lightPosition;
	private FloatBuffer whiteLight;
	private FloatBuffer lModeAmbient;
	
	@Override
	protected void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspect = WIDTH / (float) HEIGHT;
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
 
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glClearColor(0, 0, 0, 0);

		initLightArrays();
		initTextures();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	
		initGasket();
	}

	private void initGasket() {
		index = 0;
		divideTetra(vertices[0], vertices[1], vertices[2], vertices[3], numTimesToSubdivide);
		
	}

	private void divideTetra(Vector3f a, Vector3f b, Vector3f c, Vector3f d, int m) {
		if(m>0){
			Vector3f[] mid = new Vector3f[6];
			mid[0] = Vector3f.add(a, b, null);
			mid[1] = Vector3f.add(a, c, null);
			mid[2] = Vector3f.add(a, d, null);
			mid[3] = Vector3f.add(b, c, null);
			mid[4] = Vector3f.add(b, d, null);
			mid[5] = Vector3f.add(c, d, null);
			
			//divide the resultant vector by 2
			mid[0].scale(0.5f);
			mid[1].scale(0.5f);
			mid[2].scale(0.5f);
			mid[3].scale(0.5f);
			mid[4].scale(0.5f);
			mid[5].scale(0.5f);
			
			divideTetra(a, mid[0], mid[1], mid[2], m-1);
			divideTetra(mid[0], b, mid[3], mid[4],    m-1);
			divideTetra(mid[1], mid[3], c, mid[5],    m-1);
			divideTetra(mid[2], mid[4],    mid[5], d, m-1);
			
		}else{
			Tetra(a,b,c,d);
		}
		
	}

	private void Tetra(Vector3f a, Vector3f b, Vector3f c, Vector3f d) {
		triangle(a, b, c, 0);
		triangle(a, c, d, 1);
		triangle(b, d, c, 2);
		triangle(a, d, b, 3);
		
	}

	private void triangle(Vector3f a, Vector3f c, Vector3f d, int i) {
		// TODO Auto-generated method stub
		
	}

	private void initLightArrays() {
		//no constructor, not handled by JVM
        //malloc, memory management done outside of java
        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(2.0f).put(2.0f).put(2.0f).put(1.0f).flip();
       
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(-1f).put(1f).put(0.5f).put(0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
        
        lModeAmbient = BufferUtils.createFloatBuffer(4);
        lModeAmbient.put(0.1f).put(0.1f).put(0.1f).put(1f).flip();
       
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecular);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 60.0f);
       
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, whiteLight);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, whiteLight);
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, lModeAmbient);
       
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glEnable((GL11.GL_COLOR_MATERIAL));
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
		
	}

	@Override
	protected void update(int delta) {
	
	}

	@Override
	protected void resetGL() {
		
		
	}

	@Override
	protected void renderGL() {
		
	}

	@Override
	protected void initTextures() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		
	}

}
