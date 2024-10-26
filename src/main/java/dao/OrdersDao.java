package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class OrdersDao {

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS cart (
                    orderID INTEGER PRIMARY KEY AUTOINCREMENT,
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
}
