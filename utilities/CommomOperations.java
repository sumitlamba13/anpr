package utilities;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import org.opencv.core.Mat;

public class CommomOperations {
	public static void openInternalFrame(JDesktopPane deskpane,JInternalFrame jif){
	    jif.setVisible(true);
	        deskpane.add(jif);
	        try{
	            jif.setSelected(true);
	        }catch(PropertyVetoException ex){
	        }
	}
	public static BufferedImage MatToBuffered(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}
}
