import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class TextureExample {

	public Texture texture;
	
	public void start(){
		initGL(800,600);
		init();
		
		while(true){
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			render();
			Display.update();
			Display.sync(100);
			if (Display.isCloseRequested()){
				Display.destroy();
				System.exit(0);
			}
		}
	}
	
	public void initGL(int width, int height){
		try{
			Display.setDisplayMode(new DisplayMode(width,height));
			Display.create();
			Display.setVSyncEnabled(true);
		}catch(LWJGLException e){
			System.exit(0);
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0, 0, 0, 0);
		
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glViewport(0, 0, width, height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, 0, height, 0, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
	}
	
	public void init(){
		try{
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("firefox-512.png"));
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void render(){
		texture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(100,100);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(100+texture.getTextureWidth(),100);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(100+texture.getTextureWidth(),100+texture.getTextureHeight());
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(100,100+texture.getTextureHeight());
		GL11.glEnd();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TextureExample tex = new TextureExample();
		tex.start();
	}

}
