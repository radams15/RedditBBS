package uk.co.therhys.Db;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.*;

public class UserDao {
    private Connection conn;

    @AllArgsConstructor
    @Data
    public static class User {
        String username;
        String password;
    }

    @AllArgsConstructor
    @Data
    public static class RedditCreds {
        String username;
        String password;
        String cid;
        String secret;
    }

    UserDao(Connection conn) {
        this.conn = conn;
    }

    public void addUser(User user, RedditCreds creds) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO RedditCreds VALUES (?, ?, ?, ?)");
            statement.setString(1, creds.getUsername());
            statement.setString(2, creds.getPassword());
            statement.setString(3, creds.getCid());
            statement.setString(4, creds.getSecret());
            statement.execute();

            statement = conn.prepareStatement("INSERT INTO User VALUES (?, ?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, creds.getUsername());
            statement.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public RedditCreds getCreds(User user) {
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT RedditCreds.* FROM User, RedditCreds WHERE User.username IS ? AND User.password IS ?");
            statement.setString(1, user.username);
            statement.setString(2, user.password);
            ResultSet res = statement.executeQuery();

            // Found a user which fits the criteria
            if(res.next()) {
                return new RedditCreds(
                        res.getString("username"),
                        res.getString("password"),
                        res.getString("cid"),
                        res.getString("secret")
                );
            }

            return null;

        }catch (SQLException e){
            return null;
        }
    }
}
