package uk.co.therhys;

public class RedditBBS {
    public static void main(String[] args){
        ClientListener listener = new ClientListener(9999);

        listener.listen();
    }
}
