package jdbc;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Project {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/atmmachine", "root", "root"
					);
			System.out.println("Welcome to the simple ATM machine.");
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Enter in your Account Number");
			String AccountNo = scan.nextLine();
			
			System.out.println("Enter in your pin");
			String pin = scan.nextLine();
			
			System.out.println(" To Register a new Account enter 1 . To log into your account enter in 2");
			int option = scan.nextInt();
			scan.nextLine();
			
			if(option == 1) {
				System.out.println("Enter in your name");
				String name = scan.nextLine();
				String query = "INSERT INTO Accounts(AccountNo, Pin, name, amount) VALUES (?, ?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setString(1, AccountNo);
				ps.setString(2, pin);
				ps.setString(3, name);	
				ps.setInt(4, 0);				

				ps.execute();
			
			}
			
			//read the data of the user from the data base
			String rdQry = "SELECT * FROM accounts where (AccountNo = '" + AccountNo + "' AND pin = " + pin + ")";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(rdQry);
			
			String userName = "";
			int amount = 0;
			
			while(rs.next()) {
				userName = rs.getString("name");
				amount = rs.getInt("amount");
			}
			
			
			System.out.println("Welcome " + userName + " ");
			System.out.println("You currently have " + amount + " balance on your account");
			
			System.out.println("Enter in 1 to make a deposit. \n Enter in 2 to make a withdraw");
			
			int userOption = scan.nextInt();
			
			if(userOption == 1) {
				//make a deposit
				System.out.println("Please enter in the amount of money that you want to deposit : ");
				int depositAmount = scan.nextInt();
				
				String updateQry = "UPDATE accounts SET amount = amount + " + depositAmount + " where (AccountNo = '" + AccountNo + "' AND pin = " + pin + ")";
				st.execute(updateQry);
				
				System.out.print("Amount updated successfully . Your new balance is : ");
				

				rdQry = "SELECT * FROM accounts where (AccountNo = '" + AccountNo + "' AND pin = " + pin + ")";
				
				rs = st.executeQuery(rdQry);
				
				while(rs.next()) {
					int newAmount = rs.getInt("amount");
					System.out.println(newAmount);					
				}
								
			}
			else if(userOption == 2) {
				//withdrawal
				System.out.println("Please enter in the amount of money that you want to withdraw : ");
				int withdrawAmount = scan.nextInt();
				
				if(withdrawAmount > amount) {
					System.out.println("You have insufficient funds ");
				}
				else {
				
					String updateQry = "UPDATE accounts SET amount = amount - " + withdrawAmount + " where (AccountNo = '" + AccountNo + "' AND pin = " + pin + ")";
					st.execute(updateQry);
					
					System.out.print("Amount updated successfully . Your new balance is : ");
					
	
					rdQry = "SELECT * FROM accounts where (AccountNo = '" + AccountNo + "' AND pin = " + pin + ")";
					
					rs = st.executeQuery(rdQry);
					
					while(rs.next()) {
						int newAmount = rs.getInt("amount");
						System.out.println(newAmount);					
					}
					rs.close();
				}
								
			}
			else {
				System.out.println("your input is invalid bro !!!");
			}
			
			rs.close();
			
		}
		catch(ClassNotFoundException e) {
			System.out.println("Database connect error");																																																																		
		}
		catch(SQLException e) {
			System.out.println("Unable to register driver");
		}
		catch(InputMismatchException e) {
			System.out.println("Invalid input !!!");
		}

	}

}
