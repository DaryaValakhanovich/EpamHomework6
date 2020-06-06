package dao;

import entites.Friendship;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDao {
    private static final String FRIENDSHIPS_TABLE_NAME = "friendships";
    private static FriendshipDao INSTANCE = null;

    private FriendshipDao() {
    }

    public static FriendshipDao getInstance() {
        if (INSTANCE == null) {
            synchronized (FriendshipDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FriendshipDao();
                }
            }
        }
        return INSTANCE;
    }

    public Friendship create(Friendship friendship) {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO friendships (user_id1, user_id2, timestamp) " +
                            "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, friendship.getUser1().getId());
                preparedStatement.setLong(2, friendship.getUser2().getId());
                preparedStatement.setString(3, friendship.getTimestamp().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendship;
    }

    public List<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM friendships;")) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        friendships.add(createUserFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    private Friendship createUserFromResultSet(ResultSet resultSet) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setUser1(UserDao.getInstance().findById(resultSet.getLong(FRIENDSHIPS_TABLE_NAME + ".user_id1")).get());
        friendship.setUser2(UserDao.getInstance().findById(resultSet.getLong(FRIENDSHIPS_TABLE_NAME + ".user_id2")).get());
        friendship.setTimestamp((LocalDate.parse(resultSet.getString(FRIENDSHIPS_TABLE_NAME + ".timestamp"))));
        return friendship;
    }

    public void delete() {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM friendships")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
