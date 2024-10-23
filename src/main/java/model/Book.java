package model;

public class Book {
    private String title;
    private String author;
    private  int numberOfCopies;
    private float price;
    private int soldCopies;

    public Book(){
    }

    public Book(String title, String author, int numberOfCopies, float price, int soldCopies) {
        this.title = title;
        this.author = author;
        this.numberOfCopies = numberOfCopies;
        this.price = price;
        this.soldCopies = soldCopies;
    }
}
