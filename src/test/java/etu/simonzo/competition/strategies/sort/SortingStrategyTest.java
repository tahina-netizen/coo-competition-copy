package etu.simonzo.competition.strategies.sort;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;

import java.util.*;

public abstract class SortingStrategyTest {

    /* Factory method */
    protected abstract SortingStrategy createSortingStrategy();

    /* Check for IllegalArgumentException if sort is called with a list of
     * competitors containing one competitor with no associated score in the
     * score map */
    @Test
    public void sortThrowsIfCompetitorWithNoScore() {
        SortingStrategy sstrat = createSortingStrategy();
        Competitor c = new Competitor("Alpha");
        Collection<Competitor> qualified = new ArrayList<>();
        qualified.add(c);
        Map<Competitor, Integer> ranking = new HashMap<>();
        Collection<Map<Competitor, Integer>> scores = new ArrayList<>();
        scores.add(ranking);
        assertThrows(IllegalArgumentException.class, () -> {
                sstrat.sort(qualified, scores);
            });
    }

    /* Check that the sorted list of competitors only contains qualified
     * competitors, even if the score maps contains additional competitors */
    @Test
    public void sortedListOfCompetitorsOnlyContainsQualifiedCompetitors() {
        SortingStrategy sstrat = createSortingStrategy();
        Competitor c1 = new Competitor("A");
        Competitor c2 = new Competitor("B");
        Competitor c3 = new Competitor("C");
        Collection<Competitor> qualified = new ArrayList<>();
        qualified.add(c1);
        qualified.add(c2);
        Map<Competitor, Integer> ranking = new HashMap<>();
        ranking.put(c1, 0);
        ranking.put(c2, 0);
        ranking.put(c3, 0);
        Collection<Map<Competitor, Integer>> scores = new ArrayList<>();
        scores.add(ranking);
        List<Competitor> sortedQualified = sstrat.sort(qualified, scores);
        assertEquals(2, sortedQualified.size());
        assertTrue(sortedQualified.contains(c1));
        assertTrue(sortedQualified.contains(c2));
    }

}
