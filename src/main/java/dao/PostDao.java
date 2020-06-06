package dao;

import entites.Post;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostDao {
    private static final String FRIENDSHIPS_TABLE_NAME = "posts";
    private static PostDao INSTANCE = null;

    private PostDao() {
    }

    public static PostDao getInstance() {
        if (INSTANCE == null) {
            synchronized (PostDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PostDao();
                }
            }
        }
        return INSTANCE;
    }

    public Post create(Post post) {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO posts (user_id, text, timestamp) " +
                            "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, post.getUser().getId());
                preparedStatement.setString(2, post.getText());
                preparedStatement.setString(3, post.getTimestamp().toString());
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM posts;")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        posts.add(createUserFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    public Optional<Post> findById(long id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM posts " +
                            "WHERE id = ?")) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(createUserFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private Post createUserFromResultSet(ResultSet resultSet) throws SQLException {
        Post post = new Post();
        post.setId(resultSet.getLong(FRIENDSHIPS_TABLE_NAME + ".id"));
        post.setUser(UserDao.getInstance().findById(resultSet.getLong(FRIENDSHIPS_TABLE_NAME + ".user_id")).get());
        post.setText(resultSet.getString(FRIENDSHIPS_TABLE_NAME + ".text"));
        post.setTimestamp((LocalDate.parse(resultSet.getString(FRIENDSHIPS_TABLE_NAME + ".timestamp"))));
        return post;
    }

    public void delete() {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM posts")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
