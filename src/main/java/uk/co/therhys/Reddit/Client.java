package uk.co.therhys.Reddit;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;

public class Client {
    private final RedditClient reddit;
    public Client(RedditSettings settings){
        Credentials creds = Credentials.script(settings.getUsername(), settings.getPassword(), settings.getCid(), settings.getSecret());

        UserAgent userAgent = new UserAgent("BBS", "uk.co.therhys.RedditBBS", "v0.1", settings.getUsername());

        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

        reddit = OAuthHelper.automatic(adapter, creds);
    }
}
