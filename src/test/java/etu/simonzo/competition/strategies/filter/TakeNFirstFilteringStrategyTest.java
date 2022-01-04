package etu.simonzo.competition.strategies.filter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;

class TakeNFirstFilteringStrategyTest extends FilteringStrategyTest {

    private List<Competitor> competitors;

    @BeforeEach
    void setUp() throws Exception {
        competitors = listOfUsedCompetitors();
    }

    /* Implement factory method */
    protected FilteringStrategy createFilteringStrategy() {
        return createTakeNFirstFilteringStrategy();
    }

    /* Check that the TNFFS constructor throws if the number of competitors to
     * select in each group is less than 1 */
    @Test
    void constructorShouldThrowWhenNIsNotStrictlyPositive() {
        assertThrows(IllegalArgumentException.class,
                     () -> new TakeNFirstFilteringStrategy(0));
        assertThrows(IllegalArgumentException.class,
                     () -> new TakeNFirstFilteringStrategy(-1));
    }

    /* Check that the collection of qualified competitors returned by filter
     * contains (number of groups) * (number of competitors selected per group)
     * competitors */
    @Test
    void whenArgsOKFilterShouldReturnACollectionWithRightSize() {
        Collection<Map<Competitor, Integer>> scores = groupScoresExampleWithSomeExAequo();
        int n = usedValueOfN();
        int nbGroup = scores.size();
        int expectedSizeOfResult = n * nbGroup;
        TakeNFirstFilteringStrategy tnffs = createTakeNFirstFilteringStrategy();
        assertEquals(expectedSizeOfResult, tnffs.filter(scores).size());
    }

    /* Check that the collection of qualified competitors returned by filter
     * is one of the possible options (multiple options in the case off ex aequo
     * in one or more groups) */
    @Test
    void whenArgsOKFilterShouldReturnACollectionWithRightContent() {
        Collection<Map<Competitor, Integer>> scores = groupScoresExampleWithSomeExAequo();
        Collection<List<Competitor>> expectedOptionsCompetitors =
            expectedOptionsOfFilteredCompetitors();
        TakeNFirstFilteringStrategy tnffs = createTakeNFirstFilteringStrategy();
        Collection<Competitor> res = tnffs.filter(scores);

        boolean anyValidOption = false;
        for (List<Competitor> option : expectedOptionsCompetitors) {
            anyValidOption = anyValidOption ||
                ((res.size() == option.size()) && res.containsAll(option));
        }
        assertTrue(anyValidOption);
    }

    /* Check that filter throws IllegalArgumentException when one of the groups
     * contains less competitors than the number of competitors to select */
    @Test
    void whenNbOfCompetitorsInAGroupIsNotEnoughFilterShouldThrow() {
        Collection<Map<Competitor, Integer>> badScores = groupScoresExampleWithSomeExAequo();
        TakeNFirstFilteringStrategy tnffs = createTakeNFirstFilteringStrategyWithTooHighN();
        assertThrows(IllegalArgumentException.class,
                     () -> tnffs.filter(badScores)
            );
    }

    /* Ancillary methods to generate test inputs and outputs */

    protected List<Competitor> listOfUsedCompetitors() {
        List<Competitor> res = new ArrayList<>();
        res.add(new Competitor("A"));
        res.add(new Competitor("B"));
        res.add(new Competitor("C"));
        res.add(new Competitor("D"));
        res.add(new Competitor("E"));
        res.add(new Competitor("F"));
        res.add(new Competitor("G"));
        res.add(new Competitor("H"));
        res.add(new Competitor("I"));
        res.add(new Competitor("J"));
        res.add(new Competitor("K"));
        res.add(new Competitor("L"));
        res.add(new Competitor("M"));
        res.add(new Competitor("N"));
        res.add(new Competitor("0"));
        return res;
    }

    /**
     * @return
     * in this case, groups like this:
     *  group 1 (
     *  A -> 42,
     *  B -> 16,
     *  C -> 11,
     *  D -> 10,
     *  )
     *  group 2 (
     *  E -> 30,
     *  F -> 20,
     *  G -> 19,
     *  H -> 17,
     *  I -> 17,
     *  )
     *  group 3 (
     *  J -> 31,
     *  K -> 21,
     *  L -> 21,
     *  M -> 16,
     *  N -> 15,
     *  O -> 10,
     *  )
     * */
    protected Collection<Map<Competitor, Integer>> groupScoresExampleWithSomeExAequo() {
        Map<Competitor, Integer> group1 = new HashMap<>();
        group1.put(competitors.get(0), 42);
        group1.put(competitors.get(1), 16);
        group1.put(competitors.get(2), 11);
        group1.put(competitors.get(3), 10);
        Map<Competitor, Integer> group2 = new HashMap<>();
        group2.put(competitors.get(4), 30);
        group2.put(competitors.get(5), 20);
        group2.put(competitors.get(6), 19);
        group2.put(competitors.get(7), 17);
        group2.put(competitors.get(8), 17);
        Map<Competitor, Integer> group3 = new HashMap<>();
        group3.put(competitors.get(9), 31);
        group3.put(competitors.get(10), 21);
        group3.put(competitors.get(11), 21);
        group3.put(competitors.get(12), 16);
        group3.put(competitors.get(13), 15);
        group3.put(competitors.get(14), 10);
        Collection<Map<Competitor, Integer>> scores = new HashSet<>();
        scores.add(group1);
        scores.add(group2);
        scores.add(group3);
        return scores;
    }

    /**
     * @return the used value of <code>n</code> in
     * {@link TakeNFirstFilteringStrategyTest#createTakeNFirstFilteringStrategy()}
     */
    protected int usedValueOfN() {
        return 2;
    }

    /**
     * @return an instance that is typed as a TakeNFirstFilteringStrategy
     * subclass using {@link TakeNFirstFilteringStrategyTest#usedValueOfN()}
     * as the value of <code>n</code>
     */
    protected final TakeNFirstFilteringStrategy createTakeNFirstFilteringStrategy() {
        return new TakeNFirstFilteringStrategy(usedValueOfN());
    }

    private TakeNFirstFilteringStrategy createTakeNFirstFilteringStrategyWithTooHighN() {
        int maxN = 0;
        Collection<Map<Competitor, Integer>> scores = this.groupScoresExampleWithSomeExAequo();
        // calculate the maximum of N that is valid on "scores"
        for (Map<Competitor, Integer> g : scores) {
            if(g.size() > maxN) {
                maxN = g.size();
            }
        }
        return new TakeNFirstFilteringStrategy(maxN + 1);
    }

    private Collection<List<Competitor>> expectedOptionsOfFilteredCompetitors() {
        Collection<List<Competitor>> res = new ArrayList<>();
        List<Competitor> option1 = new ArrayList<>();
        option1.add(competitors.get(0));
        option1.add(competitors.get(1));
        option1.add(competitors.get(4));
        option1.add(competitors.get(5));
        option1.add(competitors.get(9));
        option1.add(competitors.get(10));
        List<Competitor> option2 = new ArrayList<>();
        option2.add(competitors.get(0));
        option2.add(competitors.get(1));
        option2.add(competitors.get(4));
        option2.add(competitors.get(5));
        option2.add(competitors.get(9));
        option2.add(competitors.get(11));
        res.add(option1);
        res.add(option2);
        return res;
    }

}
