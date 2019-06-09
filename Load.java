import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ListIterator;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.transaction.xa.XAResource;

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
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

import dao.CarInfoDao;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import to.CarInfoTo;
import utilities.CommomOperations;

import javax.swing.JPanel;
import javax.swing.JDesktopPane;
import java.awt.SystemColor;

public class Load {

	private JFrame frame;
	/**
	 * @wbp.nonvisual location=-17,14
	 */
	private final JPanel panel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Load window = new Load();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public Load() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(1300, 850);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle("Automatic Number Plate Recognition System");
		// frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JDesktopPane deskpane = new JDesktopPane();
		deskpane.setBackground(new Color(102, 205, 170));
		frame.getContentPane().add(deskpane, BorderLayout.CENTER);
		
		JLabel lblWelcomeToAutomatic = new JLabel("Welcome to Automatic Number Plate Recognition System\r\n");
		lblWelcomeToAutomatic.setForeground(new Color(0, 255, 127));
		lblWelcomeToAutomatic.setFont(new Font("Times New Roman", Font.BOLD, 40));
		lblWelcomeToAutomatic.setBounds(131, 44, 991, 120);
		deskpane.add(lblWelcomeToAutomatic);
		
		JButton btnNewButton = new JButton("Extact Image");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommomOperations.openInternalFrame(deskpane, new Imageinternalframe());
			}
		});
		btnNewButton.setBackground(SystemColor.menu);
		btnNewButton.setForeground(new Color(123, 104, 238));
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 32));
		btnNewButton.setBounds(188, 222, 257, 186);
		deskpane.add(btnNewButton);
		
		JButton btnExtactVideo = new JButton("Extact Video");
		btnExtactVideo.setBackground(new Color(240, 240, 240));
		btnExtactVideo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommomOperations.openInternalFrame(deskpane, new Videointernalframe());
			}
		});
		btnExtactVideo.setForeground(new Color(147, 112, 219));
		btnExtactVideo.setFont(new Font("Times New Roman", Font.BOLD, 32));
		btnExtactVideo.setBounds(681, 222, 257, 186);
		deskpane.add(btnExtactVideo);
		
		
	}
}
