package Biblioteka;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExceededRentalTime extends Thread{

    List<Customer> customers;
    Map<Customer, List<Rental>> customersRentals;
    List<Book> books;
    JTextField textField;

    ExceededRentalTime(List<Customer> customers, Map<Customer, List<Rental>> customersRentals, List<Book> books, JTextField textField) {
        this.customers = customers;
        this.customersRentals = customersRentals;
        this.books = books;
        this.textField = textField;
    }

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date d = new Date();
        long ds = d.getTime();
        String notifications = "";

        for (int i = 0; i < customers.size(); i++) {
            List<Rental> rentals = customersRentals.get(customers.get(i));
            if (rentals != null)
                for (Rental r : rentals)
                    for (Book b : books)
                        if (r.getIdBook() == b.getIdBook() && "No".equals(b.getAvailability())) {
                            Date z = r.getDateReturn();
                            long dz = z.getTime();
                            if (ds - dz > 14)
                                notifications += ("Customer: " + customers.get(i).getSurname() + " " + customers.get(i).getName() + " exceeded return date of the book: " + b.getBookName());
                        }
        }
        final String fNotifications = notifications;
        SwingUtilities.invokeLater(() -> textField.setText(fNotifications));
    }
}
