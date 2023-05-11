package stage_mail;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;
import java.awt.Color;

public class Form extends JFrame {

	private JPanel panel;
	private JTextField sujet_txt;
	private JTextField attachmentField;
	private JTextField attachmentField2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Form frame = new Form();
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
	public Form() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 645, 539);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_4 = new JLabel("Email successfully sent!!!");
		
		JLabel lblNewLabel = new JLabel("Envoier un Mail");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setBounds(172, 31, 248, 42);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Sujet");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1.setBounds(44, 102, 53, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Fichier 1");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_2.setBounds(24, 140, 86, 14);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Text");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_3.setBounds(54, 233, 43, 14);
		panel.add(lblNewLabel_3);
		
		sujet_txt = new JTextField();
		sujet_txt.setBounds(140, 97, 352, 27);
		panel.add(sujet_txt);
		sujet_txt.setColumns(10);
		
		attachmentField = new JTextField();
		attachmentField.setColumns(10);
		attachmentField.setBounds(140, 135, 352, 27);
		panel.add(attachmentField);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(140, 232, 352, 161);
		panel.add(scrollPane);
		
		JEditorPane body_txt = new JEditorPane();
		scrollPane.setViewportView(body_txt);
		
		JButton envoie_btn = new JButton("Envoie");
		envoie_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				Thread Thread = new Thread(new Runnable() {
					Mail mail = new Mail();
					public void run() {
				
				mail.setupServerProperties();
				
				
				if(!attachmentField.getText().isEmpty() || !attachmentField2.getText().isEmpty()) {
					try {
						mail.draftEmail(attachmentField.getText() , attachmentField2.getText() , sujet_txt.getText(),body_txt.getText());
					} catch (MessagingException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						mail.sendEmail();
					} catch (MessagingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if(attachmentField.getText().isEmpty() && attachmentField2.getText().isEmpty()) {
				try {
					mail.draftEmailsans_att(sujet_txt.getText(),body_txt.getText());
				} catch (MessagingException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					mail.sendEmail();
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				}
				
				lblNewLabel_4.setVisible(true);
				
				}
			});
			Thread.start();
				
			envoie_btn.setEnabled(false);
			}	
				
		});
		envoie_btn.setFont(new Font("Tahoma", Font.BOLD, 15));
		envoie_btn.setBounds(220, 424, 211, 42);
		panel.add(envoie_btn);
		
		JButton choose = new JButton("Choisir");
		choose.setFont(new Font("Tahoma", Font.BOLD, 11));
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(Form.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					attachmentField.setText(file.getAbsolutePath());
				}
			
			}
		});
		choose.setBounds(514, 135, 89, 23);
		panel.add(choose);
		
		JLabel lblNewLabel_2_1 = new JLabel("Fichier 2");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_2_1.setBounds(24, 184, 86, 14);
		panel.add(lblNewLabel_2_1);
		
		attachmentField2 = new JTextField();
		attachmentField2.setColumns(10);
		attachmentField2.setBounds(140, 181, 352, 27);
		panel.add(attachmentField2);
		
		JButton choose1 = new JButton("Choisir");
		choose1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(Form.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					attachmentField2.setText(file.getAbsolutePath());
				}
			}
		});
		choose1.setFont(new Font("Tahoma", Font.BOLD, 11));
		choose1.setBounds(514, 181, 89, 23);
		panel.add(choose1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(209, 71, 182, 2);
		panel.add(separator);
		
		
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_4.setForeground(new Color(204, 51, 51));
		lblNewLabel_4.setBounds(243, 475, 165, 14);
		lblNewLabel_4.setVisible(false);
		panel.add(lblNewLabel_4);
		
	}
}
