/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package sayit;

import java.io.IOException;

import javax.swing.SwingUtilities;

/**
 * Start running the app
 */
public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      /* comment this out for mock use */
      new MockMediator();

      /* comment this out for real use */
      // try {
      //   new Mediator();
      // } catch (IOException | InterruptedException e) {
      //   e.printStackTrace();
      // }
    });
  }
}