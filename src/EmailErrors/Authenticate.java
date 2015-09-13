package EmailErrors;

import java.io.BufferedReader;
import java.io.Console;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
 
public class Authenticate extends Authenticator {
 

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		//You can use JPasswordField in case you're creating a Swing Application
		//Or you can use <h:inputSecre> if you're creating a Web Application
		//Either way, ask the user to provide his email and password
		System.out.println("Please provide your Email and Password");
		char[] password = null;
		
		Console console = System.console();
		if (console == null) {
		    System.out.println("No console: non-interactive mode!");
		    System.exit(0);
		}
		System.out.print("Enter your email: ");
		String email = console.readLine();
		
		password=console.readPassword();
		 
		
		System.out.println(this.getDefaultUserName());
		//Read the values and return a new instance of the PasswordAuthentication object
		return new PasswordAuthentication(email, password.toString());
		
	}
}