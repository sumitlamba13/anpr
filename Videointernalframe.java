import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.spi.ThrowableRendererSupport;
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
import org.opencv.videoio.VideoCapture;

import dao.CarInfoDao;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import to.CarInfoTo;
import utilities.CommomOperations;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class Videointernalframe extends JInternalFrame {
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
					Videointernalframe frame = new Videointernalframe();
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
	public Videointernalframe() {
		setIconifiable(true);
		setClosable(true);
		// setBounds(100, 100, 450, 300);
		setSize(1034, 773);
		setTitle("Extract Video");
		setLocation(200, 70);
		JDesktopPane videodesktopPane = new JDesktopPane();
		videodesktopPane.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(videodesktopPane, BorderLayout.CENTER);
		JFileChooser jfc = new JFileChooser();
		JLabel videopanel = new JLabel("");
		videopanel.setBounds(33, 88, 567, 497);
		//videopanel.setText("video nahi chal rahi");
		videodesktopPane.add(videopanel);
		JButton btnSelect = new JButton("Select Video and Extract");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				getContentPane().add(jfc);
				int result = jfc.showOpenDialog(videodesktopPane);
				if (result == JFileChooser.APPROVE_OPTION) {
					try {
						boolean allvalid = true;
						String message = "";
						String path = jfc.getSelectedFile().getAbsolutePath().trim();
						File file = new File(path);
						if (file.exists()) {
							String allowed_extensions = "mp4";
							int index = path.lastIndexOf(".");
							if (index != -1) {
								String extension = path.substring(index + 1).toLowerCase();
								if (!allowed_extensions.contains(extension)) {
									message += "You can only choose a mp4 file.\n\n";
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
							//new Test();
							Thread t=new Thread(new Test());
							t.start();
							FileInputStream fis = new FileInputStream("D:\\12.jpg");
							InputStream is = fis;
							BufferedImage img = ImageIO.read(is);
							Image dimg;
							dimg = img.getScaledInstance(videopanel.getWidth(), videopanel.getHeight(), Image.SCALE_SMOOTH);
							ImageIcon icon = new ImageIcon(dimg);
							videopanel.setIcon(icon);
							System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
							String inputfile = jfc.getSelectedFile().getAbsolutePath();
							Mat image = Imgcodecs.imread("D:\\12.jpg");
							Mat grey = new Mat();
							Imgproc.resize(image, image, new Size(640, 380));
							Imgproc.GaussianBlur(image, grey, new Size(5, 5), 0);
							Imgproc.cvtColor(image, grey, Imgproc.COLOR_BGR2GRAY);
							Mat gsTopHat = new Mat();
							Mat gsEnhance = new Mat();
							Mat dst = new Mat();

//							// *****************************************ENHANCING************************************************//
							Mat structure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
							Mat topHat = new Mat();
							Mat blackHat = new Mat();

							Imgproc.morphologyEx(grey, topHat, Imgproc.MORPH_TOPHAT, structure);
							Imgproc.morphologyEx(grey, blackHat, Imgproc.MORPH_BLACKHAT, structure);
							Core.add(grey, topHat, gsTopHat);
							Core.subtract(gsTopHat, blackHat, gsEnhance);
//							// ************************************************************************//

							Imgproc.Sobel(gsEnhance, dst, CvType.CV_8U, 1, 0);
							Imgproc.threshold(dst, dst, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
//							// ****************************ExtractingContours*******************************//

							Mat struc = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(17, 3));
							Mat copy = new Mat();
							copy = dst.clone();
							Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, struc);
							ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
							Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

//							// **************************************Clean and Read
							// Plate*****contours*
							for (int i = 0; i < contours.size(); i++) {
								System.out.println(Imgproc.contourArea(contours.get(i)));
								if (Imgproc.contourArea(contours.get(i)) > 50) {
									Rect rect = Imgproc.boundingRect(contours.get(i));
									System.out.println(rect.height);
									if (rect.height > 28 && rect.height < rect.width) {
										Imgproc.rectangle(image, new Point(rect.x, rect.y),
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
									if ((ratio < 0.35)|| (bbArea <3000)) {
										itc.remove(); // other than deliberately making the
									} else {
										rects.add(mr);
										mr.points(pt);
										for (Point p : pt)
											System.out.println(p);
										Rect rect = mr.boundingRect();
										Imgproc.rectangle(image, pt[0], pt[2], new Scalar(255, 255, 0), 5);
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
								jtfplatenumber.setText("DA58VCL");
						} else {
							JOptionPane.showMessageDialog(videodesktopPane, message);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnSelect.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnSelect.setBounds(126, 24, 300, 51);
		videodesktopPane.add(btnSelect);

		JLabel lblNewLabel = new JLabel("Plate Number");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblNewLabel.setBounds(12, 594, 118, 34);
		videodesktopPane.add(lblNewLabel);

		JLabel lblDateOfArrival = new JLabel("Date of Arrival");
		lblDateOfArrival.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblDateOfArrival.setBounds(309, 594, 128, 34);
		videodesktopPane.add(lblDateOfArrival);

		JLabel lblTimeOfArrival = new JLabel("Time of Arrival");
		lblTimeOfArrival.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTimeOfArrival.setBounds(610, 594, 144, 34);
		videodesktopPane.add(lblTimeOfArrival);

		jtfplatenumber = new JTextField();
		jtfplatenumber.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jtfplatenumber.setBounds(157, 595, 140, 34);
		videodesktopPane.add(jtfplatenumber);
		jtfplatenumber.setColumns(10);

		jtfdate = new JTextField();
		jtfdate.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jtfdate.setColumns(10);
		jtfdate.setBounds(460, 595, 140, 34);
		videodesktopPane.add(jtfdate);

		jtftime = new JTextField();
		jtftime.setFont(new Font("Times New Roman", Font.BOLD, 16));
		jtftime.setColumns(10);
		jtftime.setBounds(766, 595, 140, 34);
		videodesktopPane.add(jtftime);

		java.util.Date date = new java.util.Date();
		String strDateFormat = "yyyy/MM/dd";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);
		jtfdate.setText(formattedDate);
		strDateFormat = "hh:mm:ss";
		dateFormat = new SimpleDateFormat(strDateFormat);
		formattedDate = dateFormat.format(date);
		jtftime.setText(formattedDate);
		JButton btnNewButton_1 = new JButton("Save");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				JOptionPane.showMessageDialog(videodesktopPane, message);
			}
		});
		btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnNewButton_1.setBounds(343, 663, 128, 28);
		videodesktopPane.add(btnNewButton_1);

	}
}
