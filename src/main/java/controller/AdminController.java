package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;
import model.Model;
import javafx.collections.FXCollections;
import utils.WrappingTableCell;

public class AdminController {
    private Model model;
    private Stage stage;
    private Stage parentStage;

    @FXML
    private MenuItem logout;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> bookName;

    @FXML
    private TableColumn<Book, String> bookAuthor;

    @FXML
    private TableColumn<Book, String> bookPrice;

    @FXML
    private TableColumn<Book, Integer> sold;

    @FXML
    private TableColumn<Book, Integer> stock;

    @FXML
    private Text welcome;

    public AdminController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {

        logout.setOnAction(e -> NavigationHandler.handleLoginAction(stage, model));

        welcome.setText("Welcome Admin");

        // Initialize table columns

        bookName.setCellFactory(WrappingTableCell::new);
        bookAuthor.setCellFactory(WrappingTableCell::new);

        bookName.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookPrice.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getPrice())));
        sold.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));

        stock.setCellFactory(column -> new TableCell<Book, Integer>() {
            private final TextField textField = new TextField();
            private final Button updateButton = new Button("Update");
            private final HBox container = new HBox(5); // 5 is spacing between elements

            {
                textField.setPrefWidth(60);
                container.getChildren().addAll(textField, updateButton);

                updateButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    try {
                        int newStock = Integer.parseInt(textField.getText());
                        if (newStock >= 0) {
                            model.updateBookQuantity(book.getTitle(), newStock);
                            // Refresh the table
                            tableView.setItems(FXCollections.observableArrayList(
                                    model.getBookShop().getBooks()));
                        } else {
                            showError("Stock cannot be negative");
                        }
                    } catch (NumberFormatException e) {
                        showError("Please enter a valid number");
                        textField.setText(String.valueOf(book.getNumberOfCopies()));
                    }
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    textField.setText(String.valueOf(book.getNumberOfCopies()));
                    setGraphic(container);
                }
            }
        });

        // Load data into table
        tableView.setItems(FXCollections.observableArrayList(
                model.getBookShop().getBooks()));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 650, 450);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Admin Panel");
        stage.show();
    }
}