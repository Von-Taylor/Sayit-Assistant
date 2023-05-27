/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package sayit;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;

import sayit.Server.Server;
import sayit.Server.BusinessLogic.Prompt;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.util.ArrayList;

public class FilePreserveTest {
    private final String FILE_EMPTY_PATH = "src/test/java/sayit/Test-files/empty.txt";
    private final String loadPURL = "http://localhost:8100/load";
    private final String newQURL = "http://localhost:8100/newQuestion";

    @BeforeEach
    void clearFile() throws FileNotFoundException, IOException {
        new FileOutputStream(FILE_EMPTY_PATH).close();
    }

    @AfterEach
    void closeServer() throws IOException {
        Server.stopServer();
        new FileOutputStream(FILE_EMPTY_PATH).close();
    }

    @Test
    void loadPromptsHandlerPutTest() throws IOException {
        // start the server and fill its prompts with this file
        Server.startServer(FILE_EMPTY_PATH);

        // add 2 prompts to server
        Prompt p1 = new Prompt("Is the ocean a soup?", "Whatever makes you happy.");
        URL url = new URL(newQURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(
            conn.getOutputStream()
        );
        out.write(p1.getQuery() + "\n" + p1.getAnswer());
        out.flush();
        out.close();
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        in.readLine();
        in.close();

        Prompt p2 = new Prompt("What's red and bad for your teeth?", "A brick.");
        url = new URL(newQURL);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        out = new OutputStreamWriter(
            conn.getOutputStream()
        );
        out.write(p2.getQuery() + "\n" + p2.getAnswer());
        out.flush();
        out.close();
        in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        in.readLine();
        in.close();


        // request server to save all its prompts to FILE_EMPTY_PATH
        url = new URL(loadPURL);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        String response = in.readLine();

        // make sure correct response was received
        assertEquals("All Prompts were written to " + FILE_EMPTY_PATH, response);


        // read from FILE_EMPTY_PATH and make sure it was written to correctly
        final String startSt = "#Start#";
        final String endSt = "#End#";
        FileReader fileReader = new FileReader(FILE_EMPTY_PATH);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String lineLoop;
        String qLine = "";    // query line
        String aLine = "";   // answer line
        Prompt[] readPrompts = new Prompt[2];

        int i = 0;
        while (((lineLoop = bufferedReader.readLine()) != null)) {
            if (lineLoop.equals(startSt)) {
                qLine = bufferedReader.readLine();
            } else if (lineLoop.equals(endSt)) {
                qLine = qLine.trim();
                aLine = aLine.trim();
                Prompt questionAndAnswer = new Prompt(qLine, aLine);
                readPrompts[i] = questionAndAnswer;
                i++;
                aLine = "";
            } else {
                aLine += lineLoop + '\n';
            }
        }
        bufferedReader.close();
        fileReader.close();

        // make sure correct prompts were written
        assertEquals("Is the ocean a soup?", readPrompts[0].getQuery());
        assertEquals("Whatever makes you happy.", readPrompts[0].getAnswer());
        assertEquals("What's red and bad for your teeth?", readPrompts[1].getQuery());
        assertEquals("A brick.", readPrompts[1].getAnswer());


        // close the server and open it again with the same FILE_EMPTY_PATH file
        Server.stopServer();
        Server.startServer(FILE_EMPTY_PATH);

        // check that the same prompts were restored on server
        // get the prompt at 0th index on the server
        url = new URL(loadPURL + "?=" + 0);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        response = in.readLine();
        in.close();
        String question = response.substring(0,response.indexOf("/D\\"));
        String answer = response.substring(response.indexOf("/D\\") + 3);

        // compare it to expected Q&A
        assertEquals("Is the ocean a soup?", question);
        assertEquals("Whatever makes you happy.", answer);


        // get the prompt at 1st index on the server
        url = new URL(loadPURL + "?=" + 1);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );
        response = in.readLine();
        in.close();
        question = response.substring(0,response.indexOf("/D\\"));
        answer = response.substring(response.indexOf("/D\\") + 3);

        // compare it to expected Q&A
        assertEquals("What's red and bad for your teeth?", question);
        assertEquals("A brick.", answer);
    }
}