//import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
//import java.awt.image.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
//import org.opencv.videoio.Videoio;

import utilities.CommomOperations;
	
class Test implements Runnable{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat frame = new Mat();
		VideoCapture camera = new VideoCapture();
		camera.open("D:\\test.mp4");
		if (camera.isOpened())
			System.out.println("successfull");
		else
			System.out.println("unsuccesfull");
		JFrame jframe = new JFrame("MyTitle");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel vidpanel = new JLabel();
		jframe.setContentPane(vidpanel);
		jframe.setSize(2000, 4000);
		jframe.setVisible(true);
		double count = 0;
		int i = 1;
		while (true) {
			if (camera.read(frame)) {

				ImageIcon image = new ImageIcon(CommomOperations.MatToBuffered(frame));
				vidpanel.setIcon(image);
				try {
					Thread.sleep(7);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				vidpanel.repaint();
				count++;
				if (count % 30 == 0) {
					try {
						ImageIO.write(CommomOperations.MatToBuffered(frame), "jpg", new File("D:\\" + i + ".jpg"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}

			}
		}
	}
	public static void main(String[] args) throws InterruptedException, IOException {
		Thread t=new Thread(new Test());
		t.start();
	}
}
