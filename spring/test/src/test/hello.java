package test;
import java.awt.Dialog;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
public class hello {
	
	double length=0.8;
	public double acc=0.4;
	double dcc=1.255;


		  public static void main(String[] args) {
			  hello h=new hello();
			  h.acc = h.acc*3.6;
			  
			  System.out.format(String.format("%.4f\n",h.acc) );
			  
			  System.out.format(String.format("%.2f\n",h.dcc));
			  
			  System.out.format(String.format("%.2f\n",h.length));

			  }

}