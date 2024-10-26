package dao;

import model.Cart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDao {

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS cart (
                    cartID INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    bookName TEXT NOT NULL,
                    quantity INTEGER,
                    price REAL,
                    FOREIGN KEY(username) REFERENCES users(username),
                    FOREIGN KEY(bookName) REFERENCES books(title)
                );
                """;

        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean bookExistsInCart(String username, String bookName) {
        String sql = "SELECT COUNT(*) FROM cart WHERE username = ? AND bookName = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, bookName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void updateQuantityAndPrice(String username, String bookName, int additionalQuantity, double price) {
        String sql = "UPDATE cart SET quantity = quantity + ?, price = ?  WHERE username = ? AND bookName = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, additionalQuantity);
            stmt.setDouble(2, price);
            stmt.setString(3, username);
            stmt.setString(4, bookName);
            stmt.executeUpdate();
            System.out.println("Updated quantity for book in cart: " + bookName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBookToCart(String username, String bookName, int quantity, double price) {
        if (bookExistsInCart(username, bookName)) {
            updateQuantityAndPrice(username, bookName, quantity, price);
        } else {
            String sql = "INSERT INTO cart(username, bookName, quantity, price) VALUES(?, ?, ?, ?)";
            try (Connection connection = Database.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, bookName);
                stmt.setInt(3, quantity);
                stmt.setDouble(4, price );
                stmt.executeUpdate();
                System.out.println("Book added to cart: " + bookName);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<Cart> viewCart(String username) {
        List<Cart> cartList = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE username = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cart cart = new Cart(
                        rs.getInt("cartID"),
                        rs.getString("username"),
                        rs.getString("bookName"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                cartList.add(cart);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cartList;
    }

    public void removeBookFromCart(int cartID) {
        String sql = "DELETE FROM cart WHERE cartID = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, cartID);

            stmt.executeUpdate();
            System.out.println("Book removed from cart: " + cartID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateQuantity(String username, int quantity, String bookName) {
        String sql = "UPDATE cart SET quantity = ? WHERE username = ? AND bookName = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setString(2, username);
            stmt.setString(3, bookName);

            stmt.executeUpdate();
            System.out.println("Quantity updated for book: " + bookName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clearCart(String username) {
        String sql = "DELETE FROM cart WHERE username = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);

            stmt.executeUpdate();
            System.out.println("Cart cleared for user: " + username);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
