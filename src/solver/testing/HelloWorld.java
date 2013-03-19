package solver.testing;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;

public class HelloWorld {
	   public static void main (String args[]) {
		      try {
		         System.loadLibrary("jogl");
		         System.out.println(
		            "Hello World! (The native libraries are installed.)"
		         );
		        GLCapabilities caps = new GLCapabilities(null);
		        System.out.println(
		            "Hello JOGL! (The jar appears to be available.)"
		         );
		      } catch (Exception e) {
		         System.out.println(e);
		      }
		   
		}  

}
