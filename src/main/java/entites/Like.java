package entites;

import java.time.LocalDate;

public class Like {
    private User user;
    private Post post;
    private LocalDate timestamp;

    public Like() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Like{" +
                "user=" + user +
                ", post=" + post +
                ", timestamp=" + timestamp +
                '}';
    }
}
