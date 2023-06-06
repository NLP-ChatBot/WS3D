import java.util.logging.Level;
import java.util.logging.Logger;

public class ExperimentMain {
  public Logger logger = Logger.getLogger(ExperimentMain.class.getName());

  public ExperimentMain() {
    Logger.getLogger("codelets").setLevel(Level.SEVERE);
    // Create Environment
    Environment env = new Environment(); //Creates only a creature and some apples
    AgentMind a = new AgentMind(env); // Creates the Agent Mind and start it
  }

  /**
   * @param args Main arguments
   */
  public static void main(String[] args) {
    ExperimentMain em = new ExperimentMain();
  }
}
