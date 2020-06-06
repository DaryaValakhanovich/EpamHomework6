import com.github.javafaker.Faker;
import dao.FriendshipDao;
import dao.LikeDao;
import dao.PostDao;
import entites.Friendship;
import entites.Like;
import entites.Post;
import entites.User;
import dao.UserDao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        long numberOfUsers = 500, numberOfPosts = 500, numberOfFriendships = 500, numberOfLikes = 500;

        createDatabase(numberOfUsers, numberOfPosts, numberOfFriendships, numberOfLikes);

        UserDao.getInstance().findAll().forEach(System.out::println);
        PostDao.getInstance().findAll().forEach(System.out::println);
        FriendshipDao.getInstance().findAll().forEach(System.out::println);
        LikeDao.getInstance().findAll().forEach(System.out::println);

        List<User> users = UserDao.getInstance().findByNumberOfFriendsAndLikes(3, 1);
        users.forEach(user -> System.out.println(user.getName()));

        LikeDao.getInstance().delete();
        FriendshipDao.getInstance().delete();
        PostDao.getInstance().delete();
        UserDao.getInstance().delete();
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static void createDatabase(long numberOfUsers, long numberOfPosts, long numberOfFriendships, long numberOfLikes) {
        User user;
        Post post;
        Friendship friendship;
        Like like;
        Faker faker = new Faker();

        for (int i = 0; i < numberOfUsers; i++) {
            user = new User();
            user.setName(faker.name().firstName());
            user.setSurname(faker.name().lastName());
            user.setBirthday(convertToLocalDate(faker.date().birthday()));
            UserDao.getInstance().create(user);
        }

        LocalDate someLocalDate = LocalDate.parse("2025-01-01");
        Date someDate = convertToDate(someLocalDate);
        LocalDate someLocalDate2 = LocalDate.parse("2026-01-01");
        Date someDate2 = convertToDate(someLocalDate2);

        for (int i = 0; i < numberOfPosts; i++) {
            post = new Post();
            post.setUser(new User(faker.number().numberBetween(1, numberOfUsers)));
            post.setText(faker.shakespeare().asYouLikeItQuote());
            post.setTimestamp(convertToLocalDate(faker.date().between(someDate, someDate2)));
            PostDao.getInstance().create(post);
        }

        for (int i = 0; i < numberOfFriendships; i++) {
            friendship = new Friendship();
            friendship.setUser1(new User(faker.number().numberBetween(1, numberOfUsers)));
            friendship.setUser2(new User(faker.number().numberBetween(1, numberOfUsers)));
            friendship.setTimestamp(convertToLocalDate(faker.date().between(someDate, someDate2)));
            FriendshipDao.getInstance().create(friendship);
        }

        for (int i = 0; i < numberOfLikes; i++) {
            like = new Like();
            like.setUser(new User(faker.number().numberBetween(1, numberOfUsers)));
            like.setPost(new Post(faker.number().numberBetween(1, numberOfPosts)));
            like.setTimestamp(convertToLocalDate(faker.date().between(someDate, someDate2)));
            LikeDao.getInstance().create(like);
        }
    }
}
