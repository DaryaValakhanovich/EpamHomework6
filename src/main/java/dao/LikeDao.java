package dao;

import entites.Like;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LikeDao {
    private static final String LIKES_TABLE_NAME = "likes";
    private static LikeDao INSTANCE = null;

    private LikeDao() {
    }

    public static LikeDao getInstance() {
        if (INSTANCE == null) {
            synchronized (LikeDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LikeDao();
                }
            }
        }
        return INSTANCE;
    }

    public Like create(Like like) {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO likes (user_id, post_id, timestamp) " +
                            "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, like.getUser().getId());
                preparedStatement.setLong(2, like.getPost().getId());
                preparedStatement.setString(3, like.getTimestamp().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return like;
    }

    public List<Like> findAll() {
        List<Like> likes = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM likes;")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        likes.add(createUserFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likes;
    }

    private Like createUserFromResultSet(ResultSet resultSet) throws SQLException {
        Like like = new Like();
        like.setUser(UserDao.getInstance().findById(resultSet.getLong(LIKES_TABLE_NAME + ".user_id")).get());
        like.setPost(PostDao.getInstance().findById(resultSet.getLong(LIKES_TABLE_NAME + ".post_id")).get());
        like.setTimestamp((LocalDate.parse(resultSet.getString(LIKES_TABLE_NAME + ".timestamp"))));
        return like;
    }

    public void delete() {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM likes")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
