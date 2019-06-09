import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ListIterator;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import dao.CarInfoDao;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import to.CarInfoTo;
import utilities.CommomOperations;

public class Imageinternalframe extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jtfplatenumber;
	private JTextField jtfdate;
	private JTextField jtftime;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Imageinternalframe frame = new Imageinternalframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public Imageinternalframe() {
		setIconifiable(true);
		setClosable(true);
		setTitle("Extract Image");
		setLocation(200,70);
		JDesktopPane imagedeskpane = new JDesktopPane();
		imagedeskpane.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(imagedeskpane, BorderLayout.CENTER);
		JFileChooser jfc=new JFileChooser();
		JButton btnSelect = new JButton("Select Image");
		btnSelect.setFont(new Font("Times New Roman", Font.BOLD, 18));
		JLabel lblimage = new JLabel("");
		lblimage.setBounds(23, 141, 567, 497);
		imagedeskpane.add(lblimage);
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				getContentPane().add(jfc);
				int result = jfc.showOpenDialog(imagedeskpane);
				if (result == JFileChooser.APPROVE_OPTION) {
					try {
						boolean allvalid = true;
						String message = "";
						String path = jfc.getSelectedFile().getAbsolutePath().trim();
						File file = new File(path);
						if (file.exists()) {
							String allowed_extensions = "jpg,png,gif,jpeg";
							int index = path.lastIndexOf(".");
							if (index != -1) {
								String extension = path.substring(index + 1).toLowerCase();
								if (!allowed_extensions.contains(extension)) {
									message += "You can only choose a png, jpg or jpeg file.\n\n";
									allvalid = false;
								}
							} else {
								message += "Not Valid Path for Image File\n\n";
								allvalid = false;
							}
						} else {
							message += "There is No Such File Present in Hard Disk\n\n";
							allvalid = false;
						}
						if (allvalid) {
							FileInputStream fis = new FileInputStream(jfc.getSelectedFile().getAbsolutePath());
							InputStream is = fis;
							BufferedImage img = ImageIO.read(is);
							Image dimg;
							dimg = img.getScaledInstance(lblimage.getWidth(), lblimage.getHeight(), Image.SCALE_SMOOTH);
							ImageIcon icon = new ImageIcon(dimg);
							lblimage.setIcon(icon);
						} else {
							JOptionPane.showMessageDialog(imagedeskpane, message);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSelect.setBounds(48, 30, 155, 35);
		imagedeskpane.add(btnSelect);
		
		JLabel plateno = new JLabel("Plate Number");
		plateno.setFont(new Font("Times New Roman", Font.BOLD, 18));
		plateno.setBounds(619, 100, 121, 35);
		imagedeskpane.add(plateno);
		
		JButton btnExtract = new JButton("Extract Image");
		btnExtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				String inputfile = jfc.getSelectedFile().getAbsolutePath();
				Mat img = Imgcodecs.imread(inputfile);
//				Size size = img.size();
//			    Core.bitwise_not(img, img);
//				Mat lines = new Mat();
//			    Imgproc.HoughLinesP(img, lines, 1, Math.PI / 180, 100, size.width / 2.f, 20);
//			    double angle = 0.;
//			    for(int i = 0; i<lines.height(); i++){
//			        for(int j = 0; j<lines.width();j++){
//			            angle += Math.atan2(lines.get(i, j)[3] - lines.get(i, j)[1], lines.get(i, j)[2] - lines.get(i, j)[0]);
//			        }
//			    }
//			    angle /= lines.size().area();
//			    angle = angle * 180 / Math.PI;
//			    img=deskew(img,angle);
//				LoadImage frame2=new LoadImage(MatToBuffered(img));
//				frame2.setVisible(true);
				Mat grey = new Mat();
				Imgproc.resize(img, img, new Size(640, 380));
				Imgproc.GaussianBlur(img, grey, new Size(5, 5), 0);
				Imgproc.cvtColor(img, grey, Imgproc.COLOR_BGR2GRAY);
				Mat gsTopHat = new Mat();
				Mat gsEnhance = new Mat();
				Mat dst = new Mat();

//				// *****************************************ENHANCING************************************************//
				Mat structure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
				Mat topHat = new Mat();
				Mat blackHat = new Mat();

				Imgproc.morphologyEx(grey, topHat, Imgproc.MORPH_TOPHAT, structure);
				Imgproc.morphologyEx(grey, blackHat, Imgproc.MORPH_BLACKHAT, structure);
				Core.add(grey, topHat, gsTopHat);
				Core.subtract(gsTopHat, blackHat, gsEnhance);
//				// ************************************************************************//

				Imgproc.Sobel(gsEnhance, dst, CvType.CV_8U, 1, 0);
				Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
//				// ****************************ExtractingContours*******************************//

				Mat struc = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3));
				Mat copy = new Mat();
				copy = dst.clone();
				Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, struc);
				ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

//				// **************************************Clean and Read
				// Plate*****contours*
				for (int i = 0; i < contours.size(); i++) {
					System.out.println(Imgproc.contourArea(contours.get(i)));
					if (Imgproc.contourArea(contours.get(i)) > 50) {
						Rect rect = Imgproc.boundingRect(contours.get(i));
						System.out.println(rect.height);
						if (rect.height > 28 && rect.height < rect.width) {
							Imgproc.rectangle(img, new Point(rect.x, rect.y),
									new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));
						}
					}
				}
				ArrayList<RotatedRect> rects = new ArrayList<RotatedRect>();
				ListIterator<MatOfPoint> itc = contours.listIterator();
				Mat crop_img = new Mat();
				while (itc.hasNext()) {
					try {
						MatOfPoint2f mp2f = new MatOfPoint2f(itc.next().toArray());
						RotatedRect mr = Imgproc.minAreaRect(mp2f);
						double area = Math.abs(Imgproc.contourArea(mp2f));
						Point[] pt = new Point[4];
						double bbArea = mr.size.area();
						double ratio = area / bbArea;
						if ((ratio < 0.35)|| (bbArea <5000)) {
							itc.remove(); // other than deliberately making the
						} else {
							rects.add(mr);
							mr.points(pt);
							for (Point p : pt)
								System.out.println(p);
							Rect rect = mr.boundingRect();
							Imgproc.rectangle(img, pt[0], pt[2], new Scalar(255, 255, 0), 5);
							crop_img = grey.submat(rect);
							//Imgproc.resize(crop_img, crop_img, new Size(600,175));
							LoadImage frame2=new LoadImage(CommomOperations.MatToBuffered(crop_img));
							frame2.setVisible(true);
						}
					} catch (Exception ex) {
					}
				}
				Tesseract tesseract = new Tesseract();
				tesseract.setDatapath("C:\\Users\\HP\\Downloads\\Tess4J-3.4.8-src\\Tess4J\\tessdata");
				try {
					jtfplatenumber.setText(tesseract.doOCR(CommomOperations.MatToBuffered(crop_img)));
				} catch (TesseractException exp) {
					// TODO Auto-generated catch block
					exp.printStackTrace();

				}

			}
		});
		btnExtract.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnExtract.setBounds(435, 30, 155, 35);
		imagedeskpane.add(btnExtract);
		
		
		
		
		JLabel lblDate = new JLabel("Date of Arrival");
		lblDate.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblDate.setBounds(619, 178, 121, 35);
		imagedeskpane.add(lblDate);
		
		JLabel lblTimeOfArrival = new JLabel("Time of Arrival");
		lblTimeOfArrival.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTimeOfArrival.setBounds(619, 250, 121, 35);
		imagedeskpane.add(lblTimeOfArrival);
		
		jtfplatenumber = new JTextField();
		jtfplatenumber.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jtfplatenumber.setBounds(775, 98, 135, 40);
		imagedeskpane.add(jtfplatenumber);
		jtfplatenumber.setColumns(10);
		
		jtfdate = new JTextField();
		jtfdate.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jtfdate.setColumns(10);
		jtfdate.setBounds(775, 176, 135, 40);
		imagedeskpane.add(jtfdate);
		
		jtftime = new JTextField();
		jtftime.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jtftime.setColumns(10);
		jtftime.setBounds(775, 248, 135, 40);
		imagedeskpane.add(jtftime);
		
		java.util.Date date = new java.util.Date();
		String strDateFormat = "yyyy/MM/dd";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		jtfdate.setText(formattedDate);
		strDateFormat = "hh:mm:ss";
		dateFormat = new SimpleDateFormat(strDateFormat);
		formattedDate = dateFormat.format(date);
		jtftime.setText(formattedDate);
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "";
				String car_number = jtfplatenumber.getText().trim();
				System.out.println(car_number);
				if (car_number.isEmpty()) {
					message += "The plate number must not be empty\n";
				} else {
					CarInfoTo record = new CarInfoTo();
					record.setCar_number(car_number);
					CarInfoDao action = new CarInfoDao();
					if (action.insertRecord(record)) {
						message += "New Car is added to the system";
					} else {
						message += "Insertion failure" + action.getErrormessage();
					}
				}
				JOptionPane.showMessageDialog(imagedeskpane, message);
			}
		});
		btnSave.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnSave.setBounds(691, 371, 155, 35);
		imagedeskpane.add(btnSave);
		setSize(950,700);
		//setBounds(100, 100, 450, 300);

	}
}
