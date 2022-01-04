package etu.simonzo.competition.ranking;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.MatchOutcome;

/**
 * Represents the context of the outcome of a confrontation between two competitor.
 * In other words, tells which competitor confronted with another competitor and what
 * is the outcome of that confrontation.
 * Then, a result is defined by the two confronted competitors and
 * the outcome of the match i.e there is a winner (and a loser), or the confrontation
 * is tied.
 * The difference between {@link Result} is that this is a concrete class.
 */
public class SimpleResult<T extends Competitor> implements Result<T> {
    /** First competitor */
    protected T competitor1;

    /** Second competitor */
    protected T competitor2;

    /** Outcome of the match between the competitors */
    protected MatchOutcome outcome;

    /**
     * Create a confrontation result where <code>c1</code> confronted <code>c2</code>
     * and the outcome is <code>outcome</code>
     * @param c1 competitor which confronted <code>c2</code>. This competitor
     * will be identified as the "first".
     * @param c2 competitor which confronted <code>c1</code>. This competitor
     * will be identified as the "second".
     * @param outcome the outcome of the confrontation between <code>c1</code> and
     * <code>c2</code>. The possible values are:
     * <ul>
     *  <li>{@link MatchOutcome#TIE} to mean the confrontation tied (no winner)</li>
     *  <li>{@link MatchOutcome#FIRST_PLAYER_WIN} to mean that <code>c1</code>
     * won (and <code>c2</code> loosed)</li>
     *  <li>{@link MatchOutcome#SECOND_PLAYER_WIN} to mean that <code>c2</code>
     * won (and <code>c1</code> loosed)</li>
     * </ul>
     */
    public SimpleResult(T c1, T c2, MatchOutcome outcome) {
        this.competitor1 = c1;
        this.competitor2 = c2;
        this.outcome = outcome;
    }

    /**
     * gives the "first" competitor in this result
     * @return the "first" competitor in this result
     */
    public T getCompetitor1() {
        return this.competitor1;
    }

    /**
     * gives the "second" competitor in this result
     * @return the "second" competitor in this result
     */
    public T getCompetitor2() {
        return this.competitor2;
    }

    /**
     * give the outcome of this result (tie ?, there is a winner ?)
     * @return
     * <ul>
     *   <li>{@link MatchOutcome#TIE} iff tie (no winner)</li>
     *   <li>{@link MatchOutcome#FIRST_PLAYER_WIN} iff <code>the "first"
     * competitor</code> won</li>
     *   <li>{@link MatchOutcome#SECOND_PLAYER_WIN} iff <code>the "second"
     * competitor</code> won</li>
     * </ul>
     */
    public MatchOutcome getOutcome() {
        return this.outcome;
    }

}
