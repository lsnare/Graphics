
package snowflake;

import org.eclipse.swt.graphics.Rectangle;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

public class Snowflake extends SkelGL {
 
private int numTimesToSubdivide = 5;
private int numFlakes = (int)Math.pow(4, numTimesToSubdivide);
private int numVertices = 3 * numFlakes;
 
private class KochSet{
Vector2f l = null;
Vector2f m = null;
Vector2f n = null;
Vector2f o = null;
Vector2f p = null;
}
 
double deg60 = 60.0f * Math.PI / 180.0;
 
private int index = 0;
 
Vector2f[] point = new Vector2f[numVertices];
 
public KochSet computeKochSet(Vector2f s, Vector2f d){
KochSet k = new KochSet();
k.l = s;
k.m = new Vector2f(s.x + (d.x - s.x)/3.0f, s.y + (d.y - s.y)/3.0f);
k.o = new Vector2f(s.x + (d.x - s.x)*2/3.0f, s.y + (d.y - s.y)*2/3.0f);
float px = (float)(Math.cos(deg60) * (k.o.x - k.m.x) -  Math.sin(deg60) * (k.o.y - k.m.y)+ k.m.x);
float py = (float)(Math.sin(deg60) * (k.o.x - k.m.x) +  Math.cos(deg60) * (k.o.y - k.m.y)+ k.m.y);
k.n = new Vector2f(px, py);
k.p = d;
return k;
}
 
public void divideSide(Vector2f a, Vector2f b, int count){
 
if (count > 0){
KochSet kab = computeKochSet(a, b);
 
divideSide(kab.l, kab.m, count -1);
divideSide(kab.m, kab.n, count -1);
divideSide(kab.n, kab.o, count -1);
divideSide(kab.o, kab.p, count -1);
} else {
point[index++] = a;
}
}
 
public void divideTriangle(Vector2f a, Vector2f b, Vector2f c, int count){
divideSide(a, b, count);
divideSide(b, c, count);
divideSide(c, a, count);
}
 
public void initSnowFlake(){
Vector2f[] vertices = { 
new Vector2f(-0.6f, -0.6f),
new Vector2f(0f, 0.6f),
new Vector2f(0.6f, -0.6f),
};
index = 0;
divideTriangle(vertices[0], vertices[1], vertices[2], numTimesToSubdivide);
}
/**
* @param args
*/
public static void main(String[] args) {
Snowflake snf = new Snowflake();
snf.start();
}

@Override
protected void initGL() {
try {
GLContext.useContext(canvas);
 
} catch (LWJGLException e){
e.printStackTrace();
}
GL11.glMatrixMode(GL11.GL_PROJECTION);
GL11.glLoadIdentity();
 
float aspect = WIDTH/ (float) HEIGHT;
GL11.glViewport(0, 0, WIDTH, HEIGHT);
GLU.gluPerspective(fovy, aspect, zNear, zFar);
GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
 
GL11.glClearColor(0, 0, 0, 0);
GL11.glClearDepth(1.0);
GL11.glEnable(GL11.GL_DEPTH_TEST);
 
initSnowFlake();
}

@Override
protected void update(int delta) {
updateFPS();
}

@Override
protected void resetGL() {
 
GL11.glMatrixMode(GL11.GL_PROJECTION);
GL11.glLoadIdentity();
 
Rectangle bounds = canvas.getBounds();
float aspect = WIDTH/ (float) HEIGHT;
 
GL11.glViewport(0, 0, bounds.width, bounds.height);
GLU.gluPerspective(fovy, aspect, zNear, zFar);
GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
 
GL11.glMatrixMode(GL11.GL_MODELVIEW);
GL11.glLoadIdentity();
}

@Override
protected void renderGL() {
GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
GL11.glMatrixMode(GL11.GL_PROJECTION);
GL11.glLoadIdentity();
 
Rectangle bounds = canvas.getBounds();
 
float aspect = WIDTH/ (float) HEIGHT;
GL11.glViewport(0, 0, bounds.width, bounds.height);
GLU.gluPerspective(fovy, aspect, zNear, zFar);
GLU.gluLookAt(eyex, eyey, eyez, 0, 0, 0, 0, 1, 0);
 
GL11.glMatrixMode(GL11.GL_MODELVIEW);
GL11.glPushMatrix();
 
GL11.glColor3f(0, 1, 1);
GL11.glLineWidth(3.0f);
for (int i = 0; i < 180; i++){
renderSnowFlake();
GL11.glTranslatef(0, 0, -1);
}
GL11.glPopMatrix();
 
// setup projection
}

private void renderSnowFlake() {
GL11.glBegin(GL11.GL_LINE_LOOP);
for (int i = 0; i < numVertices; i++){
GL11.glVertex3f(point[i].x, point[i].y, 0.25f);
}
GL11.glEnd();
}

@Override
protected void initTextures() {
// TODO Auto-generated method stub
 
}
}
