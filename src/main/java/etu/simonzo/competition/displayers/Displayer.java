package etu.simonzo.competition.displayers;

/**
 * A displayer is used to output some information/messages from the program.
 * For example, a displayer can print something to the console, or show a
 * notification in a GUI, ...
 */
public interface Displayer {
    /**
     * Display the given message.
     * Before being displayed, the message could be formatted (for example 
     * for esthetic purpose).
     * @param message message to be displayed
     */
    void displayMessage(String message);
}
