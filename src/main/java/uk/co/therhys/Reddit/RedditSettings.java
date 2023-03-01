package uk.co.therhys.Reddit;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedditSettings {
    String username;
    String password;
    String cid;
    String secret;
}
