package console;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadSequence {

    public static String read(String fileName) throws IOException {
        BufferedReader br = null;
        FileReader fr = null;
        String currentLine = null;

        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            currentLine = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
        return currentLine;
    }
}
