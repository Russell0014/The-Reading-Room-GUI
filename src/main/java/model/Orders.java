package model;

public class Orders {
    private int orderID;
    private String username;
    private String bookName;
    private int quantity;
    private Double price;
    private String orderDate;

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

    public String getOrderDate() { return orderDate; }

    public int getQuantity() { return quantity; }

    public Double getPrice() { return price; }


}
