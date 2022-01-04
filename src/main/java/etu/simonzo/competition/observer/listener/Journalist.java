package etu.simonzo.competition.observer.listener;

import java.util.Optional;

import etu.simonzo.competition.observer.event.*;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.displayers.Displayer;

/**
 * Implementation of CompetitionListener which formats and prints information
 * about matches in a competition. For each MatchEvent received, an instance
 * of Journalist prints a message indicating which competitors played, and
 * who won (or that the match is tied if relevant). An instance of Displayer
 * passed as an argument of the constructor is used to display the messages.
 * @param <T> Sub-type of Competitor
 */
public class Journalist<T extends Competitor> implements CompetitionListener<T> {

    /**
     * Create an instance of Journalist which uses the displayer passed as
     * argument to display information based on the events received.
     * @param displayer Displayer used to present information
     */
    public Journalist(Displayer displayer) {
        this.displayer = displayer;
    }

    /**
     * Display a message representing a match and its outcome. The following
     * formats are used:
     * <pre>
     * {@code
     * COMPETITOR1 vs COMPETITOR2 - COMPETITOR1 wins
     * }
     * </pre>
     * or
     * <pre>
     * {@code
     * COMPETITOR1 vs COMPETITOR2 - tied match
     * }
     * </pre>
     * @param event Event indicating that a match was played
     */
    public void matchPlayed(MatchEvent<T> event) {
        this.displayer.displayMessage(event.getCompetitor1().getName() + " vs " +
                                      event.getCompetitor2().getName() + " - ");
        Optional<T> winnerOption = event.getWinner();
        if (winnerOption.isPresent()) {
            this.displayer.displayMessage(winnerOption.get().getName() + " wins\n");
        } else {
            this.displayer.displayMessage("tied match\n");
        }
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

    /** Displayer used by the journalist instance */
    private Displayer displayer;

}
