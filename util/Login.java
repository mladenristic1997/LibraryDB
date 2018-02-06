package library.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.beans.Admin;
import library.beans.User;
import library.db.ConnectionManager;

public class Login {

	private static Connection conn = ConnectionManager.getInstance().getConnection();

	public static boolean loginAdmin(Admin bean) throws Exception {
		
		if(Check.adminValid(bean)) {
			String sql = "SELECT * FROM admin WHERE userName = ?";
			ResultSet rs = null;
			
			try(
					PreparedStatement stmt = conn.prepareStatement(sql);
					){
				
				stmt.setString(1, bean.getUserName());
				rs = stmt.executeQuery();
				rs.next();
				
				if(bean.getUserName().equals(rs.getString("userName"))
						&& bean.getPassword().equals(rs.getString("password"))) {
					bean.setAdminId(rs.getInt("adminId"));
					return true;
				}
				else {
					System.out.println("Incorrect username/password");
					return false;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		else {
			System.out.println("Account doesn't exist");
			return false;
		}
	}
	
	public static boolean loginUser(User bean) throws Exception {
		
		if(Check.userValid(bean)) {
			String sql = "SELECT * FROM user WHERE userName = ?";
			ResultSet rs = null;
			
			try(
					PreparedStatement stmt = conn.prepareStatement(sql);
					){
				
				stmt.setString(1, bean.getUserName());
				rs = stmt.executeQuery();
				rs.next();
				
				if(bean.getUserName().equals(rs.getString("userName"))
						&& bean.getPassword().equals(rs.getString("password"))) {
					bean.setUserId(rs.getInt("userId"));
					bean.setNumOfBooksTaken(rs.getInt("numOfBooksTaken"));
					return true;
				}
				else {
					System.out.println("Incorrect username/password");
					return false;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		else {
			System.out.println("Account doesn't exist");
			return false;
		}
	}
}
