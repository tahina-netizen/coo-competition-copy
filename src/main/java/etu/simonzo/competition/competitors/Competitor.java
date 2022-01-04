package etu.simonzo.competition.competitors;

/**
 * A named competitor
 */
public class Competitor {
    /** Competitor's name */
    protected String name;

    /**
     * Construct a competitor defined by a name
     * @param name Name of the competitor
     */
    public Competitor(String name) {
        this.name = name;
    }

    /**
     * Return the name of the competitor
     * @return The name of the competitor
     */
    public String getName() {
        return this.name;
    }
}
