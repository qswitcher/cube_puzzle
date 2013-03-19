package solver.ui;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import solver.math.Point;
import solver.solutionpath.SolutionPath;

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
	private int snakeType;
	private int[] cubes;
	private JFrame frame;
	private final Animator animator;
	private final JSlider slider;
	private final JButton button;
	private final JTextField solutionTextbox;

	static float pos[] = { 5.0f, 5.0f, 10.0f, 0.0f };
	static float red[] = { 0.8f, 0.1f, 0.0f, 0.7f };
	static float green[] = { 0.0f, 0.8f, 0.2f, 1.0f };
	static float blue[] = { 0.2f, 0.2f, 1.0f, 1.0f };	 

	private boolean mouseRButtonDown = false;
	private int prevMouseX, prevMouseY;
	
	// solutions
	private ArrayList<List<Point>> solutions;
	private List<Point> currentSolution;
	private int numberOfPointsToDraw;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SolverGraphic solverUI = new SolverGraphic(3);
		solverUI.setVisible(true);
		solverUI.start();
	}

	/**
	 * Creates a new SolverGraphic instance
	 * @param swapInterval
	 */
	public SolverGraphic(int snakeType){
		// instantiate stuff
		this.swapInterval = 1;
		this.snakeType = snakeType;
		frame = new JFrame("Drawing Test");
		slider = new JSlider(JSlider.HORIZONTAL,0,30,15);
		frame.setSize(600,600);
		button = new JButton("Next Solution");
		solutionTextbox = new JTextField();

		animator = new Animator();
		GLCanvas canvas = new GLCanvas();
		canvas.addGLEventListener(this);
		animator.add(canvas);
		
		// setup Listeners
		setupListeners();

		// do layout stuff
		doLayout(canvas);	
	}

	public void setSolutions(Set<SolutionPath> solutions){
		this.solutions = new ArrayList<List<Point>>();
		for(SolutionPath solution: solutions){
			this.solutions.add(solution.getPoints());
		}
		if(this.solutions.size() > 0){
			this.currentSolution = this.solutions.get(0);
			updateDisplayedSolution();
		}
		
		// set slider points appropriately
		this.slider.setMinimum(0);
		this.slider.setMaximum(this.currentSolution.size());
	}
	
	private void updateDisplayedSolution(){
		String displayText = "";
		for(Point point: this.currentSolution){
			if(this.currentSolution.indexOf(point) == 0){
				displayText += point.getDisplayString();
			} else{
				displayText += "-" + point.getDisplayString();
			}
		}
		solutionTextbox.setText(displayText);		
	}
	
	private void nextSolution(){
		int currentIndex = this.solutions.indexOf(this.currentSolution);
		this.currentSolution = this.solutions.get(++currentIndex % this.solutions.size());
		updateDisplayedSolution();
	}

	/**
	 * Creates the WindowListener for GL Events as well as a listerner
	 * for the slider
	 */
	private void setupListeners(){
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
				numberOfPointsToDraw = slider.getValue();
			}

		});		
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextSolution();
			}
		});
	}
	
	/**
	 * Does the layout work for the canvas and the slider
	 * @param canvas
	 */
	private void doLayout(GLCanvas canvas){
		// do layout stuff
		GroupLayout layout = new GroupLayout(frame.getContentPane());
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addComponent(slider)
				.addGroup(layout.createSequentialGroup()
						.addComponent(button)
						.addComponent(solutionTextbox))
				.addComponent(canvas));
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addComponent(slider)
				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(button)
						.addComponent(solutionTextbox))
				.addComponent(canvas));
		frame.getContentPane().setLayout(layout);

		frame.validate();		
	}
	
	public void setVisible(boolean flag){
		frame.setVisible(flag);
	}
	
	public void start(){
		animator.start();
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

		int snakeLength = snakeType*snakeType*snakeType;
		cubes = new int[snakeLength];
		for(int i = 0; i < snakeLength; i++){
			cubes[i] = gl.glGenLists(i+1);
			gl.glNewList(cubes[i], GL2.GL_COMPILE);
			//gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, green,0);
			cube(gl, i/((float) snakeLength));
			gl.glEndList();
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
		cubes = null;
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

		int offset = snakeType/2;
		for(int i = 0; i < numberOfPointsToDraw; i++){
			Point point = this.currentSolution.get(i);
			gl.glPushMatrix();
			gl.glTranslatef(-2.0f*(point.getX() - offset), -2.0f*(point.getY() - offset), -2.0f*(point.getZ() - offset));
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glCallList(cubes[i]);
			gl.glPopMatrix();			
		}
		
		// Remember that every push needs a pop; this one is paired with
		// rotating the entire assembly
		gl.glPopMatrix();
	}
	public static void cube(GL2 gl, float lightness){
		gl.glShadeModel(GL2.GL_FLAT);
		gl.glNormal3f(0.0f, 0.0f, 1.0f);

		gl.glBegin(GL2.GL_QUADS);

		float blue[] = { 0.0f + lightness, 0.0f + lightness, 1.0f, 1.0f };	 
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
		
		// draw the black wire frame
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
