package etu.simonzo.competition.strategies.group;

import java.util.Collection;
import java.util.List;

import etu.simonzo.competition.competitors.Competitor;

/**
 * Represents an algorithm used to make groups (in group stage) from a set 
 * of competitors.
 * With a set of competitors, there is many ways to make groups with it.
 * 
 * For example, if we have 8 competitors [A, B, C, D, E, F, G, H], we can make 
 * (for example):
 * <ul>
 *  <li>4 groups with 2 players in each group: [A, B], [C, D], [E, F], [G, H]</li>
 *  <li>3 groups with 2 groups with 3 players and one with 2 players: [A, B, C], 
 *  [D, E, F], [G, H]</li>
 * </ul>
 * And many others (infinite) ways to make groups ...
 * So, an instance of a class that implements this interface define a (precise)
 * way to make the grouping.  
 *
 */
public interface GroupingStrategy {
    /**
     * groups the given list of competitors using the algorithm defined in this
     * method.
     * @param <T> a subclass of competitors.
     * @param competitors the list of competitors to group
     * @return A collection of groups. Each element of the collection represents a group.
     */
    <T extends Competitor> Collection<List<T>>    
    group(List<T> competitors);
}
