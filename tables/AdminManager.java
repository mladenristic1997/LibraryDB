package library.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import library.beans.Admin;
import library.db.ConnectionManager;

public class AdminManager {
	
	private static Connection conn = ConnectionManager.getInstance().getConnection();
	
	public static void displayAllRows() throws SQLException {
		
		String sql = "SELECT * FROM admin";
		try (
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				){
			
			System.out.println("Admin table");
			System.out.println("AdminId:\t\tUserName:\tPassword");
				while (rs.next()) {
					StringBuffer bf = new StringBuffer();
					bf.append(rs.getInt("adminId") + "\t\t ");
					bf.append(rs.getString("userName") + "\t\t ");
					bf.append(rs.getString("password"));
					System.out.println(bf.toString());
				}
		}
		
	}
	
	public static Admin getRow(int adminId) throws SQLException {
		
		String sql = "SELECT * FROM admin WHERE adminId = ?";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			stmt.setInt(1, adminId);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				Admin bean = new Admin();
				bean.setAdminId(adminId);;
				bean.setUserName(rs.getString("userName"));
				bean.setPassword(rs.getString("password"));
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
	
	public static boolean isExists(int adminId) throws Exception {
		
		String sql = "SELECT EXISTS(SELECT * FROM admin WHERE adminId = ?) as 'Exists'";
		ResultSet rs = null;
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				) {
			
			stmt.setInt(1, adminId);
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
	
	public static boolean insert(Admin bean) throws Exception {

		String sql = "INSERT INTO admin VALUES (default, ?, ?)"; //ako ne radi, dodaj (default, ?, ?)
		
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
		
	
	public static boolean update(Admin bean) throws Exception {
		
		String sql = "UPDATE admin SET userName = ?, password = ? " +
				"WHERE adminId = ?";
		
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setString(1, bean.getUserName());
			stmt.setString(2, bean.getPassword());
			stmt.setInt(3, bean.getAdminId());
			
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
	
	public static boolean delete(int adminId) throws Exception {
		
		String sql = "DELETE FROM admin WHERE adminId = ?";
		try (
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			stmt.setInt(1, adminId);
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

	public static boolean isEmpty() throws SQLException {
		
		String sql = "SELECT COUNT(*) FROM admin";
		ResultSet rs = null;
		try(
				PreparedStatement stmt = conn.prepareStatement(sql);
				){
			
			rs = stmt.executeQuery();
			rs.next();
			return rs.getInt("COUNT(*)") == 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
