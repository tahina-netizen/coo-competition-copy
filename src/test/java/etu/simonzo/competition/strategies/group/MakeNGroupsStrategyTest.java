package etu.simonzo.competition.strategies.group;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;

import java.util.*;

public class MakeNGroupsStrategyTest extends GroupingStrategyTest {

    private List<Competitor> competitors;

    /* Implement factory method */
    protected GroupingStrategy createGroupingStrategy() {
        return new MakeNGroupsStrategy(4);
    }

    @BeforeEach
    public void init() {
        this.competitors = new ArrayList<>();
        this.competitors.add(new Competitor("A"));
        this.competitors.add(new Competitor("B"));
        this.competitors.add(new Competitor("C"));
        this.competitors.add(new Competitor("D"));
        this.competitors.add(new Competitor("E"));
        this.competitors.add(new Competitor("F"));
        this.competitors.add(new Competitor("G"));
        this.competitors.add(new Competitor("H"));
    }

    /* Check that the MNGS constructor throws IllegalArgumentException if the
     * number of groups to create is less than 1 */
    @Test
    public void constructorThrowsIfNIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> {
                new MakeNGroupsStrategy(0);
            });
        assertThrows(IllegalArgumentException.class, () -> {
                new MakeNGroupsStrategy(-1);
            });
    }

    /* Check that group throws IllegalArgumentException when the total number of
     * competitors to split into groups is not a multiple of the number of
     * groups to create, ie when MNGS.group cannot create groups of equal size */
    @Test
    public void groupThrowsIfNumberOfCompetitorsIsNotMultipleOfN() {
        GroupingStrategy gstrat = new MakeNGroupsStrategy(4);
        this.competitors.add(new Competitor("I"));
        assertThrows(IllegalArgumentException.class, () -> {
                gstrat.group(this.competitors);
            });
    }

    /* Check that groups are composed using the documented strategy: make a
     * group with the m first competitors, then another with the m following
     * competitors, etc */
    @Test
    public void groupsAreWellComposed() {
        GroupingStrategy gstrat = new MakeNGroupsStrategy(4);
        Collection<List<Competitor>> groups = gstrat.group(this.competitors);
        assertEquals(4, groups.size());
        for (List<Competitor> g : groups) {
            assertEquals(2, g.size());
            assertTrue(
                (g.get(0) == this.competitors.get(0) &&
                 g.get(1) == this.competitors.get(1)) ||
                (g.get(0) == this.competitors.get(2) &&
                 g.get(1) == this.competitors.get(3)) ||
                (g.get(0) == this.competitors.get(4) &&
                 g.get(1) == this.competitors.get(5)) ||
                (g.get(0) == this.competitors.get(6) &&
                 g.get(1) == this.competitors.get(7)));
        }
    }

}
