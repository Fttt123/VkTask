package ru.javabegin.BookStore.entity;

import java.util.ArrayList;
import java.util.List;

public class MainServer {

    private List<books> books;
    private account account;

    public books GetBookById(int id) {
        for(books i: books) {
            if(i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    public List<ru.javabegin.BookStore.entity.books> getBooks() {
        return books;
    }

    public void setBooks(List<ru.javabegin.BookStore.entity.books> books) {
        this.books = books;
    }

    public account getAccount() {
        return account;
    }

    public void setAccount(account account) {
        this.account = account;
    }

    public List<books> setBooksWithoutAmount0(){
        List<books> booksList = new ArrayList<books>();
        for(books book:books){
            if(book.getAmount() > 0){
                booksList.add(book);
            }
        }
        return booksList;
    }
}
