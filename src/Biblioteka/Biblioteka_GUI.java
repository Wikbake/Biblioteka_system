package Biblioteka;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Biblioteka_GUI extends JFrame {

  public Biblioteka_GUI() {
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
  private JButton button1;

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Biblioteka_GUI().setVisible(true);
      }
    });
  }


}
