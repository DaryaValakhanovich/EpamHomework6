package entites;

import java.time.LocalDate;

public class Post {
    private long id;
    private User user;
    private String text;
    private LocalDate timestamp;

    public Post() {
    }

    public Post(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
