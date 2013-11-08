package sierpinsky;

import java.nio.FloatBuffer;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import textureMapper.TextureMapper;

public class Sierpinsky extends SkelGL{

	
	private int numTimesToSubdivide = 5;
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
	
	Vector3f[] palette = {
			new Vector3f(1,0,0),
			new Vector3f(0,1,0),
			new Vector3f(0,0,1),
			new Vector3f(1,1,0)
		};
	
	TextureMapper tmap = null;
	
	Vector2f[] textureBounds = {
		new Vector2f(0.25f, 0.0f),
		new Vector2f(0.75f, 0.0f),
		new Vector2f( 0.5f, 0.5f)
	};
	
	Vector2f[] textures = new Vector2f[numVertices]; 
	
	float rotation = 0.5f;
	
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

	private void triangle(Vector3f a, Vector3f b, Vector3f c, int color) {
		
		Vector3f u = Vector3f.sub(a, b, null);
		Vector3f v = Vector3f.sub(c, b, null);
		Vector3f n = Vector3f.cross(u, v, null);
		
		points[index] = a; normals[index] = n; colors[index] = palette[color]; textures[index] = textureBounds[index%3]; index++;
		points[index] = b; normals[index] = n; colors[index] = palette[color]; textures[index] = textureBounds[index%3]; index++;
		points[index] = c; normals[index] = n; colors[index] = palette[color]; textures[index] = textureBounds[index%3]; index++;
		
		
	}

	private void initLightArrays() {
		//no constructor, not handled by JVM
        //malloc, memory management done outside of java
        matSpecular = BufferUtils.createFloatBuffer(4);
        matSpecular.put(2.0f).put(2.0f).put(2.0f).put(1.0f).flip();
       
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(-1f).put(1f).put(0.5f).put(0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1f).put(1f).put(1f).put(1.0f).flip();
        
        lModeAmbient = BufferUtils.createFloatBuffer(4);
        lModeAmbient.put(0.5f).put(0.1f).put(0.1f).put(1f).flip();
       
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
		if(animate){
			rotation += 0.01f * delta;
		}
	}

	@Override
	protected void resetGL() {
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		Rectangle bounds = canvas.getBounds();
		
		float aspect = bounds.width/bounds.height;
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		GL11.glViewport(0, 0, bounds.width, bounds.height);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
	}

	@Override
	protected void renderGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		Rectangle bounds = canvas.getBounds();
		float aspect = bounds.width/bounds.height;
		
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
		GL11.glViewport(0, 0, bounds.width, bounds.height);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glPushMatrix();
		GL11.glRotatef(rotation, 1,0,0);
		GL11.glRotatef(rotation, 0,1,0);
		GL11.glRotatef(rotation, 0,0,1);
		renderGasket();
		GL11.glPopMatrix();
	}

	private void renderGasket() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmap.getTextureID("metal"));
		GL11.glBegin(GL11.GL_TRIANGLES);
		for(int i = 0; i < numVertices; i++){
			GL11.glTexCoord2f(textures[i].x, textures[i].y);
			GL11.glNormal3f(normals[i].x, normals[i].y, normals[i].z);
			//GL11.glColor3f(colors[i].x, colors[i].y, colors[i].z);
			GL11.glVertex3f(points[i].x, points[i].y, points[i].z);
		}
		GL11.glEnd();
		
	}

	@Override
	protected void initTextures() {
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		tmap = new TextureMapper();
		tmap.createTexture("rock.jpg", "JPG", "rock");
		tmap.createTexture("metal.jpg", "JPG", "metal");
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
	}
	
	public static void main(String[] args){
		Sierpinsky s = new Sierpinsky();
		s.start();
	}

}
