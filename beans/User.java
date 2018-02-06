package library.beans;

public class User {
	private int userId;
	private String userName;
	private String password;
	private int numOfBooksTaken;
	
	public int getNumOfBooksTaken() {
		return numOfBooksTaken;
	}
	public void setNumOfBooksTaken(int numOfBooksTaken) {
		this.numOfBooksTaken = numOfBooksTaken;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		System.out.println("UserId:\t\tUserName:\tPassword:\tNumber of books taken");
		return "" + userId + "\t\t " + userName + "\t\t " + password + "\t\t " + numOfBooksTaken;
	}
	
}
