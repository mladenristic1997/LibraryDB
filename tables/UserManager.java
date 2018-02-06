package library.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import library.beans.User;
import library.db.ConnectionManager;

public class UserManager {
	
private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static void displayAllRows() throws SQLException {
		
		String sql = "SELECT * FROM user";
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				){
			
			System.out.println("User table");
			System.out.println("UserId:\t\tUserName:\tPassword\tNumber of books taken");
			
				while (rs.next()) {
					StringBuffer bf = new StringBuffer();
					bf.append(rs.getInt("userId") + "\t\t ");
					bf.append(rs.getString("userName") + "\t\t ");
					bf.append(rs.getString("password") + "\t\t ");
					bf.append(rs.getInt("numOfBooksTaken"));
					System.out.println(bf.toString());
				}
		}
		
	}
	
	public static User getRow(int userId) throws SQLException {
		
		String sql = "SELECT * FROM user WHERE userId = ?";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				User bean = new User();
				bean.setUserId(userId);;
				bean.setUserName(rs.getString("userName"));
				bean.setPassword(rs.getString("password"));
				bean.setNumOfBooksTaken(rs.getInt("numOfBooksTaken"));
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
	
	public static boolean isExists(int userId) throws Exception {
		
		String sql = "SELECT EXISTS(SELECT * FROM user WHERE userId = ?) as 'Exists'";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setInt(1, userId);
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
	
	public static boolean insert(User bean) throws Exception {
		
	String sql = "INSERT INTO user VALUES (default, ?, ?, default)";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setString(1, bean.getUserName());
			stmt.setString(2, bean.getPassword());
			stmt.executeUpdate();
		
		} catch (SQLException e) {
			System.err.println(e);
			return false;
		}
		
		return true;
	}
	
	public static boolean update(User bean) throws Exception {
		
		String sql = "UPDATE user SET userName = ?, password = ? " +
				"WHERE userId = ?";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setString(1, bean.getUserName());
			stmt.setString(2, bean.getPassword());
			stmt.setInt(3, bean.getUserId());
			
			int affected = stmt.executeUpdate();
			if (affected == 1) {
				return true;
			} else {
				return false;
			}
		}
		catch(SQLException e) {
			System.err.println(e);
			return false;
		}
		
	}
	
	public static boolean delete(int userId) throws Exception {
		
		String sql = "DELETE FROM user WHERE userId = ?";
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setInt(1, userId);
			int affected = stmt.executeUpdate();
			
			if(affected == 1) {
				return true;
			} else {
				return false;
			}
			
		}
		catch(SQLException e) {
			System.err.println(e);
			return false;
		}
				
	}

	public static void getRentedBooks(User bean) throws SQLException {
		
		String sql = "SELECT bookName FROM book WHERE bookId IN "
			+ "(SELECT bookId FROM possession WHERE userId = ?)";
		ResultSet rs = null;
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setInt(1, bean.getUserId());
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getString("bookName"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

