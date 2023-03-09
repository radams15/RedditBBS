package uk.co.therhys.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private final Connection conn;

    public DatabaseManager(String fileName) throws SQLException {
        conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", fileName));

        Statement statement = conn.createStatement();

        statement.executeUpdate(
             "CREATE TABLE IF NOT EXISTS RedditCreds (" +
                "username	TEXT NOT NULL UNIQUE,"+
                "password	TEXT NOT NULL,"+
                "cid	TEXT NOT NULL,"+
                "secret	TEXT NOT NULL,"+
                "PRIMARY KEY(username))"
        );

        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS User (" +
                        "username	TEXT NOT NULL UNIQUE,"+
                        "password	TEXT NOT NULL,"+
                        "creds	TEXT NOT NULL,"+
                        "FOREIGN KEY(creds) REFERENCES RedditCreds(username),"+
                        "PRIMARY KEY(username))"
        );
    }

    public UserDao userDao() {
        return new UserDao(conn);
    }
}
