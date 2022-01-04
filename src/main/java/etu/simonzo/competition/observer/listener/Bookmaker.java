package etu.simonzo.competition.observer.listener;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import etu.simonzo.competition.observer.event.*;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.displayers.Displayer;
import etu.simonzo.competition.match.MatchOutcome;

/**
 * Implementation of CompetitionListener which maintains associations between
 * competitors involved in competitions and odds. Odds are modified whenever
 * a MatchEvent is received, and a trace is displayed using the instance of
 * {@link Displayer} passed as argument of the constructor.
 *
 * Odds are integers varying between <code>MIN_ODD</code> and
 * <code>MAX_ODD</code> depending on the outcome of a match. When a competitor
 * is first observed, it is assigned an odd of 2. In case of a victory, the odd
 * associated to a competitor is decremented by 1 (but will be less than
 * <code>MIN_ODD</code>); in case of a defeat, the odd is incremented (but will
 * not be more than <code>MAX_ODD</code>), and in case of a tie, the odd is not
 * modified. Changes in odds are displayed after each match event received.
 * @param <T> Sub-type of Competitor
 */
public class Bookmaker<T extends Competitor> implements CompetitionListener<T> {

    /**
     * Create a bookmaker. Initially, a bookmaker has an empty table of
     * associations between competitors and odds.
     * @param displayer Displayer used to present information
     */
    public Bookmaker(Displayer displayer) {
        this.displayer = displayer;
        this.odds = new HashMap<>();
    }

    /**
     * Modify odds based on the outcome of the match. A trace is displayed using
     * the displayer instance passed as a constructor argument, using the
     * following format:
     * <pre>
     * {@code
     * New odds: COMPETITOR1 = X, COMPETITOR2 = Y
     * }
     * </pre>
     * @param event Event indicating that a match was played
     */
    public void matchPlayed(MatchEvent<T> event) {
        if (event.getOutcome() == MatchOutcome.FIRST_PLAYER_WIN) {
            this.decrementOdd(event.getCompetitor1());
            this.incrementOdd(event.getCompetitor2());
        } else if (event.getOutcome() == MatchOutcome.SECOND_PLAYER_WIN) {
            this.incrementOdd(event.getCompetitor1());
            this.decrementOdd(event.getCompetitor2());
        } else if (event.getOutcome() == MatchOutcome.TIE) {
            this.addCompetitorIfUnknown(event.getCompetitor1());
            this.addCompetitorIfUnknown(event.getCompetitor2());
        }
        this.displayer.displayMessage("New odds: " +
                                      event.getCompetitor1().getName() + " = " +
                                      this.odds.get(event.getCompetitor1()) + ", " +
                                      event.getCompetitor2().getName() + " = " +
                                      this.odds.get(event.getCompetitor2()) + "\n");
    }

    /**
     * Do not react to this event.
     * @param event Event indicating that a competition was started
     */
    public void competitionStarted(CompetitionStartEvent<T> event) {}

    /**
     * Do not react to this event.
     * @param event Event indicating that a competition was ended
     */
    public void competitionEnded(CompetitionEndEvent<T> event) {}

    /**
     * Do not react to this event.
     * @param event Event indicating that groups were formed in a groups-based
     * competition
     */
    public void groupsFormed(GroupsFormedEvent<T> event) {}

    /**
     * Do not react to this event.
     * @param event Event indicating that qualified competitors were selected
     */
    public void qualifiedCompetitorsSelected(QualifiedCompetitorsSelectedEvent<T> event) {}

    /**
     * Do not react to this event.
     * @param event Event indicating that the tournament phase of a competition
     * has started
     */
    public void tournamentPhaseStarted(TournamentPhaseStartedEvent<T> event) {}

    /**
     * Return an option containing the odd associated to a competitor in the odd
     * table of the bookmaker. The option is empty if the competitor does not
     * appear in the table.
     * @param competitor Competitor whose odd will be returned
     * @return Optional odd of the competitor, empty if no odd is associated to
     * the competitor
     */
    public Optional<Float> getCompetitorOdd(T competitor) {
        if (this.odds.containsKey(competitor)) {
            return Optional.of(this.odds.get(competitor));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Increment the odd associated to the competitor by INCREMENT, with a
     * maximum value of MAX_ODD. If the competitor is not associated to an odd,
     * it receives START_ODD, then incrementation is performed.
     * @param competitor Competitor whose odd will be incremented
     */
    private void incrementOdd(T competitor) {
        this.addCompetitorIfUnknown(competitor);
        this.odds.replace(competitor, Math.min(this.odds.get(competitor) + INCREMENT, MAX_ODD));
    }

    /**
     * Decrement the odd associated to the competitor by INCREMENT, with a
     * minimum value of MIN_ODD. If the competitor is not associated to an odd,
     * it receives START_ODD, then decrementation is performed.
     * @param competitor Competitor whose odd will be decremented
     */
    private void decrementOdd(T competitor) {
        this.addCompetitorIfUnknown(competitor);
        this.odds.replace(competitor, Math.max(this.odds.get(competitor) - INCREMENT, MIN_ODD));
    }

    /**
     * Associate the competitor to START_ODD iff the competitor is not already
     * associated to an odd.
     * @param competitor Competitor to potentially add to the odds table
     */
    private void addCompetitorIfUnknown(T competitor) {
        if (!this.odds.containsKey(competitor)) {
            this.odds.put(competitor, START_ODD);
        }
    }

    /** Displayer used by the bookmaker instance */
    private Displayer displayer;

    /** Map associating each competitor to an odd */
    private Map<T, Float> odds;

    /** Minimal odd value */
    static float MIN_ODD = 1.0f;

    /** Maximal odd value */
    static float MAX_ODD = 5.0f;

    /** Odd value assigned to a player with no associated odd */
    static float START_ODD = 2.0f;

    static float INCREMENT = 1.0f;

}
