package uk.co.therhys;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BbsSettings {
    int[] termSize;
    boolean selftextOnly;
}
