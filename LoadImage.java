import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.log4j.Layout;

public class LoadImage extends JFrame{
	public LoadImage() {
        setTitle(" hello world ");
        addWindowListener(new WindowAdapter(){
           
            public void Windowclosing(WindowEvent e){
                dispose();
            }       
        });
    }
    public LoadImage(Image img){
        this();
        setSize(img.getWidth(null),img.getHeight(null));
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        System.out.println("hello");
        ImageIcon icon=new ImageIcon(img);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        //JButton extractbtn=new JButton("Extract");
        //extractbtn.setSize(10, 20);
        add(lbl);
        //add(extractbtn);
    }
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoadImage().setVisible(true);
            }
        });
    }
}
