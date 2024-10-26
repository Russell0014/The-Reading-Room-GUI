package model;

public class Cart {

    private int cartID;
    private String username;
    private String bookName;
    private int quantity;
    private Double price;

    public Cart(int cartID, String username, String bookName, int quantity, Double price) {
        this.cartID = cartID;
        this.username = username;
        this.bookName = bookName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getCartID() { return cartID; }
    public void setCartID(int cartID) { this.cartID = cartID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

}
