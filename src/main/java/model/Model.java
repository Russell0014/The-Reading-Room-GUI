package model;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.BookShopDao;
import dao.UserDao;
import dao.UserDaoImpl;

public class Model {
	private UserDao userDao;
	private User currentUser;
	private BookShop bookShop;
	private BookShopDao bookShopDao;
	
	public Model() {
		userDao = new UserDaoImpl();
		bookShopDao = new BookShopDao();
		bookShop = new BookShop();
	}
	
	public void setup() throws SQLException {
		userDao.setup();
		bookShopDao.setup();
		bookShopDao.setupBooks();
		bookShop.loadBooksIntoBookShop(bookShopDao,bookShop);
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
}
