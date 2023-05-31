package stage_mail;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class zx extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	int count = 0;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					zx frame = new zx();
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
	public zx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		textField = new JTextField();
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {
					public void run() {
						
						
						Mail mail = new Mail();
						mail.setupServerProperties();

						List<String> recipients = new ArrayList<>();
						recipients.add("anouarzerrik@gmail.com");
						recipients.add("anouar.zerrik@usmba.ac.ma");
						
						//int count = 0;
						for (String recipient : recipients) {
							try {
								btnNewButton.setEnabled(false);
								Thread.sleep(0); // Sleep for 2 minutes (2 minutes = 120,000 milliseconds)
								
								mail.draftEmail2(
										"C:\\Users\\UTENTE\\Desktop\\send.txt;C:\\Users\\UTENTE\\Desktop\\Nouveau document texte.txt",
										"HI???????????", "Fin", recipient);
								
								mail.sendEmail("anoirzerrik2014@gmail.com", "vtkzqhsattbtzmtf");
								count++;
								textField.setText(Integer.toString(count));
								
							} catch (InterruptedException | MessagingException | IOException ex) {
								ex.printStackTrace();
							}
						}
						btnNewButton.setEnabled(true);
						int n = Integer.parseInt(textField.getText());         
						System.out.println(n);
					}
				});
				thread.start();
			}
		});
		contentPane.add(btnNewButton);
	}
}
