package health_clinic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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

    private static void recordkeeper() {
		Scanner read = new Scanner(System.in);
		System.out.println("Do you want to register a new Doctor or patient?");
		try {
			String userreg = read.nextLine().toLowerCase();
			System.out.println("Registration form");
			System.out.println("Enter name:");
			String name = read.nextLine();
			String gender = getgender(read);
			int age = getage(read);
			int phonenumber = getSanitizedPhoneNumber(read);
			String password = getPasswordWithPolicy(read);

			// Hash the password before writing it to the file
			String hashedPassword = getHashPassword(password);

			boolean flag = checkPasswordPolicy(password);
			if (flag) {
				System.out.println("Thank you!!!!");
				// We want to know the file that we want to write on
				String userfile;
				if ("patient".equals(userreg)) {
					userfile = "patientinfo.txt";
				} else if ("doctor".equals(userreg)) {
					userfile = "doctorinfo.txt";
				} else {
					System.out.println("Error!");
					return;
				}
				boolean userExists = checkifuserexists(name, userreg);

				if (userExists) {
					System.out.println("User already exists. Please choose a different username.");
					return; // Start again from the beginning
				}

				// Write registration info to the file
				writeToFile(userfile, name, hashedPassword, age, gender, phonenumber);
				System.out.println("Registration Successful");
			}
		} catch (InputMismatchException e) {
			System.out.println("Input should be an integer");
		}
	}
 
    private static final int max_gendertries = 2;
    private static String getgender(Scanner read) {
		int trytoinputgender = 0;
		while (trytoinputgender < max_gendertries) {
			try {
				System.out.println("Enter gender male/female:");
				String gender = read.nextLine().toLowerCase();
				if (gender.equals("male") || gender.equals("female")) {
					System.out.println("Gender:" + gender);
					return gender;
				} else {
					System.out.println("invaled gender, please try again");
				}
			} catch (InputMismatchException e) {
				System.out.println("you should enter strings");
				read.nextLine(); // Consume the invalid input
			}
		}
		return null;
	}

    private static final int max_agetries = 2;
	private static int getage(Scanner read) {
		int ageretrie = 0;
		while (ageretrie < max_agetries) {
			try {
				System.out.println("Enter your age:");
				int age = read.nextInt();
				read.nextLine();
				String agestring = Integer.toString(age);
				if (agestring.length() <= 3) {
					System.out.println("Age: " + age);
					return age;
				} else {
					System.out.println("Invalid age. Please try again.");
					ageretrie++;
				}
			} catch (InputMismatchException e) {
				System.out.println("you should enter strings");
				read.nextLine(); // Consume the invalid input
			}
		}
		return 0;
	}

	private static int getSanitizedPhoneNumber(Scanner read) {
		int phonenumber;
		while (true) {
			try {
				System.out.println("Enter phone number:");
				phonenumber = read.nextInt();
				read.nextLine(); // Consume the newline character
				if (String.valueOf(phonenumber).length() == 10) {
					break;
				} else {
					System.out.println("Invalid phone number. Please enter a 10-digit number.");
				}
			} catch (InputMismatchException e) {
				System.out.println("you should enter an integer number.");
				read.nextLine(); // Consume the invalid input
			}
			catch (NumberFormatException e) {
				System.out.println("bbb");
			}
		}
		return phonenumber;
	}

	private static String getPasswordWithPolicy(Scanner read) {
		String password;
		while (true) {
			System.out.println("Enter password (at least 10 characters, including uppercase, lowercase, and a digit):");
			password = read.nextLine();
			if (checkPasswordPolicy(password)) {
				break;
			} else {
				System.out.println("sorry, Please ensure it meets the policy requirements.");
			}
		}
		return password;
	}

	private static boolean checkPasswordPolicy(String password) {
		return password.length() >= 10 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")
				&& password.matches(".*\\d.*");
	}

	private static void writeToFile(String userfile, String name, String password, int age, String gender,
			int phonenumber) {
		BufferedWriter bw = null;
		try {
			// create the BufferedWriter
			bw = new BufferedWriter(new FileWriter(userfile, true));
			try {
				bw.write(name + "," + password + "," + age + "," + gender + "," + phonenumber + "\n");
				System.out.println("Data written successfully");
			} finally {
				bw.close();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private static boolean checkifuserexists(String enteredUsername, String role) {
		String fileName = role + "info.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] userInfo = line.split(",");
				if (userInfo.length >= 1) {
					String storedUsername = userInfo[0].trim();
					if (enteredUsername.equals(storedUsername)) {
						return true; // User already exists
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file: " + e.getMessage());
		}
		return false; // User does not exist
	}

	private static String getHashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			password = encode(md.digest(password.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("The algorithm does not exist");
		}
		return password;
	}

	private static String encode(byte[] digest) {
		return Base64.getEncoder().encodeToString(digest);
	}

	private static boolean checkcredentialsrecordkeeper(String enteredUsername, String enteredPassword) {
		try (BufferedReader br = new BufferedReader(new FileReader("recordkeeperinfo.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Split the line into username and password
				String[] users = line.split(",");
				if (users.length == 2) {
					String storedUsername = users[0].trim();
					String storedPassword = users[1].trim();

					// Hash the entered password for comparison
					String Passwordentered = getHashPassword(enteredPassword);

					// Check if the entered credentials match any records
					if (enteredUsername.equals(storedUsername) && Passwordentered.equals(storedPassword)) {
						return true;
					}
				} else {
					System.out.println("Invalid username or password");
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file");
		}
		return false;
	}

	private static boolean checkcredentialspatient(String enteredUsername, String enteredPassword) {
		try (BufferedReader br = new BufferedReader(new FileReader("patientinfo.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Split the line into username and password
				String[] users = line.split(",");
				if (users.length == 5) {
					String storedUsername = users[0].trim();
					String storedPassword = users[1].trim();

					// Hash the entered password for comparison
					String Passwordentered = getHashPassword(enteredPassword);

					// Check if the entered credentials match any records
					if (enteredUsername.equals(storedUsername) && Passwordentered.equals(storedPassword)) {
						return true;
					}
				} 
				else {
					System.out.println("Invalid username or password");
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file");
		}
		return false;
	}

	private static boolean checkcredentialsdoctor(String enteredUsername, String enteredPassword) {
		try (BufferedReader br = new BufferedReader(new FileReader("doctorinfo.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Split the line into username and password
				String[] users = line.split(",");
				if (users.length == 5) {
					String storedUsername = users[0].trim();
					String storedPassword = users[1].trim();

					// Hash the entered password for comparison
					String Passwordentered = getHashPassword(enteredPassword);

					// Check if the entered credentials match any records
					if (enteredUsername.equals(storedUsername) && Passwordentered.equals(storedPassword)) {
						return true;
					}
				} else {
					System.out.println("Invalid username or password");
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file");
		}
		return false;
	}
	
    private static void doctor(String enteredUsername, String enteredPassword) {
			Scanner read = new Scanner(System.in);
			// Doctor options after login
			System.out.println("Do you want to enter patient medical info or view personal info?");
			String doctorOption = read.nextLine().toLowerCase();
			try {
				if ("medical info".equals(doctorOption)) {
					enterMedicalInfo();
				} else if ("view personal info".equals(doctorOption)) {
					viewpersonalinfo(enteredUsername, enteredPassword);
				} else {
					System.out.println("Invalid option.");
				}
			}
			catch (InputMismatchException e) {
				System.out.println("Input doesn't match");
			}
		}
	     
		// doctor option: enter patient medical info
	private static void enterMedicalInfo() {
				Scanner read = new Scanner(System.in);
				// Add your logic here for entering patient medical info
				System.out.println("Enter patient medical information:");
				System.out.println("Enter name:");
				String name = read.nextLine();
				if (isPatientExists(name)) {
					System.out.println("Enter medical situation:");
					String medicalsituation = read.nextLine();
					System.out.println("Enter medical treatment:");
					String medicaltreatment = read.nextLine();
					// write registration info to the file
					try (BufferedWriter bw = new BufferedWriter(new FileWriter("pmedicalinfo.txt", true))) {
						bw.write(name + "," + medicalsituation + "," + medicaltreatment + "\n");
						System.out.println("entering medicalinfo done Successfully");
					} catch (IOException e) {
						System.out.println("FileNotFound");
					}
				} else {
					System.out.println("patient dosenot exist");
				}
			}
	    
	private static boolean isPatientExists(String name) {
			try (BufferedReader br = new BufferedReader(new FileReader("patientinfo.txt"))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] patientInfo = line.split(",");
					String storedName = patientInfo[0].trim();
					
				 if (name.equalsIgnoreCase(storedName)) {
					 return true; // Patient found }
				 }
					 
				}
			} catch (IOException e) {
				System.out.println("Error reading file: " + e.getMessage());
			}
			return false; // Patient not found
		}

		// doctor option: view personal info
	private static void viewpersonalinfo(String enteredUsername, String enteredPassword) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader("doctorinfo.txt"));
					try {
						System.out.println("What do you want me to display for you?");
						String line;
						while ((line = br.readLine()) != null) {
							String str[] = line.split(",");
							String storedUsername = str[0].trim();
							String storedPassword = str[1].trim();
							String Passwordentered = getHashPassword(enteredPassword);
							if (enteredUsername.equals(storedUsername) && Passwordentered.equals(storedPassword) == true) {
								System.out.println("Personal Information:");
								System.out.println("name: " + str[0]);
								System.out.println("medicalsituation: " + str[1]);
								System.out.println("medicaltreatment: " + str[2]);
							} else {
								System.out.println("error");
							}
						}
					} finally {
						br.close();
					}
				} catch (IOException e) {
					System.out.println(e);
				}
				System.out.println("Viewing personal information.");
			}

	private static void patient(String enteredUsername, String enteredPassword) {
		Scanner read = new Scanner(System.in);
		System.out.println("Do you want to view personal info or medical info");
		String patientOption = read.nextLine().toLowerCase();

		if ("medical info".equals(patientOption)) {
			viewMedicalInfo(enteredUsername);
		} else if ("view personal info".equals(patientOption)) {
			viewPersonalInfopatient(enteredUsername, enteredPassword);
		} else {
			System.out.println("Invalid option.");
		}
	} 
	private static void viewPersonalInfopatient(String enteredUsername, String enteredPassword) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("patientinfo.txt"));
			try {
				System.out.println("What do you want me to display for you?");
				String line;
				while ((line = br.readLine()) != null) {
					String str[] = line.split(",");
					String storedUsername = str[0].trim();
					String storedPassword = str[1].trim();
					String Passwordentered = getHashPassword(enteredPassword);
					if (enteredUsername.equals(storedUsername) && Passwordentered.equals(storedPassword) == true) {
						System.out.println("Personal Information:");
						System.out.println("Username: " + str[0]);
						System.out.println("Password: " + str[1]);
						System.out.println("Age: " + str[2]);
						System.out.println("Phone Number: " + str[3]);
						System.out.println("Gender: " + str[4]);
					}
					else {
						System.out.println("error");
					}
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			System.out.println("error");
		}
		System.out.println("Viewing personal information.");
		}

	private static void viewMedicalInfo(String enteredUsername) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("pmedicalinfo.txt"));
			try {
				System.out.println("your medical info page :) ");
				String line;
				while ((line = br.readLine()) != null) {
					String str[] = line.split(",");
					String storedUsername = str[0].trim();
					String storedPassword = str[1].trim();
					if (enteredUsername.equals(storedUsername) == true) {
						System.out.println("medical Information:");
						System.out.println("Username: " + str[0]);
						System.out.println(" medicalsituation: " + str[1]);
						System.out.println("medicaltreatment: " + str[2]);
					}
					else {
						System.out.println("error");
					}
				}
			} finally {
				br.close();
			}
		} catch (IOException e) {
			System.out.println("error");
		}
		System.out.println("Viewing personal information.");
	}


}	