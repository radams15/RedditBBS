package uk.co.therhys;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BbsSettings {
    String username;
    String password;
    int[] termSize;
    boolean selftextOnly;
}
