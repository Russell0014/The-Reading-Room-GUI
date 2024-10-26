package dao;

import model.Cart;
import model.Orders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDao {

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS orders (
                    orderID INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    bookName TEXT NOT NULL,
                    quantity INTEGER,
                    price REAL,
                    date TEXT DEFAULT CURRENT_TIMESTAMP,
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

    public void addOrder(String username, String bookName, int quantity, double price) {
        String sql = """
                INSERT INTO orders (username, bookName, quantity, price)
                VALUES (?, ?, ?, ?);
                """;

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, bookName);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Orders> viewOrders(String username) {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE username = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Orders orders = new Orders(
                        rs.getInt("orderID"),
                        rs.getString("username"),
                        rs.getString("bookName"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("date")
                );
                ordersList.add(orders);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ordersList;
    }
}
