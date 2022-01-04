package etu.simonzo.competition.strategies.group;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;

import java.util.*;

public abstract class GroupingStrategyTest {

    /* Factory method */
    protected abstract GroupingStrategy createGroupingStrategy();

    /* Check that each competitors from the list passed as argument of group is
     * assigned to a group, ie is part of one the lists in the collection of
     * list returned by group */
    @Test
    public void eachCompetitorIsAssignedToAGroup() {
        List<Competitor> competitors = makeCompetitors();
        GroupingStrategy gstrat = createGroupingStrategy();
        Collection<List<Competitor>> groups = gstrat.group(competitors);
        for (Competitor c : competitors) {
            assertTrue(competitorIsInAGroup(c, groups));
        }
    }

    /* Like the previous one, but check that each competitor is assigned to
     * exactly one group, under the condition that the original list of
     * competitors does not contain duplicates */
    @Test
    public void eachCompetitorIsExactlyAssignedToAGroupWhenNoDuplicate() {
        List<Competitor> competitors = makeCompetitors();
        GroupingStrategy gstrat = createGroupingStrategy();
        Collection<List<Competitor>> groups = gstrat.group(competitors);
        for (Competitor c : competitors) {
            assertTrue(competitorIsInExactlyOneGroup(c, groups));
        }
    }

    /* Check that each competitor in each of the lists returned by group was
     * part of the original list of competitors to split into groups (passed as
     * argument to group) */
    @Test
    public void eachGroupedCompetitorWasInOriginalList() {
        List<Competitor> competitors = makeCompetitors();
        GroupingStrategy gstrat = createGroupingStrategy();
        Collection<List<Competitor>> groups = gstrat.group(competitors);
        for (List<Competitor> group : groups) {
            for (Competitor c : group) {
                assertTrue(competitors.contains(c));
            }
        }
    }

    /* Ancillary methods to generate test inputs and verify outputs */

    private boolean competitorIsInAGroup(
        Competitor c, Collection<List<Competitor>> groups) {
        for (List<Competitor> group : groups) {
            if (group.contains(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean competitorIsInExactlyOneGroup(
        Competitor c, Collection<List<Competitor>> groups) {
        int nbOfGroupsContainingCompetitor = 0;
        for (List<Competitor> group : groups) {
            if (group.contains(c)) {
                nbOfGroupsContainingCompetitor++;
            }
        }
        return (nbOfGroupsContainingCompetitor == 1);
    }

    private List<Competitor> makeCompetitors() {
        List<Competitor> competitors = new ArrayList<>();
        competitors.add(new Competitor("A"));
        competitors.add(new Competitor("B"));
        competitors.add(new Competitor("C"));
        competitors.add(new Competitor("D"));
        competitors.add(new Competitor("E"));
        competitors.add(new Competitor("F"));
        competitors.add(new Competitor("G"));
        competitors.add(new Competitor("H"));
        return competitors;
    }

}
