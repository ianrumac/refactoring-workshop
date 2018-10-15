package first;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookController {

    public UI ui = null;
    public ArrayList<User> renters = new ArrayList<>();
    public HashMap<Book, List<User>> waitingList = new HashMap<>();

    public void rentBook(User user, Book book) {
        ArrayList<Book> books = (ArrayList<Book>) Database.getInstance().getColumn("books");

        if (books.contains(book) && !waitingList.containsKey(book)) {
            user.getBooks().add(book);
            Database.getInstance().putObject(user, "users", new Database.DBCallback() {
                @Override
                public void success() {
                    ui.bookRented();
                }

                @Override
                public void failure(Throwable throwable) {
                    throwable.printStackTrace();
                    if (throwable instanceof NoSuchFieldError)
                        ui.showError("No such item!");
                }
            });
        } else if (!books.contains(book)) {
            if (waitingList.containsKey(book))
                waitingList.get(book).add(user);
            else {
                ArrayList<User> newWaitingList = new ArrayList<>();
                newWaitingList.add(user);
                waitingList.put(book, newWaitingList);
            }
            ui.addedToWaitlist(waitingList.get(book).size() * book.getAverageReadingTime());
        }
    }

    public void returnBook(Book book, User user){
        user.getBooks().remove(book);
        Database.getInstance().putObject(user, "users", new Database.DBCallback() {
            @Override
            public void success() {
                ui.bookReturned();
                User next = waitingList.get(book).get(0);
                NotificationManager.getInstance().notifyUser(next, new NotificationManager.NotificationCallback() {
                    @Override
                    public void userNotified() {
                        ui.bookReturned();
                    }

                    @Override
                    public void errorHappened() {
                        ui.showError("No such user found");
                    }
                });
            }

            @Override
            public void failure(Throwable throwable) {
                ui.showError("second.Book cannot be returned to that user");
            }
        });

    }
}


class Book {

    private String name;
    private String author;
    private Long id;
    private Long authorId;
    private Integer averageReadingTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getAverageReadingTime() {
        return averageReadingTime;
    }

    public void setAverageReadingTime(Integer averageReadingTime) {
        this.averageReadingTime = averageReadingTime;
    }
}

class User {
    private Map<Long, Book> books;
    private String name;
    private Long id;

    public ArrayList<Book> getBooks() {
        return (ArrayList) books.values();
    }
}
