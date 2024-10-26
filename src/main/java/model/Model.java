package model;

import dao.*;

import java.sql.SQLException;
import java.util.List;

public class Model {
    private UserDao userDao;
    private User currentUser;
    private BookShop bookShop;
    private BookShopDao bookShopDao;
    private CartDao cartDao;
    private OrdersDao ordersDao;

    public Model() {
        userDao = new UserDaoImpl();
        bookShopDao = new BookShopDao();
        bookShop = new BookShop();
        cartDao = new CartDao();
        ordersDao = new OrdersDao();
    }

    public void setup() throws SQLException {
        userDao.setup();
        bookShopDao.setup();
        bookShopDao.setupBooks();
        cartDao.createTable();
        ordersDao.createTable();
        bookShop.loadBooksIntoBookShop(bookShopDao, bookShop);
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public BookShop getBookShop() {
        return bookShop;
    }

    // Cart-related methods

    public void addBookToCart(String bookName, int quantity, double price) {
        if (currentUser != null) {
            cartDao.addBookToCart(currentUser.getUsername(), bookName, quantity, price);
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public List<Cart> viewCart() {
        if (currentUser != null) {
            return cartDao.viewCart(currentUser.getUsername());
        } else {
            System.out.println("No user is currently logged in.");
            return null;
        }
    }

    public void removeBookFromCart(int cartID) {
        cartDao.removeBookFromCart(cartID);
    }

    public void updateCartQuantity(String bookName, int quantity) {
        if (currentUser != null) {
            cartDao.updateQuantity(currentUser.getUsername(), quantity, bookName);
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void clearCart() {
        if (currentUser != null) {
            cartDao.clearCart(currentUser.getUsername());
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public double getCartTotal() {
        double total = 0.0;
        if (currentUser != null) {
            List<Cart> cartItems = cartDao.viewCart(currentUser.getUsername());
            for (Cart item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }
        } else {
            System.out.println("No user is currently logged in.");
        }
        return total;
    }

    // Orders-related methods
    public List<Orders> viewOrders() {
        if (currentUser != null) {
            return ordersDao.viewOrders(currentUser.getUsername());
        } else {
            System.out.println("No user is currently logged in.");
            return null;
        }
    }

    public void addOrder(String username, String bookName, int quantity, double price) {
        if (currentUser != null) {
            ordersDao.addOrder(username, bookName, quantity, price);
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public double getOrderTotal() {
        double total = 0.0;
        if (currentUser != null) {
            List<Orders> orders = ordersDao.viewOrders(currentUser.getUsername());
            for (Orders order : orders) {
                total += order.getPrice() * order.getQuantity();
            }
        } else {
            System.out.println("No user is currently logged in.");
        }

        return total;

    }
}
