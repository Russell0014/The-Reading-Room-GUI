package model;

import javafx.scene.control.CheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Orders {
    private int orderID;
    private String username;
    private String bookName;
    private int quantity;
    private Double price;
    private String orderDate;

    private CheckBox selected;

    public Orders(int orderID, String username, String bookName, int quantity, Double price, String orderDate) {
        this.orderID = orderID;
        this.username = username;
        this.bookName = bookName;
        this.quantity = quantity;
        this.price = price;
        this.orderDate = orderDate;
    }

    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBookName() {return bookName;}

    public String getOrderDate() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = inputFormat.parse(orderDate)
;
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // or handle the error as needed
        }
    }


    public int getQuantity() { return quantity; }

    public Double getPrice() { return price; }

    public CheckBox getSelected() {
        return selected;
    }

    public void setSelected(CheckBox selected) {
        this.selected = selected;
    }


}
