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
	
	public void cathedral(int n, int angle){
		if (n==0){
			turtle.forward(distance);
		} else {
			for(int i = 0; i < n; i++){
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			turtle.left(-(angle*2));
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			}
		}
		
	}
	
	public void koch(int n, int angle){
		if (n==0){
			turtle.forward(distance);
		} else {
			
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			turtle.left(-(angle*2));
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			
		}
		
	}
	
	public void koch2(int n, int angle){
		if (n==0){
			turtle.forward(distance);
		} else {
			
			koch(n-1,angle);
			turtle.right(angle);
			koch(n-1,angle);
			turtle.left(-(angle*2));
			koch(n-1,angle);
			turtle.left(angle);
			koch(n-1,angle);
			
		}
		
	}
	
	public void wada(int n){
		if (n==0){
			turtle.forward(distance);
			n++;
		} else {
			wada(n-1);
			turtle.left(80);
			wada(n-1);
			turtle.left(-160);
			wada(n-1);
			turtle.left(80);
			wada(n-1);
		}
		
	}
	
	public void tree(int branchlen){
		if(branchlen>5){
			turtle.forward(distance);
			turtle.right(20);
			tree(branchlen-1);
			turtle.left(20);
			turtle.forward(distance);
			tree(branchlen-15);
		}
	}
	
	public static void main(String[] args){
		Turtle turtle = new Turtle();
		Dragon dragon = new Dragon(turtle, 5);
		turtle.init(200, 400, 0);
		turtle.pen(true);
		
		//dragon.dragon(16);
		//dragon.koch2(5,60);
		dragon.tree(50);
		/*dragon.koch(4);
		turtle.left(-120);
		dragon.koch(4);
		turtle.left(-120);
		dragon.koch(4);
		*/
		
		/*dragon.wada(5);
		turtle.left(-160);
		dragon.wada(5);
		turtle.left(-160);
		*/
		turtle.pen(false);
		turtle.show();
		
	}
	
	
}
