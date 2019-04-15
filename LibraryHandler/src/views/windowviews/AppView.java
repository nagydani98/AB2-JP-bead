package views.windowviews;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AppView extends JFrame {

	private JPanel contentPane;
	private MemberDialog memberDialog;
	private BookDialog bookDialog;
	private LendDialog lendDialog;
	private AuthorDialog authorDialog;
	private SettingsDialog settingsDialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppView frame = new AppView();
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
	public AppView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnMembers = new JButton("Tagok");
		btnMembers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				memberDialog = new MemberDialog(AppView.this);
				memberDialog.setVisible(true);
			}
		});
		btnMembers.setBounds(10, 11, 135, 23);
		contentPane.add(btnMembers);
		
		JButton btnBooks = new JButton("K\u00F6nyvek");
		btnBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookDialog = new BookDialog();
				bookDialog.setVisible(true);
			}
		});
		btnBooks.setBounds(10, 45, 135, 23);
		contentPane.add(btnBooks);
		
		JButton btnLends = new JButton("K\u00F6lcs\u00F6nz\u00E9sek");
		btnLends.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lendDialog = new LendDialog();
				lendDialog.setVisible(true);
			}
		});
		btnLends.setBounds(10, 79, 135, 23);
		contentPane.add(btnLends);
		
		JButton btnAuthors = new JButton("Szerz\u0151k");
		btnAuthors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				authorDialog = new AuthorDialog();
				authorDialog.setVisible(true);
			}
		});
		btnAuthors.setBounds(10, 113, 135, 23);
		contentPane.add(btnAuthors);
		
		JButton btnSettings = new JButton("Be\u00E1ll\u00EDt\u00E1sok");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsDialog = new SettingsDialog();
				settingsDialog.setVisible(true);
			}
		});
		btnSettings.setBounds(10, 147, 135, 23);
		contentPane.add(btnSettings);
		
		JButton btnExit = new JButton("Kil\u00E9p\u00E9s");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exiting GUI");
				System.exit(0);
			}
		});
		btnExit.setBounds(10, 227, 89, 23);
		contentPane.add(btnExit);
		
		JLabel lblMainWImage = new JLabel(new ImageIcon(loadImage("mainwimage.jpg")));
		lblMainWImage.setBounds(155, 11, 269, 239);
		contentPane.add(lblMainWImage);
		
	}
	
	public static BufferedImage loadImage(String path){
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return image;
	}
}
