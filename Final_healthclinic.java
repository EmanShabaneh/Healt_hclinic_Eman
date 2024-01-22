package health_clinic;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Final_healthclinic {
	private static int max_usertype = 3;
	public static void main(String[] args) {
		Scanner read = new Scanner(System.in);
		int usertypeattempts = 0;
		while (usertypeattempts < max_usertype) {
		try {
			System.out.println("Are you patient, doctor, or recordkeeper?");
			System.out.println("Please enter below :)");
			String users = read.nextLine().toLowerCase();
			if (users.length()> 12) {
				System.out.println("Invalid input length,please try again");
				continue;
			}
			
			if ("recordkeeper".equals(users)) {
				logintosystem("recordkeepeer",read);
				return;
			}
			else if ("doctor".equals(users)) {
				logintosystem("doctor",read);
				return;
			} else if ("patient".equals(users)) {
				logintosystem("patient",read);
				return;
			}
			else {
				//if (usertypeattempts == max_usertype ) {
				//	System.out.println("we are sorry we need to close the program since you do't know what is your role:(");
				//}else {
					usertypeattempts++; 
					//System.out.println("Remaining users attempts: " + (max_usertype- usertypeattempts));
					System.out.println("Invalid user :( please enter a valied user role.");	
	//			}
		    }
		 } catch(InputMismatchException e) {
			 System.out.println("input mismatch, please enter only alphabets");
		 } catch(Exception e) {
			 System.out.println("error appears");
		 }
	}
	}

	private static int max_loginattempts = 3;	
    private static void logintosystem(String users, Scanner read) {
		int loginattempts = 0;
		while(loginattempts < max_loginattempts) {
		try {
			System.out.println("Login form");
			System.out.println("Enter username:");
			String enteredUsername = read.nextLine();
			System.out.println("Enter password:");
			String enteredPassword = read.nextLine();
			if (checkcredentialsrecordkeeper(enteredUsername, enteredPassword )) {
				System.out.println("Login successful!");
				recordkeeper();
				return; 
			}
			else if (checkcredentialsdoctor(enteredUsername, enteredPassword )){
				System.out.println("Login successful!");
				doctor(enteredUsername, enteredPassword);
				return; 
			}
			else if (checkcredentialspatient(enteredUsername, enteredPassword )) {
				System.out.println("Login successful!");
				patient(enteredUsername, enteredPassword);
				return; // if login done successfully then exit the method 
			}
			else {
				//System.out.println("Invalid username or password");
				loginattempts++;
				System.out.println("Remaining login attempts: " + (max_loginattempts - loginattempts));
			}
		} catch (Exception e) {
			System.out.println("unexpacted error");
		}
		
	}
		System.out.println("sorry :(, we are exiting ");
	}	
}
