package etu.simonzo.competition.observer.event;

import java.util.List;
import java.util.EventObject;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitors.Competitor;

/**
 * Event which can be emitted to indicate that a competition has started. It
 * encapsulates a reference to a list containing the competitors which are
 * taking place in the competition.
 * @param <T> Sub-type of Competitor
 */
public class CompetitionStartEvent<T extends Competitor> extends EventObject {

    /**
     * Create an event encapsulating a list of participating competitors.
     * @param source Competition which emitted the event
     * @param competitionId Identifier of the competition
     * @param participants List of competitors taking part in the competition
     */
    public CompetitionStartEvent(Competition<T> source, String competitionId,
                                 List<T> participants) {
        super(source);
        this.competitionId = competitionId;
        this.participants = participants;
    }

    /** Return a string identifier of the competition which emitted the event
     * @return String identifier
     */
    public String getCompetitionId() {
        return this.competitionId;
    }

    /**
     * Return the list of competitors encapsulated by the event.
     * @return List of participating competitors
     */
    public List<T> getParticipants() {
        return this.participants;
    }

    /** Identifier of the competition */
    private String competitionId;

    /**
     * List of competitors taking part in the competition which emitted the
     * event. */
    private List<T> participants;

}
