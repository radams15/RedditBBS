package uk.co.therhys;

import uk.co.therhys.Db.DatabaseManager;
import uk.co.therhys.Db.UserDao;

import java.sql.SQLException;

public class RedditBBS {
    public static void main(String[] args){
        DatabaseManager dbMan;

        try {
            dbMan = new DatabaseManager("database.db");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        ClientListener listener = new ClientListener(9999, dbMan);

        listener.listen();
    }
}
