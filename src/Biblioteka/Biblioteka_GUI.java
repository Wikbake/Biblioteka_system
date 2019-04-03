package Biblioteka;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Biblioteka_GUI extends JFrame {


  public Biblioteka_GUI() {

    try {
      connection = DriverManager.getConnection("jdbc:postgresql:Data_Lib", "postgres", "postgres");

      Statement statement = connection.createStatement();
      statement.setQueryTimeout(5);
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS Books(" +
              "Book_Id serial PRIMARY KEY, " +
              "Book_name VARCHAR(50) NOT NULL, " +
              "Cost INTEGER NOT NULL, " +
              "YearOfPublishment INTEGER NOT NULL, " +
              "Genre VARCHAR(50) NOT NULL, " +
              "Author VARCHAR(50) NOT NULL, " +
              "Age INTEGER NOT NULL, " +
              "Availability VARCHAR(5) NOT NULL)");
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS Customers(" +
              "Customer_Id serial PRIMARY KEY, " +
              "Surname VARCHAR(50) NOT NULL, " +
              "Name VARCHAR(50) NOT NULL, " +
              "Adress VARCHAR(255) NOT NULL, " +
              "Phone INTEGER NOT NULL)");
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS Rented_books(" +
              "Rental_Id serial PRIMARY KEY, " +
              "Book_Id INTEGER NOT NULL, " +
              "Customer_Id INTEGER NOT NULL, " +
              "Rental_date timestamp without time zone NOT NULL, " +
              "Book_name VARCHAR(50) NOT NULL, " +
              "Return_date timestamp without time zone NOT NULL, " +
              "CONSTRAINT rented_books_book_id_fkey FOREIGN KEY (Book_Id) REFERENCES Books (Book_Id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, " +
              "CONSTRAINT rented_books_customer_id_fkey FOREIGN KEY (Customer_Id) REFERENCES Customers (Customer_Id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION)");
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS Returns(" +
              "Rental_Id INTEGER PRIMARY KEY, " +
              "Customer_Id INTEGER NOT NULL, " +
              "Book_name VARCHAR(50) NOT NULL, " +
              "Return_date timestamp without time zone NOT NULL, " +
              "CONSTRAINT returns_rental_id_fkey FOREIGN KEY (Rental_Id) REFERENCES Rented_books (Rental_Id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION)");
    } catch (SQLException ex) {
      Logger.getLogger(Biblioteka_GUI.class.getName()).log(Level.SEVERE, null, ex);
    }


    add(Library);
    this.setTitle("Library");
    this.setBounds(0, 0, 250, 250);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();

    addBookButton.addActionListener(this::addBookActionPerformed);
    addCustomerButton.addActionListener(this::addCustomerActionPerformed);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        formWindowOpened(e);
      }
    });
    rentalButton.addActionListener(this::addNewRentalActionPerformed);
  }

  Connection connection;
  private List<Book> books = new ArrayList<Book>();
  private List<Customer> customers = new ArrayList<Customer>();
  private Map<Customer, List<Rental>> customersRentals = new HashMap<Customer, List<Rental>>();
  private Map<Customer, List<Rental>> rentals = new HashMap<Customer, List<Rental>>();

  private void formWindowOpened(WindowEvent e) {
    functionBook();
    functionCustomer();

  }

  private void addBookActionPerformed(ActionEvent e) {
    String bookName = bookNameEnter.getText();
    float cost = Float.parseFloat(costEnter.getText());
    int year = Integer.parseInt(yearEnter.getText());
    String genre = typeEnter.getText();
    String author = authorEnter.getText();
    int age = Integer.parseInt(ageEnter.getText());
    String availability = availabilityEnter.getText();

    String sqlq = "INSERT INTO Books (Book_name, Cost, YearOfPublishment, Genre, Author, Age, Availability) VALUES (?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement ps;
    try {
      ps = connection.prepareStatement(sqlq);
      ps.setString(1, bookName);
      ps.setFloat(2, cost);
      ps.setInt(3, year);
      ps.setString(4, genre);
      ps.setString(5, author);
      ps.setInt(6, age);
      ps.setString(7, availability);
      ps.executeUpdate();

      booksComboBox.removeAllItems();
      functionBook();
    } catch (SQLException ex) {
      Logger.getLogger(Biblioteka_GUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void addCustomerActionPerformed(ActionEvent e) {
    String n1 = nameEnter.getText();
    String s1 = surnameEnter.getText();
    String a1 = adressEnter.getText();
    int p1 = Integer.parseInt(phoneEnter.getText());

    String sqlq = "INSERT INTO Customers(Surname, Name, Adress, Phone) VALUES (?, ?, ?, ?)";
    PreparedStatement ps;
    try {
      ps = connection.prepareStatement(sqlq);
      ps.setString(1, n1);
      ps.setString(2, s1);
      ps.setString(3, a1);
      ps.setInt(4, p1);
      ps.executeUpdate();

      clientsComboBox.removeAllItems();
      functionCustomer();
    } catch (SQLException ex) {
      Logger.getLogger(Biblioteka_GUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void functionBook() {
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT Book_Id, Book_name, Cost, YearOfPublishment, Genre, Author, Age, Availability FROM Books");
      while (rs.next()) {
        int id1 = rs.getInt("Book_Id");
        String n1 = rs.getString("Book_name");
        float c1 = rs.getFloat("Cost");
        int y1 = rs.getInt("YearOfPublishment");
        String g1 = rs.getString("Genre");
        String a1 = rs.getString("Author");
        int age1 = rs.getInt("Age");
        String ava1 = rs.getString("Availability");
        booksComboBox.addItem("Id: " + id1 + " Name: " + n1 + " Cost: " + c1 + "zl. Available: " + ava1);
        Book book = new Book(id1, n1, c1, y1, g1, a1, age1, ava1);
        books.add(book);
      }
    } catch (SQLException ex) {
      Logger.getLogger(Biblioteka_GUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void functionCustomer() {
    try {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT Customer_Id, Surname, Name, Adress, Phone FROM Customers");
      while (rs.next()) {
        int id1 = rs.getInt("Customer_Id");
        String s1 = rs.getString("Surname");
        String n1 = rs.getString("Name");
        String a1 = rs.getString("Adress");
        int p1 = rs.getInt("Phone");
        clientsComboBox.addItem(id1 + " " + s1 + " " + n1 + " " + a1 + " " + p1);
        Customer customer = new Customer(id1, s1, n1, a1, p1);
        customers.add(customer);
      }
    } catch (SQLException ex) {
      Logger.getLogger(Biblioteka_GUI.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void addNewRentalActionPerformed(ActionEvent e) {
    String sqlq = "INSERT INTO Rented_books (Book_Id, Customer_Id, Rental_date, Book_name, Return_date) VALUES (?, ?, ?, ?, ?)";
    try {
      PreparedStatement statement = connection.prepareStatement(sqlq);
      int index1 = booksComboBox.getSelectedIndex();
      int index2 = clientsComboBox.getSelectedIndex();
      Book book = books.get(index1);
      Customer customer = customers.get(index2);
      String date = rentalEnter.getText();
      Date d = null;
      SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-mm-dd");
      if ("YES".equals(book.getAvailability())) {
        try {
          d = (Date) parserSDF.parse(date);
        } catch (ParseException exc) {
          Logger.getLogger(Biblioteka_GUI.class.getName()).log(Level.SEVERE, null, exc);
        }
        statement.setInt(1, book.getIdBook());
        statement.setInt(2, customer.getIdCustomer());
        Date sqlDate = new Date(d.getTime());
        statement.setDate(3, sqlDate);
        statement.setString(4, book.getBookName());
        long da = d.getTime() + 14 * 60 * 60 * 24 * 1000;
        Date sqlDate1 = new Date(da);
        statement.setDate(5, sqlDate1);
        statement.executeUpdate();

        DefaultListModel model = new DefaultListModel();
        model.addElement(book.getBookName() + " " + parserSDF.format(d));
        rentalList.setModel(model);
        String notAvailable = "NO";
        book.setAvailability(notAvailable);
        String sqlUpd = "UPDATE Books SET Availability = 'NO' WHERE Book_Id = (?)";
        PreparedStatement statementUpd = connection.prepareStatement(sqlUpd);
        statementUpd.setInt(1, book.getIdBook());
        statementUpd.executeUpdate();
        updateBooks();
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private void updateBooks() {
    booksComboBox.removeAllItems();
    for (Book b : books)
      booksComboBox.addItem("Id: " + b.getIdBook() + " Name: " + b.getBookName() + " Cost: " + b.getCost() + "zl. Available: " + b.getAvailability());
  }


  private JTextField exceededRentalTimeList;
  private JTextField bookNameEnter;
  private JTextField ageEnter;
  private JTextField authorEnter;
  private JTextField typeEnter;
  private JTextField yearEnter;
  private JTextField costEnter;
  private JTextField availabilityEnter;
  private JPanel Books;
  private JButton addBookButton;
  private JComboBox booksComboBox;
  private JPanel Library;
  private JTextField surnameEnter;
  private JTextField phoneEnter;
  private JTextField adressEnter;
  private JTextField nameEnter;
  private JButton addCustomerButton;
  private JComboBox clientsComboBox;
  private JTextField rentalEnter;
  private JButton rentalButton;
  private JList rentalList;
  private JList returnList;
  private JTextField yyyyMmDdTextField;
  private JButton addReturnButton;

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> new Biblioteka_GUI().setVisible(true));
  }


  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    Library = new JPanel();
    Library.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    Books = new JPanel();
    Books.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(11, 2, new Insets(0, 0, 0, 0), -1, -1));
    Library.add(Books, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(600, -1), null, null, 0, false));
    Books.setBorder(BorderFactory.createTitledBorder("Books"));
    bookNameEnter = new JTextField();
    bookNameEnter.setText("");
    Books.add(bookNameEnter, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    ageEnter = new JTextField();
    Books.add(ageEnter, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    authorEnter = new JTextField();
    Books.add(authorEnter, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    typeEnter = new JTextField();
    Books.add(typeEnter, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    yearEnter = new JTextField();
    Books.add(yearEnter, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    costEnter = new JTextField();
    Books.add(costEnter, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    availabilityEnter = new JTextField();
    Books.add(availabilityEnter, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Name");
    Books.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(158, 16), null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Cost");
    Books.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(158, 16), null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("Year of publishment");
    Books.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(158, 16), null, 0, false));
    final JLabel label4 = new JLabel();
    label4.setText("Genre");
    Books.add(label4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(158, 16), null, 0, false));
    final JLabel label5 = new JLabel();
    label5.setText("Author");
    Books.add(label5, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(158, 16), null, 0, false));
    final JLabel label6 = new JLabel();
    label6.setText("Minimal Age");
    Books.add(label6, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(158, 16), null, 0, false));
    final JLabel label7 = new JLabel();
    label7.setText("Availability");
    Books.add(label7, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(158, 16), null, 0, false));
    addBookButton = new JButton();
    addBookButton.setText("Add book");
    Books.add(addBookButton, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(79, 23), new Dimension(100, 23), new Dimension(100, 23), 0, false));
    booksComboBox = new JComboBox();
    Books.add(booksComboBox, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
    Books.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(18, 18), new Dimension(18, 18), 0, false));
    final JLabel label8 = new JLabel();
    label8.setText("Books");
    Books.add(label8, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
    Library.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(600, -1), null, null, 0, false));
    panel1.setBorder(BorderFactory.createTitledBorder("Customers"));
    surnameEnter = new JTextField();
    panel1.add(surnameEnter, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    phoneEnter = new JTextField();
    panel1.add(phoneEnter, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    adressEnter = new JTextField();
    panel1.add(adressEnter, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    nameEnter = new JTextField();
    panel1.add(nameEnter, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(130, 30), null, null, 0, false));
    final JLabel label9 = new JLabel();
    label9.setText("Surname");
    panel1.add(label9, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label10 = new JLabel();
    label10.setText("Name");
    panel1.add(label10, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label11 = new JLabel();
    label11.setText("Adress");
    panel1.add(label11, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label12 = new JLabel();
    label12.setText("Phone");
    panel1.add(label12, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
    panel1.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(18, 18), new Dimension(18, 18), 0, false));
    addCustomerButton = new JButton();
    addCustomerButton.setText("Add customer");
    panel1.add(addCustomerButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(79, 23), new Dimension(100, 23), new Dimension(100, 23), 0, false));
    final JLabel label13 = new JLabel();
    label13.setText("Clients");
    panel1.add(label13, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    clientsComboBox = new JComboBox();
    panel1.add(clientsComboBox, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
    Library.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panel2.setBorder(BorderFactory.createTitledBorder("Books returned by a customer"));
    final JLabel label14 = new JLabel();
    label14.setText("Returns");
    panel2.add(label14, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    returnList = new JList();
    panel2.add(returnList, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(350, 100), new Dimension(350, 100), null, 1, false));
    final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
    panel2.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, -1), null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
    panel2.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
    panel2.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, -1), null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
    panel2.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 15), null, null, 0, false));
    final JLabel label15 = new JLabel();
    label15.setText("Return date");
    panel2.add(label15, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 16), null, null, 0, false));
    addReturnButton = new JButton();
    addReturnButton.setText("Add return");
    panel2.add(addReturnButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 23), null, null, 0, false));
    yyyyMmDdTextField = new JTextField();
    yyyyMmDdTextField.setText("yyyy-mm-dd");
    panel2.add(yyyyMmDdTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, 30), new Dimension(100, 30), null, 0, false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 5, new Insets(0, 0, 0, 0), -1, -1));
    Library.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panel3.setBorder(BorderFactory.createTitledBorder("Books of the selected customer"));
    final JLabel label16 = new JLabel();
    label16.setText("Rental date");
    panel3.add(label16, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 16), null, 0, false));
    rentalEnter = new JTextField();
    rentalEnter.setText("yyyy-mm-dd");
    panel3.add(rentalEnter, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(100, -1), new Dimension(100, 30), new Dimension(100, -1), 0, false));
    rentalButton = new JButton();
    rentalButton.setText("Add new rental");
    panel3.add(rentalButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(120, 23), null, new Dimension(-1, 23), 0, false));
    rentalList = new JList();
    panel3.add(rentalList, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(350, 100), new Dimension(350, 100), null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
    panel3.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, -1), null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
    panel3.add(spacer8, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(10, -1), null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer9 = new com.intellij.uiDesigner.core.Spacer();
    panel3.add(spacer9, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(20, 10), null, null, 0, false));
    final com.intellij.uiDesigner.core.Spacer spacer10 = new com.intellij.uiDesigner.core.Spacer();
    panel3.add(spacer10, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 15), null, null, 0, false));
    final JLabel label17 = new JLabel();
    label17.setText("Rentalled books");
    panel3.add(label17, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    Library.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panel4.setBorder(BorderFactory.createTitledBorder("Customers who have exceeded the rental time"));
    exceededRentalTimeList = new JTextField();
    panel4.add(exceededRentalTimeList, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(8, 80), new Dimension(8, 80), new Dimension(32767, 32767), 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return Library;
  }


}
