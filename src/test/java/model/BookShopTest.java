package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class BookShopTest {
    private BookShop bookShop;
    private Book book1, book2, book3, book4, book5, book6;

    @Before
    public void setUp() {
        bookShop = new BookShop();

        // Create test books with different sold copies
        book1 = new Book("Absolute Java", "Savitch", 10, 50.00, 142);
        book2 = new Book("JAVA: How to Program", "Deitel and Deitel", 100, 70.00, 475);
        book3 = new Book("Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 89.00, 60);
        book4 = new Book("Java Software Solutions", "Lewis and Loftus", 500, 99.00, 12);
        book5 = new Book("Clean Code", "Robert Martin", 100, 45.00, 300);
        book6 = new Book("Gray Hat C#", "Brandon Perry", 300, 68.00, 178);
    }

    @Test
    public void testAddBook() {
        bookShop.addBook(book1);
        assertEquals(1, bookShop.getBooks().size());
        assertEquals(book1, bookShop.getBooks().get(0));
    }

    @Test
    public void testGetTop5BooksSold() {
        // Add books in random order
        bookShop.addBook(book3); // 60 copies
        bookShop.addBook(book1); // 142 copies
        bookShop.addBook(book5); // 300 copies
        bookShop.addBook(book2); // 475 copies
        bookShop.addBook(book6); // 178 copies
        bookShop.addBook(book4); // 12 copies

        List<Book> top5Books = bookShop.getTop5BooksSold();

        assertEquals(5, top5Books.size());
        assertEquals(book2, top5Books.get(0)); // Most sold (475)
        assertEquals(book5, top5Books.get(1)); // Second most sold (300)
        assertEquals(book6, top5Books.get(2)); // Third most sold (178)
        assertEquals(book1, top5Books.get(3)); // Fourth most sold (142)
        assertEquals(book3, top5Books.get(4)); // Fifth most sold (60)
    }

    @Test
    public void testGetBooksReturnsAlphabeticalOrder() {
        // Add books in random order
        bookShop.addBook(book3); // "Computing Concepts with JAVA 8 Essentials"
        bookShop.addBook(book4); // "Java Software Solutions"
        bookShop.addBook(book1); // "Absolute Java"

        List<Book> sortedBooks = bookShop.getBooks();

        assertEquals(3, sortedBooks.size());
        assertEquals("Absolute Java", sortedBooks.get(0).getTitle());
        assertEquals("Computing Concepts with JAVA 8 Essentials", sortedBooks.get(1).getTitle());
        assertEquals("Java Software Solutions", sortedBooks.get(2).getTitle());
    }

    @Test
    public void testEmptyBookShopReturnsEmptyLists() {
        assertTrue(bookShop.getBooks().isEmpty());
        assertTrue(bookShop.getTop5BooksSold().isEmpty());
    }

    @Test
    public void testTop5BooksWithLessThan5Books() {
        bookShop.addBook(book1); // 142 copies
        bookShop.addBook(book2); // 475 copies
        bookShop.addBook(book3); // 60 copies

        List<Book> top5Books = bookShop.getTop5BooksSold();

        assertEquals(3, top5Books.size());
        assertEquals(book2, top5Books.get(0)); // Most sold (475)
        assertEquals(book1, top5Books.get(1)); // Second most sold (142)
        assertEquals(book3, top5Books.get(2)); // Least sold (60)
    }
}