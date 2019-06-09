import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import org.opencv.core.Mat;
class displayimage{
	 public static BufferedImage Mat2BufferedImage(Mat m)
	    {
	        int type = BufferedImage.TYPE_BYTE_GRAY;
	        if (m.channels() > 1)
	        {
	            type = BufferedImage.TYPE_3BYTE_BGR;
	        }
	        int bufferSize = m.channels()*m.cols()*m.rows();
	        byte[] b = new byte[bufferSize];
	        m.get(0, 0, b); // get all the pixels
	        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
	        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	        System.arraycopy(b, 0, targetPixels, 0, b.length);  
	        return image;
	    }
public static void displayImage(Image img)
    {   
        ImageIcon icon = new ImageIcon(img);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());        
        frame.setSize(img.getWidth(null)+50, img.getHeight(null)+50);     
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
