package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;
import model.Cart;
import model.Model;
import utils.WrappingTableCell;

import java.util.List;

public class CartController {
    private Model model;
    private Stage stage;
    private Stage parentStage;


    @FXML
    private MenuItem updateProfile;
    @FXML
    private MenuItem home;
    @FXML
    private MenuItem shop;
    @FXML
    private MenuItem cart;
    @FXML
    private MenuItem orders;
    @FXML
    private MenuItem logout;
    @FXML
    private TableView<Cart> tableView;
    @FXML
    private TableColumn<Cart, String> bookName;
    @FXML
    private TableColumn<Cart, String> bookPrice;
    @FXML
    private TableColumn<Cart, TextField> quantityColumn;
    @FXML
    private TableColumn<Cart, Button> deleteFromCartColumn;
    @FXML
    private Text total;
    @FXML
    private Button checkout;


    public CartController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        home.setOnAction(e -> NavigationHandler.handleHomeAction(stage, model));
        shop.setOnAction(e -> NavigationHandler.handleShopAction(stage, model));
        cart.setOnAction(e -> NavigationHandler.handleCartAction(stage, model));
        orders.setOnAction(e -> NavigationHandler.handleOrdersAction(stage, model));
        logout.setOnAction(e -> NavigationHandler.handleLoginAction(stage, model));
        updateProfile.setOnAction(e -> NavigationHandler.handleUpdateProfileAction(stage, model));

        loadCart();

        checkout.setOnAction(e -> {
            if (tableView.getItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cart is empty");
                alert.setContentText("Please add items to the cart before checking out.");
                alert.showAndWait();
                return;
            }

            // Check stock before checkout
            String outOfStockBook = checkStock();
            if (outOfStockBook != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Insufficient Stock");
                alert.setHeaderText(null);
                alert.setContentText("Please remove " + outOfStockBook + " from cart (insufficient stock)");
                alert.showAndWait();
                return;
            }

            NavigationHandler.handleCheckoutAction(stage, model);
        });
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Shop");
        stage.show();
    }

    public void loadCart() {
        // Get cart items for current user
        List<Cart> cartItems = model.viewCart();
        ObservableList<Cart> items = FXCollections.observableArrayList(cartItems);

        // Configure the book name column with text wrapping
        bookName.setCellFactory(WrappingTableCell::new);

        // Set up the book name column
        bookName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookName()));

        // Set up the price column (price * quantity)
        bookPrice.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("%.2f",
                        cellData.getValue().getPrice())));

        // Set up the quantity column with text fields
        quantityColumn.setCellFactory(col -> new TableCell<Cart, TextField>() {
            private final TextField textField = new TextField();

            {
                textField.setPrefWidth(80);
                textField.setStyle("-fx-alignment: CENTER;");

                // Add input validation for numbers only
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.isEmpty()) {
                        return;
                    }
                    if (!newValue.matches("\\d+")) {
                        textField.setText(oldValue);
                        return;
                    }

                    // Update quantity in database when value changes
                    Cart cart = getTableView().getItems().get(getIndex());
                    int newQuantity = Integer.parseInt(newValue);
                    model.updateCartQuantity(cart.getBookName(), newQuantity);

                    // Update the cart item's quantity
                    cart.setQuantity(newQuantity);

                    // Update total
                    updateTotal();
                });

                // When focus is lost, ensure there's at least a 1
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {  // Focus lost
                        if (textField.getText().isEmpty()) {
                            textField.setText("1");
                        } else if (Integer.parseInt(textField.getText()) < 1) {
                            textField.setText("1");

                        }
                    }
                });
            }

            @Override
            protected void updateItem(TextField item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Cart cart = getTableView().getItems().get(getIndex());
                    textField.setText(String.valueOf(cart.getQuantity()));
                    setGraphic(textField);
                }
            }
        });

        // Configure the delete button column
        deleteFromCartColumn.setCellFactory(col -> new TableCell<Cart, Button>() {
            private final Button deleteButton = new Button("Remove");

            {
                deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    Cart cart = getTableView().getItems().get(getIndex());

                    // Remove from database
                    model.removeBookFromCart(cart.getCartID());

                    // Remove from TableView
                    getTableView().getItems().remove(cart);

                    // Update total
                    updateTotal();
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        // Set the items in the TableView
        tableView.setItems(items);

        // Calculate initial total
        updateTotal();
    }

    // Helper method to update the total text
    private void updateTotal() {
        double total = 0.0;
        for (Cart cart : tableView.getItems()) {
            total += cart.getPrice() * cart.getQuantity();
        }
        // Update the total Text component
        this.total.setText(String.format("Total: $%.2f", total));
    }

    private String checkStock() {
        List<Book> allBooks = model.getBookShop().getBooks();

        for (Cart cartItem : tableView.getItems()) {
            for (Book book : allBooks) {
                if (book.getTitle().equals(cartItem.getBookName())) {
                    if (cartItem.getQuantity() > book.getNumberOfCopies()) {
                        return cartItem.getBookName();
                    }
                    break;
                }
            }
        }
        return null;
    }

}
