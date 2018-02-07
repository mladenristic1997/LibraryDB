package library.db;

import java.util.Scanner;

import library.beans.Admin;
import library.beans.Book;
import library.beans.User;
import library.tables.AdminManager;
import library.tables.BookManager;
import library.tables.Library;
import library.tables.UserManager;
import library.util.InputHelper;
import library.util.Login;

public class Main {
	
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception{
		
		ConnectionManager.getInstance().setDBType(DBType.MYSQL);
		
		if(AdminManager.isEmpty()) {
			Admin bean = new Admin();
			bean.setUserName(InputHelper.getInput("Enter username: "));
			bean.setPassword(InputHelper.getInput("Enter password: "));
			AdminManager.insert(bean);
		}
		
		System.out.println("Login for admin (0)\nLogin for user (1)");
		int loginChoice = -1;
		try {
		loginChoice = scan.nextInt();
		}
		catch (Exception e) {
			
		}
		switch(loginChoice) {
			case 0:
				Admin admin = new Admin();
				admin.setUserName(InputHelper.getInput("Enter username: "));
				admin.setPassword(InputHelper.getInput("Enter password: "));
				if(!Login.loginAdmin(admin))
					break;
				
				//Options for admin
				while(true) {
					System.out.println("1. Display all admin rows");
					System.out.println("2. Get one admin row");
					System.out.println("3. Insert admin");
					System.out.println("4. Update admin");
					System.out.println("5. Delete admin");
					System.out.println("6. Display all user rows");
					System.out.println("7. Get one user row");
					System.out.println("8. Insert user");
					System.out.println("9. Update user");
					System.out.println("10. Delete user");
					System.out.println("11. Check user's rented books");
					System.out.println("12. Display all book rows");
					System.out.println("13. Get one book row");
					System.out.println("14. Insert book");
					System.out.println("15. Delete book");
					System.out.println("0. Exit");
					
					int adminInputChoice = scan.nextInt();
					
					switch(adminInputChoice) {
						case 1:
							AdminManager.displayAllRows();
							System.out.println("");
							break;
						case 2:
							System.out.println("Enter adminId");
							int adminIdInput = scan.nextInt();
							if(AdminManager.isExists(adminIdInput)) {
								Admin getAdmin = AdminManager.getRow(adminIdInput);
								System.out.println(getAdmin.toString());
								System.out.println("");
							}
							else {
								System.out.println("Admin doesn't exist");
								System.out.println("");
							}
							break;
						case 3:
							Admin insertAdmin = new Admin();
							insertAdmin.setUserName(InputHelper.getInput("Enter username: "));
							insertAdmin.setPassword(InputHelper.getInput("Enter password: "));
							AdminManager.insert(insertAdmin);
							System.out.println("");
							break;
						case 4:
							System.out.println("Enter admin id");
							int updateAdminId = scan.nextInt();
							if(AdminManager.isExists(updateAdminId)) {
								Admin updateAdmin = AdminManager.getRow(updateAdminId);
								updateAdmin.setUserName(InputHelper.getInput("Enter username: "));
								updateAdmin.setPassword(InputHelper.getInput("Enter password: "));
								AdminManager.update(updateAdmin);
								System.out.println("");
							}
							else {
								System.out.println("Admin doesn't exist\n");
							}
							break;
						case 5:
							System.out.println("Enter admin id");
							int deleteAdminId = scan.nextInt();
							if(deleteAdminId == admin.getAdminId()) {
								AdminManager.delete(deleteAdminId);
								System.exit(1);
							}
							else if(AdminManager.isExists(deleteAdminId)) {
								AdminManager.delete(deleteAdminId);
								System.out.println("");
							}
							else {
								System.out.println("Admin doesn't exist \n");
							}
							break;
						case 6:
							UserManager.displayAllRows();
							System.out.println("");
							break;
						case 7:
							System.out.println("Enter userId");
							int userIdInput = scan.nextInt();
							if(UserManager.isExists(userIdInput)) {
								User getUser = UserManager.getRow(userIdInput);
								System.out.println(getUser.toString());
								System.out.println("");
							}
							else {
								System.out.println("User doesn't exist\n");
							}
							break;
						case 8:
							User insertUser = new User();
							insertUser.setUserName(InputHelper.getInput("Enter username: "));
							insertUser.setPassword(InputHelper.getInput("Enter password: "));
							UserManager.insert(insertUser);
							System.out.println("");
							break;
						case 9:
							System.out.println("Enter user id");
							int updateUserId = scan.nextInt();
							if(UserManager.isExists(updateUserId)) {
								User updateUser = UserManager.getRow(updateUserId);
								updateUser.setUserName(InputHelper.getInput("Enter username: "));
								updateUser.setPassword(InputHelper.getInput("Enter password: "));
								UserManager.update(updateUser);
								System.out.println("");
							}
							else {
								System.out.println("User doesn't exist\n");
							}
							break;
						case 10:
							System.out.println("Enter user id");
							int deleteUserId = scan.nextInt();
							if(UserManager.isExists(deleteUserId)) {
								User delete = UserManager.getRow(deleteUserId);
								Library.returnAllBooks(delete);
								UserManager.delete(deleteUserId);	
								System.out.println("");
							}
							else {
								System.out.println("User doesn't exist\n");
							}
							break;
						case 11:
							System.out.println("Enter user id");
							int getUserBooks = scan.nextInt();
							if(UserManager.isExists(getUserBooks)) {
								User rentedBooks = UserManager.getRow(getUserBooks);
								UserManager.getRentedBooks(rentedBooks);
								System.out.println("");
							}
							else {
								System.out.println("User doesn't exist\n");
							}
							break;
						case 12:
							BookManager.displayAllRows();
							System.out.println("");
							break;
						case 13:
							System.out.println("Enter book id");
							int getBook = scan.nextInt();
							if(BookManager.isExists(getBook)) {
								Book getBookRow = BookManager.getRow(getBook);
								System.out.println(getBookRow.toString());
								System.out.println("");
							}
							else {
								System.out.println("Book doesn't exist");
								System.out.println("");
							}
							break;
						case 14:
							Book insertBook = new Book();
							insertBook.setBookName(InputHelper.getInput("Enter book name"));
							BookManager.insert(insertBook);
							System.out.println("");
							break;
						case 15:
							System.out.println("Enter book id");
							int deleteBookId = scan.nextInt();
							if(BookManager.isExists(deleteBookId)) {
								BookManager.delete(deleteBookId);								
							}
							else {
								System.out.println("Book id doesn't exist");
							}
							System.out.println("");
							break;
						case 0:
							System.exit(1);
							break;
						default:
							System.out.println("Invalid input");
							
					}
				}
				
			case 1:
				User user = new User();
				user.setUserName(InputHelper.getInput("Enter username: "));
				user.setPassword(InputHelper.getInput("Enter password: "));
				if(!Login.loginUser(user))
					break;
					
				while(true) {
					//User option
					System.out.println("1. User status");
					System.out.println("2. Check rented books");
					System.out.println("3. See all books");
					System.out.println("4. Rent a book");
					System.out.println("5. Return a book");
					System.out.println("6. Return all books");
					System.out.println("7. Update account");
					System.out.println("8. Delete account");
					System.out.println("0. Exit");
					
					int userInput = scan.nextInt();
					
					switch(userInput) {
						case 1:
							user = UserManager.getRow(user.getUserId());
							System.out.println(user.toString());
							System.out.println("");
							break;
						case 2:
							UserManager.getRentedBooks(user);
							System.out.println("");
							break;
						case 3:
							BookManager.displayAllRows();
							System.out.println("");
							break;
						case 4:
							System.out.println("Enter bookId of the book you want to rent");
							int rentBookId = scan.nextInt();
							if(BookManager.isExists(rentBookId)) {
								Book rentBook = BookManager.getRow(rentBookId);
								Library.rentBook(user, rentBook);
								System.out.println("");								
							}
							else {
								System.out.println("Book doesn't exist");
								System.out.println("");
							}
							break;
						case 5:
							System.out.println("Enter bookId of the book you want to return");
							int returnBookId = scan.nextInt();
							Book returnBook = BookManager.getRow(returnBookId);
							Library.returnBook(user, returnBook);
							System.out.println("");
							break;
						case 6:
							Library.returnAllBooks(user);
							System.out.println("");
							break;
						case 7:
							User updateUser = new User();
							updateUser.setUserName(InputHelper.getInput("Enter new name: "));
							updateUser.setPassword(InputHelper.getInput("Enter new password: "));
							updateUser.setUserId(user.getUserId());
							UserManager.update(updateUser);
							user = UserManager.getRow(user.getUserId());
							System.out.println("");
							break;
						case 8:
							Library.returnAllBooks(user);
							UserManager.delete(user.getUserId());
							System.out.println("");
							System.exit(1);
							break;
						case 0:
							System.exit(1);
							break;
						default:
							System.out.println("Invalid input");
					}
				}
				
			default:
				System.out.println("Invalid input");
		}
	
		
	//	ConnectionManager.getInstance().close();
	
	}
}
