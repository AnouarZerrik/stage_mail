package stage_mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mail {

	Session newSession = null;
	MimeMessage mimeMessage = null;

	public static void main(String args[]) throws AddressException, MessagingException, IOException {
		Mail mail = new Mail();
		mail.setupServerProperties();
		// mail.draftEmail();
		// mail.draftEmailsans_att();
		mail.sendEmail();
	}

	void sendEmail() throws MessagingException {
		String fromUser = "anoirzerrik2014@gmail.com";
		String fromUserPassword = "vtkzqhsattbtzmtf";

		String emailHost = "smtp.gmail.com";
		Transport transport = newSession.getTransport("smtp");
		transport.connect(emailHost, fromUser, fromUserPassword);
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		transport.close();
		System.out.println("Email successfully sent!!!");
	}

	MimeMessage draftEmailsans_att(String Subject, String Body)
			throws AddressException, MessagingException, IOException {
		//String[] emailReceipients = { "anouarzerrik@gmail.com" };
		// String b = "C:\\Users\\UTENTE\\Desktop\\send.txt";

		List<String> emailReceipients1 = new ArrayList<>();

		emailReceipients1.add("anouarzerrik@gmail.com");
		emailReceipients1.add("abdelilah.kouzih@usmba.ac.ma");
		emailReceipients1.add("ayoubelfakraoui@gmail.com");
		String emailSubject = Subject;
		String emailBody = Body;
		mimeMessage = new MimeMessage(newSession);

		for (String element : emailReceipients1) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(element));
		}
		
		mimeMessage.setSubject(emailSubject);

		MimeMultipart multiPart = new MimeMultipart();

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(emailBody);

		multiPart.addBodyPart(messageBodyPart);
		mimeMessage.setContent(multiPart);

		return mimeMessage;
	}

	MimeMessage draftEmail(String f1, String f2, String Subject, String Body)
			throws AddressException, MessagingException, IOException {
		//String[] emailReceipients = { "anouarzerrik@gmail.com" };

		List<String> emailReceipients1 = new ArrayList<>();

		emailReceipients1.add("anouarzerrik@gmail.com");

		emailReceipients1.add("abdelilah.kouzih@usmba.ac.ma");
		emailReceipients1.add("ayoubelfakraoui@gmail.com");
		// emailReceipients1.add("anouar.zerrik@usmba.ac.ma");
		String b = "";// C:\\Users\\UTENTE\\Desktop\\send.txt
		String emailSubject = Subject;
		String emailBody = Body;
		mimeMessage = new MimeMessage(newSession);

		for (String element : emailReceipients1) {
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(element));
		}

		mimeMessage.setSubject(emailSubject);

		MimeMultipart multiPart = new MimeMultipart();

		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(emailBody);

		File f = new File(f1);

		if (f.exists()) {
			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			attachmentBodyPart.attachFile(f);
			multiPart.addBodyPart(attachmentBodyPart);
		}

		File k = new File(f2);
		if (k.exists()) {
			MimeBodyPart attachmentBodyPart2 = new MimeBodyPart();
			attachmentBodyPart2.attachFile(k);
			multiPart.addBodyPart(attachmentBodyPart2);
		}

		multiPart.addBodyPart(messageBodyPart);
		mimeMessage.setContent(multiPart);

		return mimeMessage;
	}

	void setupServerProperties() {
		Properties properties = System.getProperties();
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		newSession = Session.getDefaultInstance(properties, null);
	}

}