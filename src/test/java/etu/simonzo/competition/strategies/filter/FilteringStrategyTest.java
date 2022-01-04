package etu.simonzo.competition.strategies.filter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;

import java.util.*;

public abstract class FilteringStrategyTest {

    /* Factory method */
    protected abstract FilteringStrategy createFilteringStrategy();

    /* Check that the collection of qualified competitors only contains
     * competitors which are associated to a score in the score map passed as
     * argument of filter */
    @Test
    public void eachFilteredCompetitorHasAScore() {
        Competitor c1 = new Competitor("A");
        Competitor c2 = new Competitor("B");
        Competitor c3 = new Competitor("C");
        Competitor c4 = new Competitor("D");
        Competitor c5 = new Competitor("E");
        Competitor c6 = new Competitor("F");
        Collection<Map<Competitor, Integer>> scores = new ArrayList<>();
        Map<Competitor, Integer> scoreG1 = new HashMap<>();
        scoreG1.put(c1, 3);
        scoreG1.put(c2, 0);
        Map<Competitor, Integer> scoreG2 = new HashMap<>();
        scoreG2.put(c3, 3);
        scoreG2.put(c4, 0);
        Map<Competitor, Integer> scoreG3 = new HashMap<>();
        scoreG3.put(c5, 3);
        scoreG3.put(c6, 0);
        scores.add(scoreG1);
        scores.add(scoreG2);
        scores.add(scoreG3);
        FilteringStrategy fstrat = createFilteringStrategy();
        Collection<Competitor> qualified = fstrat.filter(scores);
        for (Competitor c : qualified) {
            assertTrue(c == c1 || c == c2 || c == c3 ||
                       c == c4 || c == c5 || c == c6);
        }
    }

    /* Check that the number of qualified competitors is less than or equal to
     * the number of competitors which are associated to a score in the score
     * map passed as argument of filter */
    @Test
    public void numberOfQualifiedCompetitorsInferiorToNumberOfKeys() {
        Competitor c1 = new Competitor("A");
        Competitor c2 = new Competitor("B");
        Competitor c3 = new Competitor("C");
        Competitor c4 = new Competitor("D");
        Competitor c5 = new Competitor("E");
        Competitor c6 = new Competitor("F");
        Collection<Map<Competitor, Integer>> scores = new ArrayList<>();
        Map<Competitor, Integer> scoreG1 = new HashMap<>();
        scoreG1.put(c1, 3);
        scoreG1.put(c2, 0);
        Map<Competitor, Integer> scoreG2 = new HashMap<>();
        scoreG2.put(c3, 3);
        scoreG2.put(c4, 0);
        Map<Competitor, Integer> scoreG3 = new HashMap<>();
        scoreG3.put(c5, 3);
        scoreG3.put(c6, 0);
        scores.add(scoreG1);
        scores.add(scoreG2);
        scores.add(scoreG3);
        FilteringStrategy fstrat = createFilteringStrategy();
        Collection<Competitor> qualified = fstrat.filter(scores);
        int nbKeys = 0;
        for (Map<Competitor, Integer> score : scores) {
            nbKeys += score.size();
        }
        assertTrue(qualified.size() <= nbKeys);
    }

}
