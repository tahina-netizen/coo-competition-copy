package etu.simonzo.competition.displayers;

/**
 * A displayer which uses the standard output to display information.
 */
public class StdoutDisplayer implements Displayer {

    /**
     * the unique instance of StdoutDisplayer (singleton pattern)
     */
    private static final StdoutDisplayer STDOUT_DISPLAYER_SINGLETON =
            new StdoutDisplayer();

    /**
     * Create a standard output displayer.
     */
    private StdoutDisplayer() {}

    /**
     * Display the given message to the standard output.
     * The message may be formatted for esthetic and readability purpose.
     */
    public void displayMessage(String message) {
        System.out.print(message);
    }

    /**
     * Give the unique instance of StdoutDisplayer. (singleton pattern is used)
     * @return the unique instance of StdoutDisplayer
     */
    public static StdoutDisplayer getInstance() {
        return STDOUT_DISPLAYER_SINGLETON;
    }

}
