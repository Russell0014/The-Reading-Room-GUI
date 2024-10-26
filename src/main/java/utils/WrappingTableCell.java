package utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class WrappingTableCell<T> extends TableCell<T, String> {
    private final Text text;
    private final TableColumn<T, String> column;

    public WrappingTableCell(TableColumn<T, String> column) {
        this.text = new Text();
        this.column = column;

        // Update wrapping width when column is resized
        column.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (text != null) {
                text.setWrappingWidth(newValue.doubleValue() - 10);
            }
        });
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            text.setText(item);
            text.setWrappingWidth(column.getWidth() - 10);
            text.setTextAlignment(TextAlignment.LEFT);
            setGraphic(text);
        }
    }
}