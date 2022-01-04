package etu.simonzo.competition.observer.event;

import java.util.Map;
import java.util.EventObject;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitors.Competitor;

/**
 * Event which can be emitted to indicate that a competition has ended. It
 * encapsulates a string identifying the emitting competition, and a map
 * associating competitors to scores in the competition.
 * @param <T> Sub-type of Competitor
 */
public class CompetitionEndEvent<T extends Competitor> extends EventObject {

    /**
     * Create an event encapsulating a list of qualified competitors.
     * @param source Competition which emitted the event
     * @param competitionId Identifier of the competition
     * @param scores Map associating competitors to scores
     */
    public CompetitionEndEvent(Competition<T> source, String competitionId,
                               Map<T, Integer> scores) {
        super(source);
        this.competitionId = competitionId;
        this.scores = scores;
    }

    /** Return a string identifier of the competition which emitted the event
     * @return String identifier
     */
    public String getCompetitionId() {
        return this.competitionId;
    }

    /**
     * Return associations between competitors and scores.
     * @return Score map
     */
    public Map<T, Integer> getScores() {
        return this.scores;
    }

    /** Identifier of the competition */
    private String competitionId;

    /** Map associating competitors and scores */
    private Map<T, Integer> scores;

}
