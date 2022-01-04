package etu.simonzo.competition.strategies.sort;

import java.util.*;

import etu.simonzo.competition.competitors.Competitor;

/**
 * Represents a strategy to sort a collection of competitors based on the
 * results of a group phase. These results are represented by a collection of
 * associative maps (each map associating competitors to their score inside of
 * their own group). Unqualified competitors may be present in the score maps,
 * but these competitors are ignored and do not appear in the sorted list of
 * competitors. The list which is returned by the sort method can be used as an
 * argument to create a new competition
 */
public interface SortingStrategy {

    /**
     * Return a list of competitors in a particular order. All the competitors
     * in the <code>qualifiedCompetitors</code> collection are present in the
     * returned list exactly once. The <code>groupScores</code> collection of
     * score associations is used to determine the sorting order
     * @param <T> Sub-type of Competitor
     * @param qualifiedCompetitors Competitors which should be present in the
     * sorted list of competitors
     * @param groupScores Collection of maps associating competitors to their
     * score in their respective groups
     * @return Sorted list of competitors
     * @throws IllegalArgumentException iff a qualified competitor can not be
     * found in any of the rankings represented by <code>groupScores</code>
     */
    <T extends Competitor> List<T>
    sort(Collection<T> qualifiedCompetitors, Collection<Map<T, Integer>> groupScores);

}
