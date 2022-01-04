package etu.simonzo.competition.strategies.group;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import etu.simonzo.competition.competitors.Competitor;

/**
 * An algorithm of making groups (in the group stage) defined by the number of
 * groups this algorithm should make (we will call it <code>N</code>).
 * With a given list of competitors, this algorithm proceed to make exactly
 * <code>N</code> groups from it.
 * Let's suppose the list of competitors contains <code>n</code> competitors,
 * to make this algorithm works, <code>n</code> must be a multiple of
 * <code>N</code>.
 * So, each constructed
 * group will contains exactly <code>n / N</code> competitors
 * For example:
 * <ul>
 *  <li>if the given list is [A, B, C, D, E, F, G, H] and we have
 *      to make 4 groups, then this algorithm will make 4 groups with 2
 *      competitors per group like [A, B], [C, D], [E, F], [G, H].
 *  </li>
 *  <li>
 *      if the given list is [B, A, C, E, D, G, H, F] and we have to make 4
 *      groups, then this algorithm will make 4 groups with 2 competitors
 *      per group like like [B, A], [C, E], [D, G], [H, F]
 *  </li>
 * </ul>
 * As illustrated in the examples, the order in the given list influences the
 * making of the groups.
 *
 */
public class MakeNGroupsStrategy implements GroupingStrategy {

    /**
     * Create an instance of MakeNGroupsStrategy with the given number
     * of groups to make. So the call to {@link MakeNGroupsStrategy#group(List)}
     * will make exactly <code>n</code> groups. The number of groups must be
     * strictly positive, else an exception is thrown.
     * @param n the number of groups this instance have to make
     * @throws IllegalArgumentException iff <code>n</code> is not strictly positive
     */
    public MakeNGroupsStrategy(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n is not strictly positive");
        }
        this.n = n;
    }

    /**
     * gives the number of groups this strategy makes
     * @return number of groups this strategy makes
     */
    public int getN() {
        return this.n;
    }

    /**
     * With <code>n</code> as the integer given at construction, make exactly
     * <code>n</code> groups with the given list of competitors.
     * <strong>the order in the given list influences the
     * making of the groups.</strong>
     * @param competitors the list of competitors that will be used to make the
     * groups.
     * Its size has to be a multiple of <code>n</code>
     * @return <code>n</code> groups formed from the given list of competitors
     * @throws IllegalArgumentException if the size of <code>competitors</code>
     * is not a multiple of <code>n</code>
     */
    public <T extends Competitor> Collection<List<T>> group(List<T> competitors) {
        if (competitors.size() % getN() != 0) {
            throw new IllegalArgumentException(
                    "n is not a multiple of the size of competitors");
        }
        Collection<List<T>> res = new LinkedList<>();
        
        Iterator<T> iterator = competitors.iterator();
        int groupSize = competitors.size() / getN();
        while(iterator.hasNext()) {
            List<T> currentGroup = new LinkedList<>();
            for(int i=0; i < groupSize; i++) {
                currentGroup.add(iterator.next());
            }
            res.add(currentGroup);   
        }
        return res;
    }
    
    /**
     * the number of group this strategy makes
     */
    private int n;

}
