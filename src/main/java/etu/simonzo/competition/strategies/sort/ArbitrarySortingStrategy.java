package etu.simonzo.competition.strategies.sort;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import etu.simonzo.competition.competitors.Competitor;

/**
 * Represents a "default" sorting strategy. This strategy just iterate
 * over the elements of the given collection of competitors and add them
 * to the list to return.
 * In other words, no genuine sorting is done, the collection
 * of competitors is returned "as it is" (but as a list)
 *
 */
public class ArbitrarySortingStrategy implements SortingStrategy {

    /**
     * Gives a list of competitors where the order
     * is the order with which <code>qualifiedCompetitors</code>'s
     * iterator iterate over it.
     * No genuine sorting is done, the collection
     * of competitors is returned "as it is" (but as a list)
     *
     * @param <T> Sub-type of Competitor
     * @param qualifiedCompetitors Competitors which should be present in the
     * sorted list of competitors
     * @param groupScores Collection of maps associating competitors to their
     * score in their respective groups
     * @return the resulting list of competitors
     * @throws IllegalArgumentException iff a qualified competitor can not be
     * found in any of the rankings represented by <code>groupScores</code>
     */
    public <T extends Competitor> List<T> sort(Collection<T> qualifiedCompetitors,
            Collection<Map<T, Integer>> groupScores) {
        List<T> res = new LinkedList<>();
        for (T competitor: qualifiedCompetitors) {
            if (!inAnyRanking(competitor, groupScores)) {
                throw new IllegalArgumentException("competitor with no score");
            }
            res.add(competitor);
        }
        return res;
    }

    /**
     * Return true if the competitor has a score in any ranking
     * @param <T> Sub-type of Competitor
     * @param c Competitor to look up in rankings
     * @param rankings Collection of ranking maps
     * @return <code>true</code> iff the competitor is associated to a score in
     * any ranking of the collection
     */
    private <T extends Competitor>
    boolean inAnyRanking(T c, Collection<Map<T, Integer>> rankings) {
        for (Map<T, Integer> r : rankings) {
            if (r.containsKey(c)) {
                return true;
            }
        }
        return false;
    }

}
