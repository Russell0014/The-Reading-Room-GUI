package model;

public class Book {
    private String title;
    private String author;
    private  int numberOfCopies;
    private Double price;
    private int soldCopies;

    public Book(){
    }

    public Book(String title, String author, int numberOfCopies, Double price, int soldCopies) {
        this.title = title;
        this.author = author;
        this.numberOfCopies = numberOfCopies;
        this.price = price;
        this.soldCopies = soldCopies;
    }

    // Getter for soldCopies
    public int getSoldCopies() {
        return soldCopies;
    }

    // Other getters (optional)
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public Double getPrice() {
        return price;
    }

    // toString() method (optional)
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", numberOfCopies=" + numberOfCopies +
                ", price=" + price +
                ", soldCopies=" + soldCopies +
                '}';
    }
}
