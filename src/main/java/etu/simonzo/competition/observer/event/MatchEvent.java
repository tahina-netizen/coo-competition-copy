package etu.simonzo.competition.observer.event;

import java.util.EventObject;
import java.util.Optional;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.MatchOutcome;

/**
 * Event which can be emitted to indicate that a match has been played. It
 * encapsulates references to the two competitors who played in the match, and
 * the outcome of the match as an instance of MatchOutcome.
 * @param <T> Sub-type of Competitor
 */
public class MatchEvent<T extends Competitor> extends EventObject {

    /**
     * Create an instance of MatchEvent, from the specified source, pair of
     * competitors, and match outcome.
     * @param source Competition which emitted the event
     * @param c1 First competitor
     * @param c2 Second competitor
     * @param outcome Enum instance indicating which competitor won the match
     */
    public MatchEvent(Competition<T> source, T c1, T c2, MatchOutcome outcome) {
        super(source);
        this.c1 = c1;
        this.c2 = c2;
        this.outcome = outcome;
    }

    /**
     * Return the first competitor encapsulated by the event.
     * @return First competitor
     */
    public T getCompetitor1() {
        return this.c1;
    }

    /**
     * Return the second competitor encapsulated by the event.
     * @return Second competitor
     */
    public T getCompetitor2() {
        return this.c2;
    }

    /**
     * Return an instance of MatchOutcome indicating which competitor won the
     * match.
     * @return <code>FIRST_PLAYER_WIN</code> iff the competitor returned by
     * {@link MatchEvent#getCompetitor1()} won, <code>SECOND_PLAYER_WIN</code>
     * iff {@link MatchEvent#getCompetitor2()} won, else <code>TIE</code>
     */
    public MatchOutcome getOutcome() {
        return this.outcome;
    }

    /**
     * Return an optional competitor. If the option is not empty, then the
     * returned competitor is the winner of the match corresponding to the
     * event. If the option is empty, the match is tied.
     * @return Optional winner of the match
     */
    public Optional<T> getWinner() {
        switch (this.outcome) {
        case FIRST_PLAYER_WIN: return Optional.of(this.c1);
        case SECOND_PLAYER_WIN: return Optional.of(this.c2);
        default: return Optional.empty();
        }
    }

    /** First competitor */
    private T c1;

    /** Second competitor */
    private T c2;

    /** Outcome of the match between the competitors */
    private MatchOutcome outcome;

}
