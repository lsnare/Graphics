package basiccube6;

import java.nio.FloatBuffer;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import textureMapper.TextureMapper;

public class AnimCube extends SkelGL{

	//hardware independent implementation of C sizeof() functions
	private int sizeOfInt = Integer.SIZE / Byte.SIZE;
	private int sizeOfFloat = Float.SIZE / Byte.SIZE;
	private int sizeOfDouble = Double.SIZE / Byte.SIZE;
	FloatBuffer buffer;
	
	private int numVertices = 36;
	
	private Vector4f[] points = new Vector4f[numVertices];
	private Vector3f[] colors = new Vector3f[numVertices];
	private Vector3f[] normals = new Vector3f[numVertices];
	
	private int index = 0;
	
	private int vboID;
	
	Vector3f[] palette = {
			new Vector3f(1,0,0),
			new Vector3f(0,1,0),
			new Vector3f(0,0,1),
			new Vector3f(1,0,1),
			new Vector3f(1,1,0),
			new Vector3f(1,1,1)
	};
	
	Vector4f[] vertices = {
			new Vector4f(-0.5f, -0.5f,  0.5f, 1.0f),
			new Vector4f(-0.5f,  0.5f,  0.5f, 1.0f),
			new Vector4f( 0.5f,  0.5f,  0.5f, 1.0f),
			new Vector4f( 0.5f, -0.5f,  0.5f, 1.0f),
			new Vector4f(-0.5f, -0.5f, -0.5f, 1.0f),
			new Vector4f(-0.5f,  0.5f, -0.5f, 1.0f),
			new Vector4f( 0.5f,  0.5f, -0.5f, 1.0f),
			new Vector4f( 0.5f, -0.5f, -0.5f, 1.0f)
	};
	
	TextureMapper tmap = null;
	
	Vector2f[] textureBounds = {
			//first triangle
			new Vector2f(0,0),	
			new Vector2f(0.5f,0),	
			new Vector2f(0.5f,0.5f),
			//second triangle
			new Vector2f(0,0),	
			new Vector2f(0.5f,0.5f),
			new Vector2f(0,0.5f)
	};
	
	Vector2f[] textures = new Vector2f[numVertices]; 
	
	float rotation = 20.0f;
	float increment = 0.02f;
	int corner = 0;
	int stretch = 10;
	boolean dir = false;
	boolean morph = false;
	int count = 0;
	
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
		GL11.glClearColor(0, 0.4f, 0.9f, 0);
		GL11.glColor3f(1.0f, 0, 0);
		
		initCube();
		allocVBO();
	}

public void quad(int a, int b, int c, int d, int col){
		
		Vector3f aa = new Vector3f(vertices[a].x, vertices[a].y, vertices[a].z);
		Vector3f bb = new Vector3f(vertices[b].x, vertices[b].y, vertices[b].z);
		Vector3f cc = new Vector3f(vertices[c].x, vertices[c].y, vertices[c].z);
		
		//vector subtraction bb - aa, null constructs a new vector
		Vector3f u = Vector3f.sub(bb,aa,null);
		Vector3f v = Vector3f.sub(cc, bb, null);
		
		Vector3f norm = Vector3f.cross(u, v, null);
		
		textures[index] = textureBounds[index%6]; normals[index] = norm; points[index] = vertices[a]; colors[index] = palette[col]; index++;
		textures[index] = textureBounds[index%6]; normals[index] = norm; points[index] = vertices[b]; colors[index] = palette[col]; index++;
		textures[index] = textureBounds[index%6]; normals[index] = norm; points[index] = vertices[c]; colors[index] = palette[col]; index++;
		textures[index] = textureBounds[index%6]; normals[index] = norm; points[index] = vertices[a]; colors[index] = palette[col]; index++;
		textures[index] = textureBounds[index%6]; normals[index] = norm; points[index] = vertices[c]; colors[index] = palette[col]; index++;
		textures[index] = textureBounds[index%6]; normals[index] = norm; points[index] = vertices[d]; colors[index] = palette[col]; index++;
	}
	
	public void initCube(){
		quad(1,0,3,2,0);
		quad(2,3,7,6,1);
		quad(3,0,4,7,2);
		quad(6,5,1,2,3);
		quad(4,5,6,7,4);
		quad(5,4,0,1,5);

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

	public void allocVBO(){
		//outside JVM
		buffer = BufferUtils.createFloatBuffer(4 * numVertices + 3 * numVertices + 
				3 * numVertices + 2 * numVertices);
		
		
		
		for (int i = 0; i < numVertices; i++){
			buffer.put(points[i].x).put(points[i].y).put(points[i].z).put(points[i].w);
			buffer.put(colors[i].x).put(colors[i].y).put(colors[i].z);
			buffer.put(normals[i].x).put(normals[i].y).put(normals[i].z);
			buffer.put(textures[i].x).put(textures[i].y);	
		}
		
		buffer.flip();
		

		GL30.glBindVertexArray(0);
		
		GL20.glDisableVertexAttribArray(0);
		//Create VBO
		vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		int stride = 4 * sizeOfFloat + 3 * sizeOfFloat + 3 * sizeOfFloat + 2 * sizeOfFloat;
				
		//vertex buffer
		int offset = 0;
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glVertexPointer(4, GL11.GL_FLOAT, stride, offset);
		
		//color buffer
		offset += 4 * sizeOfFloat;
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glColorPointer(3, GL11.GL_FLOAT, stride, offset);
		
		//normal buffer
		offset += 3 * sizeOfFloat;
		GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glNormalPointer(GL11.GL_FLOAT, stride, offset);
		
		//texture buffer
		offset += 3 * sizeOfFloat;
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, stride, offset);
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	@Override
	protected void update(int delta) {
		if(alive){
			if(!morph){
				morph = true;
				corner = (int)(Math.random() * numVertices);
				dir = true;
			} else {
				if (dir){
					//interpolate outwards
					points[corner].x += increment;
					points[corner].y += increment;
					points[corner].z += increment;
					points[corner].w = 1;
					count++;
				}else {
					//interpolate inwards
					points[corner].x -= increment;
					points[corner].y -= increment;
					points[corner].z -= increment;
					points[corner].w = 1;
					count--;
				}
				if (count > stretch){
					dir = false;	
				}
				if(count < 0){
					morph = false;
				}
				
			}
			for (int i = 0; i < numVertices; i++){
				buffer.put(points[i].x).put(points[i].y).put(points[i].z).put(points[i].w);
				buffer.put(colors[i].x).put(colors[i].y).put(colors[i].z);
				buffer.put(normals[i].x).put(normals[i].y).put(normals[i].z);
				buffer.put(textures[i].x).put(textures[i].y);	
			}
			buffer.flip();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
			GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
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
		renderCube();
		GL11.glPopMatrix();
	}

	private void renderCube() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmap.getTextureID("metal"));
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, numVertices);
		
		
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
		AnimCube s = new AnimCube();
		s.start();
	}

}
