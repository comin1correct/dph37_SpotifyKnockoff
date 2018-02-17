package edu.pitt.IS18.SpotifyKnockoff;
import java.io.*;
import java.util.Date;
/**
 * Class: ErrorLogger
 * @author David Hinton
 * @version 1.0
 * @since 2018-01-31
 */
public class ErrorLogger {
    /**
     * Logging error messages into a local text file.
     * Format: date time, error message.
     * @param errorMessage
     */
    public static void log(String errorMessage){
        String FILE_PATH = "src/data/errorLog.txt";
        try{
                BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH,true));
                bw.write(new Date()+", "+errorMessage + "\n");
                bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
