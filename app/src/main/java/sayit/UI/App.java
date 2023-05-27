/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package sayit.UI;

import java.io.IOException;
import javax.swing.SwingUtilities;
import sayit.Server.Server;

/**
 * Start running the app
 */
public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      // Start the web server
      try {
        String filePath = "src/main/java/sayit/Server/Handlers/preserve.txt";
        Server.startServer(filePath);
      } catch (IOException e) {
        System.out.println("App.java: " + e);
        e.printStackTrace();
      }

      // Create the UI
      new AppFrame();
    });
  }
}