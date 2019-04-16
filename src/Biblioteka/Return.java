package Biblioteka;

import java.util.Date;

public class Return extends Rental{

    private Date dateReturnCustomer;

    public Date getDateReturnCustomer() { return dateReturnCustomer; }

    public void setDateReturnCustomer(Date dateReturnCustomer) { this.dateReturnCustomer = dateReturnCustomer; }

    public Return(int idRental, int idBook, String bookName, Date dateReturn, Date dateRental, Date dateReturnCustomer) {
        super(idRental, idBook, bookName, dateReturn, dateRental);
        this.dateReturnCustomer = dateReturnCustomer;
    }

    @Override
    public int hashCode() {
        return getBookName().hashCode();
    }
}
