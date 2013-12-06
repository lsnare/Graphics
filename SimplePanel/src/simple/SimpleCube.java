package simple;

import java.nio.FloatBuffer;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector4f;

import shader.InitShader;

public class SimpleCube extends SkelGL{

	int numVertices = 4;
	private Vector4f[] points = new Vector4f[numVertices];
	private Vector4f[] colors = new Vector4f[numVertices];
	int sizeOfFloat = Float.SIZE / Byte.SIZE;
	FloatBuffer rot = BufferUtils.createFloatBuffer(3 * numVertices);

	//buffer stuff
	int vboID;
	int shaderProgram;
	int vpos, cpos, rpos;
	
	//animation stuff
	float rotation = 0.0f;
	
	public static void main(String[] args) {
		SimplePanel2 panel = new SimplePanel2();
		panel.start();
	}

	public void initSquare(){
		
		points[0] = new Vector4f(-0.5f, 0.5f, 0.5f, 1);
		points[1] = new Vector4f( 0.5f, -0.5f, 0.5f, 1);
		points[2] = new Vector4f( 0.5f,  0.5f, 0.5f, 1);
		points[3] = new Vector4f(-0.5f,  0.5f, 0.5f, 1);
		

		colors[0] = new Vector4f(1, 0, 0, 1);
		colors[1] = new Vector4f(0, 1, 0, 0);
		colors[2] = new Vector4f(0, 0, 1, 0);
		colors[3] = new Vector4f(1, 1, 0, 0);
		
		
	}
	
	public void initGL(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float aspect = WIDTH / (float) HEIGHT;
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
		GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
 
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glClearColor(0, 0, 0, 0);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0, 0.4f, 0.9f, 0);
		GL11.glColor3f(1.0f, 0, 0);
		
		initSquare();
		allocVBO();
	}
	
	protected void allocVBO() {
		
		FloatBuffer vcBuffer = BufferUtils.createFloatBuffer(4 * numVertices + 4 * numVertices);
		
		for(int i = 0; i < numVertices; i++){
			vcBuffer.put(points[i].x).put(points[i].y).put(points[i].z).put(points[i].w);
			vcBuffer.put(colors[i].x).put(colors[i].y).put(colors[i].z).put(colors[i].w);
		}
		vcBuffer.flip();
		
		vboID = GL15.glGenBuffers();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vcBuffer, GL15.GL_STATIC_DRAW);
		
		//use shaders
		shaderProgram = InitShader.initProgram("GLSL/vshader2.glsl", "GLSL/shader1.glsl");
		
		//use this program
		ARBShaderObjects.glUseProgramObjectARB(shaderProgram);
		
		int stride = 4 * numVertices + 4 * numVertices;
		int offset = 0;
		
		//enable vertex processing
		vpos = GL20.glGetAttribLocation(shaderProgram, "vPosition");
		GL20.glEnableVertexAttribArray(vpos);
		GL20.glVertexAttribPointer(vpos, 4, GL11.GL_FLOAT, false, stride, offset);
		
		offset += 4 * numVertices;
		cpos = GL20.glGetAttribLocation(shaderProgram, "vColor");
		GL20.glEnableVertexAttribArray(cpos);
		GL20.glVertexAttribPointer(cpos, 4, GL11.GL_FLOAT, false, stride, offset);
		
		rpos = GL20.glGetUniformLocation(shaderProgram, "theta");
		rot.put(rotation).put(rotation).put(rotation);
		rot.flip();
		GL20.glUniform3(rpos, rot);
		
	}

	@Override
	protected void update(int delta) {
		if(animate){
			rotation += 0.0015f * delta;
			FloatBuffer rot = BufferUtils.createFloatBuffer(3 * numVertices);
			rot.put(rotation).put(rotation).put(rotation);
			rot.flip();
			GL20.glUniform3(rpos, rot);
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
		
		renderSquare();
		
	}

	private void renderSquare() {
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_QUADS, 0, numVertices);
		
	}

	@Override
	protected void initTextures() {
		// TODO Auto-generated method stub
		
	}

}
