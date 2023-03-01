package uk.co.therhys.UI;

import uk.co.therhys.ClientConnection;

import java.io.IOException;

public class Pager {
    static int findPageEnd(String data, int start, int rows, int cols){
        int end = 0;
        int max = rows*cols;
        int linechar = 0;

        data = data.substring(start);

        char c;
        int lastLineEnd = 0;
        while(end <= max && end < data.length()) {
            c = data.charAt(end);

            if (c == '\n') {
                lastLineEnd = end;
                end += (rows - linechar);
                linechar = 0;
            } else if (linechar == rows) {
                lastLineEnd = end;
                end += linechar;
                linechar = 0;
            }else {
                linechar++;
            }
        }

        return start + lastLineEnd;
    }

    static void page(String data, ClientConnection conn) throws IOException {
        int[] termSize = conn.client.settings.getTermSize();
        int rows = termSize[0];
        int cols = termSize[1];

        int end = 0;
        int start;
        while(end < data.length()){
            start = end;
            end = findPageEnd(data, end, rows-1, cols);

            if(start == end){
                end = data.length();
            }

            conn.writeln(data.substring(start, end));
            conn.readln();
        }
    }
}
