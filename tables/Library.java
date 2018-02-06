package library.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.beans.Book;
import library.beans.User;
import library.db.ConnectionManager;
import library.util.Check;

public class Library {
	private static Connection conn = ConnectionManager.getInstance().getConnection();

	private static boolean isAlreadyTaken(User user, Book book) throws Exception {
		String sql = "SELECT EXISTS(SELECT * FROM possession WHERE userId = ? AND bookId = ?) as 'Exists'";
		ResultSet rs = null;
		
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setInt(1, user.getUserId());
			stmt.setInt(2, book.getBookId());
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("Exists") == 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public static boolean rentBook(User user, Book book) throws Exception {
		
		if(Check.userValid(user) && Check.bookValid(book)
				&& !Check.isBookLimitExceded(user.getUserId())
				&& Check.isBookAvailable(book.getBookId())
				&& !isAlreadyTaken(user, book)) {
			
			String sql = "INSERT INTO possession VALUES (?, ?)";
			String numTaken = "UPDATE user SET numOfBooksTaken = ? "
					+ "WHERE userId = ?";
			
			try(
					PreparedStatement stmt = conn.prepareStatement(sql);
					PreparedStatement num = conn.prepareStatement(numTaken);
					) {
				
				int numOfBooks = user.getNumOfBooksTaken();
				num.setInt(1, ++numOfBooks);
				num.setInt(2, user.getUserId());
				num.executeUpdate();
				user.setNumOfBooksTaken(numOfBooks);
				
				stmt.setInt(1, user.getUserId());
				stmt.setInt(2, book.getBookId());
				
				stmt.executeUpdate();
				
				BookManager.removeSameBook(book);
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		else {
			if(isAlreadyTaken(user, book))
				System.out.println("Book is already rented");
			if(!Check.userValid(user))
				System.out.println("UserId not valid");
			if(!Check.bookValid(book))
				System.out.println("BookId not valid");
			if(Check.isBookLimitExceded(user.getUserId()))
				System.out.println("User's book count is over limit");
			if(!Check.isBookAvailable(book.getBookId()))
				System.out.println("Book isn't available");
			return false;
		}
		
		return false;
	}
	
	
	public static boolean returnBook(User user, Book book) throws Exception{
		
		if(Check.userValid(user) && Check.bookValid(book)) {
			
			String sql = "DELETE FROM possession WHERE userId = ? AND bookId = ?";
			String numTaken = "UPDATE user SET numOfBooksTaken = ? "
					+ "WHERE userId = ?";
			
			try(
					PreparedStatement stmt = conn.prepareStatement(sql);
					PreparedStatement num = conn.prepareStatement(numTaken);
					) {
				
				stmt.setInt(1, user.getUserId());
				stmt.setInt(2, book.getBookId());
				
				int numOfBooks = user.getNumOfBooksTaken();
				user.setNumOfBooksTaken(numOfBooks - 1);
				
				int affected = stmt.executeUpdate();
				if(affected == 1) {
					num.setInt(1, --numOfBooks);
					num.setInt(2, user.getUserId());
					num.executeUpdate();
					BookManager.addSameBook(book);
					return true;
				}
				
				return false;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		else {
			if(!Check.userValid(user))
				System.out.println("UserId not valid");
			if(!Check.bookValid(book))
				System.out.println("BookId not valid");
	
			return false;
		}
		
	}
	
	public static boolean returnAllBooks(User user) throws Exception {
		
			String selectBooks = "SELECT bookName FROM book WHERE bookId IN "
					+ "(SELECT bookId FROM possession WHERE userId = ?)";
			String sql = "DELETE FROM possession WHERE userId = ?";
			String numTaken = "UPDATE user SET numOfBooksTaken = 0 "
					+ "WHERE userId = ?";
			ResultSet rs = null;
			try(
					PreparedStatement stmt = conn.prepareStatement(sql);
					PreparedStatement slct = conn.prepareStatement(selectBooks);
					PreparedStatement num = conn.prepareStatement(numTaken);
					) {
				
				slct.setInt(1, user.getUserId());
				stmt.setInt(1, user.getUserId());
				num.setInt(1, user.getUserId());
				num.executeUpdate();
				
				rs = slct.executeQuery();
				
				while(rs.next()) {
					Book insert = new Book();
					insert.setBookName(rs.getString("bookName"));
					BookManager.addSameBook(insert);
				}
				
				stmt.executeUpdate();
				user.setNumOfBooksTaken(0);
				
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		
	}
}
