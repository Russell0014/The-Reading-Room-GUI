package model;

import java.sql.SQLException;
import java.util.List;

import dao.BookShopDao;
import dao.UserDao;
import dao.UserDaoImpl;
import dao.CartDao;

public class Model {
	private UserDao userDao;
	private User currentUser;
	private BookShop bookShop;
	private BookShopDao bookShopDao;
	private CartDao cartDao;

	public Model() {
		userDao = new UserDaoImpl();
		bookShopDao = new BookShopDao();
		bookShop = new BookShop();
		cartDao = new CartDao();
	}

	public void setup() throws SQLException {
		userDao.setup();
		bookShopDao.setup();
		bookShopDao.setupBooks();
		cartDao.createTable();
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
}
