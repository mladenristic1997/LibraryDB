package library.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.beans.Admin;
import library.beans.Book;
import library.beans.User;
import library.db.ConnectionManager;

public class Check {
private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static boolean adminValid(Admin bean) throws Exception {
		
		String sql = "SELECT EXISTS(SELECT * FROM admin WHERE userName = ?) as 'Exists'";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setString(1, bean.getUserName());
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
	
	public static boolean userValid(User bean) throws Exception{
		
		String sql = "SELECT EXISTS(SELECT * FROM user WHERE userName = ?) as 'Exists'";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setString(1, bean.getUserName());
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
	
	public static boolean isBookLimitExceded(int userId) throws Exception {
		
			String sql = "SELECT numOfBooksTaken FROM user WHERE userId = ?";
			ResultSet rs = null;
			
			try(
					PreparedStatement stmt = conn.prepareStatement(sql);
					){
				
				stmt.setInt(1, userId);
				rs = stmt.executeQuery();
				
				if(rs.next()) {
					return rs.getInt("numOfBooksTaken") > 2;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
				
				return false;
		}

	public static boolean bookValid(Book bean) throws Exception{
		
		String sql = "SELECT EXISTS(SELECT * FROM book WHERE bookId = ?) as 'Exists'";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setInt(1, bean.getBookId());
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
	
	public static boolean isBookAvailable(int bookId) throws Exception{
		
		String sql = "SELECT numOfAvailable FROM book WHERE bookId = ?";
		ResultSet rs = null;
		
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setInt(1, bookId);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("numOfAvailable") >= 1;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
			
		return false;
	}
	
}
