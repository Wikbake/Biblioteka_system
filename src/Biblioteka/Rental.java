package Biblioteka;

import java.util.Date;

public class Rental {

  int idRental;
  int idBook;
  String bookName;
  Date dateReturn;
  Date dateRental;

  public int getIdRental() { return idRental; }
  public int getIdBook() { return idBook; }
  public Date getDateRental() { return dateRental; }
  public Date getDateReturn() { return dateReturn; }
  public String getBookName() {return bookName; }

  public void setIdRental(int idRental) { this.idRental = idRental; }
  public void setIdBook(int idBook) { this.idBook = idBook; }
  public void setBookName(String bookName) { this.bookName = bookName; }
  public void setDateReturn(Date dateReturn) { this.dateReturn = dateReturn; }
  public void setDateRetnal(Date dateRental) { this.dateRental = dateRental; }

  public Rental(int idRental, int idBook, String bookName, Date dateReturn, Date dateRental) {
    this.idRental = idRental;
    this.idBook = idBook;
    this.bookName = bookName;
    this.dateReturn = dateReturn;
    this.dateRental = dateRental;
  }

  @Override
  public int hashCode() {
    return bookName.hashCode();
  }
}
