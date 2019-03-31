package Biblioteka;

public class Book {

  private int idBook;
  private String bookName;
  private float cost;
  private int yearOfPublishment;
  private String genre;
  private String author;
  private int age;
  private String availability;

  public int getIdBook() { return idBook; }
  public String getBookName() { return bookName; }
  public float getCost() { return cost; }
  public int getYearOfPublishment() { return yearOfPublishment; }
  public String getGenre() { return genre; }
  public String getAuthor() { return author; }
  public int getAge() { return age; }
  public String getAvailability() { return availability; }

  public void setIdBook(int idBook) { this.idBook = idBook; }
  public void setBookName(String bookName) { this.bookName = bookName; }
  public void setCost(float cost) { this.cost = cost; }
  public void setYearOfPublishment(int yearOfPublishment) { this.yearOfPublishment = yearOfPublishment; }
  public void setGenre(String genre) { this.genre = genre; }
  public void setAuthor(String author) {this.author = author; }
  public void setAge(int age) { this.age = age; }
  public void setAvailability(String availability) { this.availability = availability; }

  public Book(int idBook, String bookName, float cost, int yearOfPublishment, String genre, String author, int age, String availability) {
    this.idBook = idBook;
    this.bookName = bookName;
    this.cost = cost;
    this.yearOfPublishment = yearOfPublishment;
    this.genre = genre;
    this.author = author;
    this.age = age;
    this.availability = availability;
  }

  @Override
  public int hashCode() {
    return bookName.hashCode();
  }

  @Override
  public String toString() {
    return idBook + " " + bookName + " " + author + " " + yearOfPublishment + " " + age;
  }
}
