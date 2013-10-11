import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


public class Dragon {

	private Turtle turtle;
	private int distance;
	
	public Dragon(Turtle turtle){
		this.turtle = turtle;
		distance = 10;
	}
	
	public Dragon(Turtle turtle, int distance){
		this.turtle = turtle;
		this.distance = distance;
	}
	public void dragon(int n){
		if (n == 0){
			turtle.forward(distance);
			
		} else {
			dragon(n-1);
			turtle.right(90);
			nogard(n-1);
		}
	}
	
	public void nogard(int n){
		if (n == 0){
			turtle.forward(distance);
			
		} else {
			dragon(n-1);
			turtle.left(90);
			nogard(n-1);
		}
	}
	
	public static void main(String[] args){
		Turtle turtle = new Turtle();
		Dragon dragon = new Dragon(turtle, 5);
		turtle.init(350, 230, 0);
		turtle.pen(true);
		
		dragon.dragon(16);
	
		turtle.pen(false);
		turtle.show();
		
	}
	
	
}
