
	import org.lwjgl.LWJGLException;
	import org.lwjgl.input.Keyboard;
	import org.lwjgl.input.Mouse;
	import org.lwjgl.opengl.Display;
	import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

	public class Blocks {

		/**
		 * @param args
		 */
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			Blocks display = new Blocks();
			display.show();
		}

		private void show() {
			// TODO Auto-generated method stub
			try{
				Display.setDisplayMode(new DisplayMode(800,600));
				Display.create();
			}catch(LWJGLException e){
				e.getStackTrace();
				System.exit(0);
			}
			
			GL11.glMatrixMode(GL11.GL_PROJECTION); //applies to projection methods
			GL11.glLoadIdentity(); //clears settings
			GL11.glOrtho(0, 800, 0, 600, 1, -1); //orthographic projection
			GL11.glMatrixMode(GL11.GL_MODELVIEW); 
			
			try {
				Keyboard.create();
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//take repeated events when key is held down
			Keyboard.enableRepeatEvents(true);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT); //clear buffers
			GL11.glColor3f(1.0f, 0.0f, 0.0f); 
			GL11.glBegin(GL11.GL_QUADS); //draw a quad out of following vertices
			GL11.glVertex2f(100, 100);
			GL11.glVertex2f(100+100, 100);
			GL11.glVertex2f(100+100, 100+100);
			GL11.glVertex2f(100, 100+100);
			GL11.glEnd();
			while (!Display.isCloseRequested()){
				grabBlock();
				//blockFall();
				
				Display.update();
			}
			
			Display.destroy();
		}

		private void blockFall() {
			// TODO Auto-generated method stub
			int x = Mouse.getX();
			int y = Mouse.getY();
			
			while(!Mouse.isButtonDown(0) && y > -(Display.getHeight())){
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT); //clear buffers
				
				GL11.glColor3f(1.0f, 0.0f, 0.0f); 
				GL11.glBegin(GL11.GL_QUADS); //draw a quad out of following vertices
				GL11.glVertex2f(x,y);
				GL11.glVertex2f(x+100, y);
				GL11.glVertex2f(x+100, y+100);
				GL11.glVertex2f(x, y+100);
				GL11.glEnd();
				
				y--;
				
			}
			
			
		}

		private void grabBlock() {
			// TODO Auto-generated method stub
			if (Mouse.isButtonDown(0)){
				int x = Mouse.getX();
				int y = Mouse.getY();
				
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT| GL11.GL_DEPTH_BUFFER_BIT); //clear buffers
				
				GL11.glColor3f(1.0f, 0.0f, 0.0f); 
				GL11.glBegin(GL11.GL_QUADS); //draw a quad out of following vertices
				GL11.glVertex2f(x,y);
				GL11.glVertex2f(x+100, y);
				GL11.glVertex2f(x+100, y+100);
				GL11.glVertex2f(x, y+100);
				GL11.glEnd();
			}
			
			while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if(Keyboard.getEventKey() == Keyboard.KEY_W){
					System.out.println("W was pressed");
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_A){
					System.out.println("A was pressed");
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_S){
					System.out.println("S was pressed");
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_D){
					System.out.println("D was pressed");
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE){
					System.exit(0);
				}
			}
			}
		}

	}
