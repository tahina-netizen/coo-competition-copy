package etu.simonzo.competition.ranking;

import java.util.Collection;
import java.util.Map;

import etu.simonzo.competition.competitors.Competitor;

/**
 * Given a set of match results (matches between competitors), this interface can
 * compute the ranking of the competitors.
 * Gradually as match results is given to this interface (via
 * {@link RankingHandler#addResult(Result)}), this interface can (gradually)
 * compute the ranking. So each time a match result is given, the ranking may
 * change.
 *
 */
public interface RankingHandler<T extends Competitor> {
    /**
     * make this ranking handler take account of the given match result
     * @param result a match result this ranking handler have to take account
     * @throws IllegalArgumentException if the result is invalid (eg if one of
     * the competitors is unknown)
     */
    public void addResult(Result<T> result);

    /**
     * gives the collection of the match results already taken account by this ranking handler
     * @return the collection of the match results already taken account by this ranking handler
     */
    public Collection<Result<T>> getResults();

    /**
     * Gives the ranking computed from the set of match results already given to
     * this ranking handler
     * @return the ranking computed from the set of match results already given
     * to this ranking handler.
     * Its a map where:
     * <ol>
     *  <li>key: a competitor instance</li>
     *  <li>value: the score of that competitor</li>
     * </ol>
     */
    public Map<T, Integer> getRanking();
}
