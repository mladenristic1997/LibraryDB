package library.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import library.beans.Book;
import library.db.ConnectionManager;

public class BookManager {
private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static void displayAllRows() throws SQLException {
		
		String sql = "SELECT * FROM book";
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				){
			
			System.out.println("Book table");
			System.out.println("BookId:\t\tBook Name:\tNumber of available:");
				while (rs.next()) {
					StringBuffer bf = new StringBuffer();
					bf.append(rs.getInt("bookId") + "\t\t ");
					bf.append(rs.getString("bookName") + "\t\t ");
					bf.append(rs.getInt("numOfAvailable"));
					System.out.println(bf.toString());
				}
		}
		
	}
	
	public static Book getRow(int bookId) throws SQLException {
		
		String sql = "SELECT * FROM book WHERE bookId = ?";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, bookId);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				Book bean = new Book();
				bean.setBookId(bookId);;
				bean.setBookName(rs.getString("bookName"));
				bean.setNumAvailable(rs.getInt("numOfAvailable"));
				return bean;
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
	}
	
	public static boolean isExists(int bookId) throws Exception {
		
		String sql = "SELECT EXISTS(SELECT * FROM book WHERE bookId = ?) as 'Exists'";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setInt(1, bookId);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("Exists") == 1;
			}
		} catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	public static boolean isExists(String bookName) throws Exception {
		
		String sql = "SELECT EXISTS(SELECT * FROM book WHERE bookName = ?) as 'Exists'";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setString(1, bookName);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("Exists") == 1;
			}
		} catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	public static boolean insert(Book bean) throws Exception {
	
		if(isExists(bean.getBookName())) {
			addSameBook(bean);
		}
		else {
			String sql = "INSERT INTO book VALUES (default, ?, default)";
				
				try (
						PreparedStatement stmt = conn.prepareStatement(sql);
						) {
					
					stmt.setString(1, bean.getBookName());
					stmt.executeUpdate();
				
				} catch (SQLException e) {
					System.err.println(e);
					e.printStackTrace();
					return false;
				}
			return true;
			}
			
		return true;
	}
	
	
	public static void addSameBook(Book bean) throws Exception {
		
		String src = "SELECT numOfAvailable AS 'num' FROM book WHERE bookName = ?";
		String sql = "UPDATE book SET numOfAvailable = ? " +
				"WHERE bookName = ?";
		ResultSet rs = null;
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				PreparedStatement search = conn.prepareStatement(src);
				){
			
			search.setString(1, bean.getBookName());
			rs = search.executeQuery();
			
			int numOfAvailable = 0;
			rs.next();
			numOfAvailable = rs.getInt("num");
			
			stmt.setInt(1, ++numOfAvailable);
			stmt.setString(2, bean.getBookName());
			
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		
	}
	
	//Delete one book from the row
	public static boolean removeSameBook(Book bean) throws Exception {
			
		String src = "SELECT numOfAvailable AS 'num' FROM book WHERE bookName = ?";
		String sql = "UPDATE book SET numOfAvailable = ? " +
				"WHERE bookName = ?";
		ResultSet rs = null;
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				PreparedStatement search = conn.prepareStatement(src);
				){
			
			search.setString(1, bean.getBookName());
			rs = search.executeQuery();
			
			int numOfAvailable = 0;
			rs.next();
			numOfAvailable = rs.getInt("num");
			
			stmt.setInt(1, --numOfAvailable);
			stmt.setString(2, bean.getBookName());
			
			stmt.executeUpdate();		
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
		return true;
	}
	
	//Completely delete the row (all the books)
	public static boolean delete(int bookId) throws Exception {
		
		String sql = "DELETE FROM book WHERE bookId = ?";
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setInt(1, bookId);
			int affected = stmt.executeUpdate();
			
			if(affected == 1) {
				return true;
			} else {
				return false;
			}
			
		}
		catch(SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			return false;
		}
	}
	
}
