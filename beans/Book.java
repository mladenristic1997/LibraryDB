package library.beans;

public class Book {
	private int bookId;
	private String bookName;
	private int numAvailable;
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public int getNumAvailable() {
		return numAvailable;
	}
	public void setNumAvailable(int numAvailable) {
		this.numAvailable = numAvailable;
	}
	@Override
	public String toString() {
		System.out.println("BookId:\t\tBookName:\tNumber of available");
		return "" + bookId + "\t " + bookName + "\t\t " + numAvailable;
	}
}
