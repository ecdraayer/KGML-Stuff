package EmailErrors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 * This class will be used to send an alert email when program fails.
 * The list of recipients will be read from maillist.lst file.
 * @author Raul Alvarado
 *
 */
public class SendMail {
	private static BufferedReader br = null;
	private static FileReader fileReader = null;
	
	
	public SendMail () {
		

	}
	public void send(String ProgramName, String body)
	{
		// TODO Auto-generated method stub

	      // Sender's email ID needs to be mentioned
	      String from = "PathwayProject@nmsu.edu";

	      // Assuming you are sending email from localhost
	      String host = "smtp.nmsu.edu";

	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);
	      //properties.setProperty("mail.smtp.port", "587");
	      //properties.setProperty("mail.smtp.auth", "true");
	      //properties.setProperty("mail.smtp.starttls.enable", "true");

	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);
	
	 

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         ArrayList <String> list = new ArrayList <String>();
	         list= ReadEmailLst();
	         for (String email:list)
	        	 message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

	         // Set Subject: header field
	         message.setSubject("**Program Error **" + ProgramName);

	         // Now set the actual message
	         message.setText(body);

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
	private static ArrayList<String> ReadEmailLst()	
	{
		ArrayList<String> list= new ArrayList<String> ();
		try {
			fileReader = new FileReader("maillist.lst");
			br = new BufferedReader(fileReader);
			String line="";
			
		
			try {
				while ((line = br.readLine()) != null) 
				{
					list.add(line);
					
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return list;
		
	}

}
