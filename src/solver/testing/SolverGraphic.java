package solver.testing;

import java.awt.Panel;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import solver.testing.Gears.GearsKeyAdapter;
import solver.testing.Gears.GearsMouseAdapter;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.awt.AWTKeyAdapter;
import com.jogamp.newt.event.awt.AWTMouseAdapter;
import com.jogamp.opengl.util.Animator;

public class SolverGraphic implements GLEventListener{
	private float view_rotx = 20.0f, view_roty = 30.0f, view_rotz = 0.0f;
	private float angle = 0.0f;
	private int swapInterval;
	private int cube1;
	private int cubeFrame1;
	
    static float pos[] = { 5.0f, 5.0f, 10.0f, 0.0f };
    static float red[] = { 0.8f, 0.1f, 0.0f, 0.7f };
    static float green[] = { 0.0f, 0.8f, 0.2f, 1.0f };
    static float blue[] = { 0.2f, 0.2f, 1.0f, 1.0f };	 
    
	private boolean mouseRButtonDown = false;
	private int prevMouseX, prevMouseY;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame("Drawing Test");
		final JSlider slider = new JSlider(JSlider.HORIZONTAL,0,30,15);
		frame.setSize(600,600);
		
		final Animator animator = new Animator();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e){
				new Thread(new Runnable(){
					public void run(){
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
					System.out.println(slider.getValue());
				
			}
			
		});
		
		GLCanvas canvas = new GLCanvas();
		animator.add(canvas);
		
		final SolverGraphic drawingTest = new SolverGraphic();
		canvas.addGLEventListener(drawingTest);
		
		// do layout stuff
		GroupLayout layout = new GroupLayout(frame.getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup()
					.addComponent(slider)
					.addComponent(canvas));
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(slider)
					.addComponent(canvas));
		frame.getContentPane().setLayout(layout);

		frame.validate();
		frame.setVisible(true);
		
		// start animator
		animator.start();
	}
	
	public SolverGraphic(int swapInterval){
		this.swapInterval = swapInterval;
	}
	
	public SolverGraphic(){
		this(1);
	}
	
	public void init(GLAutoDrawable drawable){
		System.out.println("Drawing Test: Init: " + drawable);
		
	    GL2 gl = drawable.getGL().getGL2();

	
	    
	    //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT_AND_DIFFUSE, pos, 0);
	    gl.glEnable(GL2.GL_CULL_FACE);
	    //gl.glEnable(GL2.GL_LIGHTING);
	    //gl.glEnable(GL2.GL_LIGHT0);
	    //gl.glEnable(GL2.GL_AMBIENT);
	    gl.glEnable(GL2.GL_LINE_SMOOTH);
	    gl.glEnable(GL2.GL_BLEND);
	    gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glEnable(GL2.GL_DEPTH_TEST);
    	gl.glPolygonOffset(-1.0f, -1.0f);
    	gl.glLineWidth(2.0f);
    	gl.glEnable(GL2.GL_POLYGON_OFFSET_LINE);

	    if(0>= cube1){
		    cube1 = gl.glGenLists(1);
		    gl.glNewList(cube1, GL2.GL_COMPILE);
		    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, green,0);
		    cube(gl,1);
		    gl.glEndList();
		    
		    cubeFrame1 = gl.glGenLists(2);
		    gl.glNewList(cubeFrame1, GL2.GL_COMPILE);
		    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, blue,0);
		    cubeFrame(gl,1);
		    gl.glEndList();
		    
		    System.out.println("cube1 list created: " + cube1);
	    } else{
	    	System.out.println("cube1 list reused: " + cube1);
	    }
	    
	    gl.glEnable(GL2.GL_NORMALIZE);
	    
	    // MouseListener mouse = new TraceMouseAdapter(new GearsMouseAdapter());
	    MouseListener mouse = new TestMouseAdapter();    
	    KeyListener keys = new TestKeyAdapter();

	    if (drawable instanceof Window) {
	        Window window = (Window) drawable;
	        window.addMouseListener(mouse);
	        window.addKeyListener(keys);
	    } else if (GLProfile.isAWTAvailable() && drawable instanceof java.awt.Component) {
	    	java.awt.Component comp = (java.awt.Component) drawable;
	    	new AWTMouseAdapter(mouse).addTo(comp);
	    	new AWTKeyAdapter(keys).addTo(comp);
	    }
	}

	  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		    System.out.println("Reshape "+x+"/"+y+" "+width+"x"+height);
		    GL2 gl = drawable.getGL().getGL2();

		    gl.setSwapInterval(swapInterval);

		    float h = (float)height / (float)width;
		            
		    gl.glMatrixMode(GL2.GL_PROJECTION);

		    gl.glLoadIdentity();
		    gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
		    gl.glMatrixMode(GL2.GL_MODELVIEW);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.0f, 0.0f, -40.0f);
		  }

		  public void dispose(GLAutoDrawable drawable) {
		    System.out.println("Dispose");
		    cube1 = 0;
		    cubeFrame1 = 0;
		  }

		  public void display(GLAutoDrawable drawable) {
		    // Turn the gears' teeth
		    //angle += 2.0f;

		    // Get the GL corresponding to the drawable we are animating
		    GL2 gl = drawable.getGL().getGL2();

		    gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		    // Special handling for the case where the GLJPanel is translucent
		    // and wants to be composited with other Java 2D content
		    if (GLProfile.isAWTAvailable() && 
		        (drawable instanceof javax.media.opengl.awt.GLJPanel) &&
		        !((javax.media.opengl.awt.GLJPanel) drawable).isOpaque() &&
		        ((javax.media.opengl.awt.GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
		      gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		    } else {
		      gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		    }
		            
		    // Rotate the entire assembly of gears based on how the user
		    // dragged the mouse around
		    gl.glPushMatrix();
		    gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
		    gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);
		            
		    // Place the first gear and call its display list
		    gl.glPushMatrix();
		    //gl.glTranslatef(-3.0f, -2.0f, 0.0f);
		    gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		    gl.glCallList(cube1);
		    gl.glCallList(cubeFrame1);
		    gl.glPopMatrix();
		    
		    gl.glPushMatrix();
		    gl.glTranslatef(-2.0f, -2.0f, 0.0f);
		    gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		    gl.glCallList(cube1);
		    gl.glCallList(cubeFrame1);
		    gl.glPopMatrix();
		            
		    // Remember that every push needs a pop; this one is paired with
		    // rotating the entire gear assembly
		    gl.glPopMatrix();
		  }
    public static void cube(GL2 gl, float size){
    	gl.glShadeModel(GL2.GL_FLAT);
    	gl.glNormal3f(0.0f, 0.0f, 1.0f);

    	gl.glBegin(GL2.GL_QUADS);

    	gl.glColor3fv(blue, 0);
    	gl.glVertex3f(-1.0f, -1.0f, 1.0f);
    	gl.glVertex3f(1.0f, -1.0f, 1.0f);
    	gl.glVertex3f(1.0f, 1.0f, 1.0f);
    	gl.glVertex3f(-1.0f, 1.0f, 1.0f);
    	
    	gl.glVertex3f(1.0f,-1.0f, 1.0f);
    	gl.glVertex3f(1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(1.0f,1.0f, -1.0f);
    	gl.glVertex3f(1.0f,1.0f, 1.0f);
    	
    	gl.glVertex3f(-1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(-1.0f,-1.0f, 1.0f);
    	gl.glVertex3f(-1.0f,1.0f, 1.0f);
    	gl.glVertex3f(-1.0f,1.0f, -1.0f);
 
    	gl.glVertex3f(1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(-1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(-1.0f,1.0f, -1.0f);
    	gl.glVertex3f(1.0f,1.0f, -1.0f);
    	
    	gl.glVertex3f(-1.0f, 1.0f, -1.0f);
    	gl.glVertex3f(-1.0f, 1.0f, 1.0f);
    	gl.glVertex3f(1.0f, 1.0f, 1.0f);
    	gl.glVertex3f(1.0f,1.0f, -1.0f);

    	gl.glVertex3f(-1.0f, -1.0f, 1.0f);
    	gl.glVertex3f(-1.0f, -1.0f, -1.0f);
    	gl.glVertex3f(1.0f, -1.0f, -1.0f);
    	gl.glVertex3f(1.0f,-1.0f, 1.0f);
    	
    	gl.glEnd();
   
    }
    
    public static void cubeFrame(GL2 gl, float size){
    	
    	gl.glBegin(GL2.GL_LINE_LOOP);
    	gl.glColor3f(0,0,0);
    	
    	// front
    	gl.glVertex3f(-1.0f, -1.0f, 1.0f);
    	gl.glVertex3f(1.0f, -1.0f, 1.0f);
    	gl.glVertex3f(1.0f, 1.0f, 1.0f);
    	gl.glVertex3f(-1.0f, 1.0f, 1.0f);
    	gl.glEnd();
    	
    	// back
    	gl.glBegin(GL2.GL_LINE_LOOP);
    	gl.glVertex3f(-1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(1.0f,1.0f, -1.0f);
    	gl.glVertex3f(-1.0f,1.0f, -1.0f);
    	gl.glEnd();

    	// left side
    	gl.glBegin(GL2.GL_LINE_LOOP);
    	gl.glVertex3f(-1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(-1.0f,-1.0f, 1.0f);
    	gl.glVertex3f(-1.0f,1.0f, 1.0f);
    	gl.glVertex3f(-1.0f,1.0f, -1.0f);
    	gl.glEnd();

    	// right side
    	gl.glBegin(GL2.GL_LINE_LOOP);
    	gl.glVertex3f(1.0f,-1.0f, -1.0f);
    	gl.glVertex3f(1.0f,-1.0f, 1.0f);
    	gl.glVertex3f(1.0f,1.0f, 1.0f);
    	gl.glVertex3f(1.0f,1.0f, -1.0f);
    	
    	gl.glEnd();
    	
    	
    }
    
	class TestKeyAdapter extends KeyAdapter {      
		public void keyPressed(KeyEvent e) {
			int kc = e.getKeyCode();
			if(KeyEvent.VK_LEFT == kc) {
				view_roty -= 1;
			} else if(KeyEvent.VK_RIGHT == kc) {
				view_roty += 1;
			} else if(KeyEvent.VK_UP == kc) {
				view_rotx -= 1;
			} else if(KeyEvent.VK_DOWN == kc) {
				view_rotx += 1;
			}
		}
	}

	class TestMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			prevMouseX = e.getX();
			prevMouseY = e.getY();
			if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
				mouseRButtonDown = true;
			}
		}

		public void mouseReleased(MouseEvent e) {
			if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
				mouseRButtonDown = false;
			}
		}

		public void mouseDragged(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int width=0, height=0;
			Object source = e.getSource();
			if(source instanceof Window) {
				Window window = (Window) source;
				width=window.getWidth();
				height=window.getHeight();
			} else if (GLProfile.isAWTAvailable() && source instanceof java.awt.Component) {
				java.awt.Component comp = (java.awt.Component) source;
				width=comp.getWidth();
				height=comp.getHeight();
			} else {
				throw new RuntimeException("Event source neither Window nor Component: "+source);
			}
			float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)width);
			float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)height);

			prevMouseX = x;
			prevMouseY = y;

			view_rotx += thetaX;
			view_roty += thetaY;
		}
	}

}
