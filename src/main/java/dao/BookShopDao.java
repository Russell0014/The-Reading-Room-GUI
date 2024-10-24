package dao;

import model.Book;
import model.BookShop;

import java.sql.*;
import java.util.ArrayList;

public class BookShopDao {

    private final String TABLE_NAME = "books";

    public BookShopDao() {
    }

    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();) {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + "title VARCHAR(50) NOT NULL, "
                    + "author VARCHAR(50) NOT NULL, "
                    + "numberOfCopies INT NOT NULL, "
                    + "price DECIMAL(10, 2) NOT NULL, "
                    + "soldCopies INT NOT NULL, "
                    + "PRIMARY KEY (title))";
            stmt.executeUpdate(sql);
        }
    }

    public void addBook(String title, String author, int numberOfCopies, float price, int soldCopies) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, numberOfCopies);
            stmt.setFloat(4, price);
            stmt.setInt(5, soldCopies);

            stmt.executeUpdate();
        }
    }

    public void setupBooks() throws SQLException {
        try {
            addBook("Absolute Java", "Savitch", 10, 50.0f, 142);
            addBook("JAVA: How to Program", "Deitel and Deitel", 100, 70.0f, 475);
            addBook("Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 89.0f, 60);
            addBook("Java Software Solutions", "Lewis and Loftus", 500, 99.0f, 12);
            addBook("Java Program Design", "Cohoon and Davidson", 2, 29.0f, 86);
            addBook("Clean Code", "Robert Martin", 100, 45.0f, 300);
            addBook("Gray Hat C#", "Brandon Perry", 300, 68.0f, 178);
            addBook("Python Basics", "David Amos", 1000, 49.0f, 79);
            addBook("Bayesian Statistics The Fun Way", "Will Kurt", 600, 42.0f, 155);
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_PRIMARYKEY")) {
                System.out.println("Duplicate entry detected. Skipping insertion.");
            } else {
                throw e;  // Re-throw if it's a different type of SQLException
            }
        }
    }


    public ArrayList<Book> getAllBooks() throws SQLException {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT title, author, numberOfCopies, price, soldCopies FROM " + TABLE_NAME;

        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterate through the result set and add each book to the list
            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                int numberOfCopies = rs.getInt("numberOfCopies");
                float price = rs.getFloat("price");
                int soldCopies = rs.getInt("soldCopies");

                // Create a new Book object and add it to the list
                Book book = new Book(title, author, numberOfCopies, price, soldCopies);
                books.add(book);
            }
        }

        return books;  // Return the list of books
    }



}
