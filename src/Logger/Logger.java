package Logger;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    public static  synchronized void myLogger( String write, boolean append) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("src/Logger/log.txt", append);
            fileWriter.write(write);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
