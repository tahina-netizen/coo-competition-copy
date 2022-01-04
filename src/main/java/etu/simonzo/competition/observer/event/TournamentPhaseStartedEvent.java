package etu.simonzo.competition.observer.event;

import java.util.EventObject;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitors.Competitor;

/**
 * Event which can be emitted to indicate that a tournament phase has started,
 * (eg. <code>1/8th finale</code>, <code>1/4 finale</code>, ...). This event
 * encapsulates an integer which indicates what phase has started
 * (<code>8</code> for <code>1/8th</code>, <code>4</code> for
 * <code>1/4th</code>, ...).
 * @param <T> Sub-type of Competitor
 */
public class TournamentPhaseStartedEvent<T extends Competitor> extends EventObject {

    /**
     * Create an event to indicate that a tournament phase has started in the
     * source competition.
     * @param source Competition which emitted the event
     * @param phase Integer indicating which phase of the tournament started
     */
    public TournamentPhaseStartedEvent(Competition<T> source, int phase) {
        super(source);
        this.phase = phase;
    }

    /**
     * Return the integer indicating the phase of tournament
     * @return For example, <code>8</code> for <code>1/8th</code>,
     * <code>4</code> for <code>1/4th</code>*/
    public int getPhase() {
        return this.phase;
    }

    /** Integer indicating the phase of tournament. */
    private int phase;

}
