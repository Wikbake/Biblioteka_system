package Biblioteka;

public class Customer {

  private int idCustomer;
  private String surname;
  private String name;
  private String adress;
  private int phone;

  public int getIdCustomer() {
    return idCustomer;
  }
  public String getSurname() {
    return surname;
  }

  public String getName() {
    return name;
  }

  public String getAdress() {
    return adress;
  }

  public int getPhone() {
    return phone;
  }

  public void setIdCustomer(int idCustomer) {
    this.idCustomer = idCustomer;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAdress(String adress) {
    this.adress = adress;
  }

  public void setPhone(int phone) {
    this.phone = phone;
  }

  public Customer(int idCustomer, String surname, String name, String adress, int phone) {
    this.idCustomer = idCustomer;
    this.surname = surname;
    this.name = name;
    this.adress = adress;
    this.phone = phone;
  }

  @Override
  public int hashCode() {
    return surname.hashCode();
  }

  @Override
  public String toString() {
    return idCustomer + " " + surname + " " + name;
  }
}
