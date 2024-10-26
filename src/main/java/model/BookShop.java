package model;

import dao.BookShopDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BookShop {
    private ArrayList<Book> books = new ArrayList<>();

    public BookShop() {
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public List<Book> getTop5BooksSold() {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getSoldCopies).reversed())  // Sort by sold copies in descending order
                .limit(5)  // Limit the result to 5 books
                .collect(Collectors.toList());  // Collect the result as a list
    }

    public void loadBooksIntoBookShop(BookShopDao bookShopDao, BookShop bookShop) throws SQLException {
        // Load books from database and add them to the bookShop
        ArrayList<Book> booksFromDatabase = bookShopDao.getAllBooks();
        for (Book book : booksFromDatabase) {
            bookShop.addBook(book);  // Add each book to the bookShop
        }
    }

    public ArrayList<Book> getBooks() {
        return books.stream()
                .sorted(Comparator.comparing(Book::getTitle))  // Sort by title
                .collect(Collectors.toCollection(ArrayList::new));  // Collect the result as an ArrayList
    }


}
