package Biblioteka;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    booksButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addBookActionPerformed(e);
      }
    });
  }

  Connection connection;


  private void addBookActionPerformed(ActionEvent e) {
    System.out.println("Hello!"); // do zmodyfikowania
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
  private JButton booksButton;
  private JComboBox booksComboBox;
  private JPanel Library;
  private JTextField surnameEnter;
  private JTextField phoneEnter;
  private JTextField adressEnter;
  private JTextField nameEnter;
  private JButton clientsButton;
  private JComboBox clientsComboBox;
  private JTextField rentalEnter;
  private JButton rentalButton;
  private JList rentalList;
  private JList returnList;
  private JTextField yyyyMmDdTextField;
  private JButton addReturnButton;

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Biblioteka_GUI().setVisible(true);
      }
    });
  }


}
