package etu.simonzo.competition.strategies.filter;

import java.util.*;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.util.MapUtil;

/**
 * This class represents a filtering strategy where the <code>n</code>
 * competitors with the highest scores in each group are chosen and returned as
 * a collection of competitors
 */
public class TakeNFirstFilteringStrategy implements FilteringStrategy {

    /**
     * Create an instance of the strategy, which can be used to keep only the
     * <code>n</code> competitors with the highest score in each group for the
     * next phase.
     * @param n Number of players to select in each group
     * @throws IllegalArgumentException iff <code>n</code> is not positive
     */
    public TakeNFirstFilteringStrategy(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is not positive");
        }
        this.n = n;
    }

    /**
     * Return the number of competitors which are selected in each group.
     * @return Number of competitors selected in each group
     */
    public int getN() {
        return this.n;
    }

    /**
     * Return a filtered collection of competitors, containing only those with
     * the highest scores in each group (the number of competitors chosen in
     * each group is decided at instanciation).
     * @param <T> Sub-type of Competitor
     * @param scores A collection of maps (each map representing a group of
     * competitors) associating competitors to their score
     * @return A collection of selected competitors, chosen using the "take the
     * <code>n</code> best in each group" strategy
     * @throws IllegalArgumentException iff one of the group contains less
     * competitors than the number to select
     */
    public <T extends Competitor> Collection<T>
    filter(Collection<Map<T, Integer>> scores) {
        Collection<T> qualified = new ArrayList<>();
        for (Map<T, Integer> ranking : scores) {
            if (ranking.size() < this.n) {
                throw new IllegalArgumentException("ranking has size < n");
            }
            Map<T, Integer> sortedRanking = MapUtil.sortByDescendingValue(ranking);
            int nbSelected = 0;
            for (T c : sortedRanking.keySet()) {
                if (nbSelected >= this.n) {
                    break;
                }
                qualified.add(c);
                nbSelected++;
            }
        }
        return qualified;
    }

    /** Number of players to select in each group */
    private int n;

}
