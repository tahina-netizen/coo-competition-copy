package etu.simonzo.competition.observer.listener;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.HashMap;

import etu.simonzo.competition.observer.event.*;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.displayers.Displayer;
import etu.simonzo.competition.util.MapUtil;

/**
 * Implementation of CompetitionListener which formats and prints information
 * about competitions, including competition start, list of participants,
 * competition end and list of qualified competitors. This class uses an
 * instance of {@link Displayer} to display the collected information.
 * The following format is used in display :
 * <pre>
 * {@code
 * Start competition IDENTIFIER
 * Competitors: COMPETITOR1, COMPETITOR2, ...
 * ...
 * End competition IDENTIFIER
 * Qualified competitors: COMPETITOR1, ...
 * }
 * </pre>
 * @param <T> Sub-type of Competitor
 */
public class Speaker<T extends Competitor> implements CompetitionListener<T> {

    /**
     * Create a speaker which uses the displayer passed as argument to
     * display information based on the events received.
     * @param displayer Displayer used to present information
     */
    public Speaker(Displayer displayer) {
        this.displayer = displayer;
    }

    /**
     * Display nothing.
     * @param event Event indicating that a match was played
     */
    public void matchPlayed(MatchEvent<T> event) {}

    /**
     * Display a message representing the start of a competition, using the
     * following format:
     * <pre>
     * {@code
     *
     * ========================================
     * Start competition IDENTIFIER
     * ========================================
     *
     * Competitors:
     * ------------
     * COMPETITOR1
     * COMPETITOR2
     * ...
     *
     * }
     * </pre>
     * @param event Event indicating that a competition was started
     */
    public void competitionStarted(CompetitionStartEvent<T> event) {
        this.printNewline();
        this.printSeparator();
        this.displayer.displayMessage("Start competition " + event.getCompetitionId() + "\n");
        this.printSeparator();
        this.printNewline();
        this.displayer.displayMessage("Competitors:\n");
        this.displayer.displayMessage("------------\n");

        for (T participant : event.getParticipants()) {
            this.displayer.displayMessage(participant.getName() + "\n");
        }
        this.printNewline();
    }

    /**
     * Display a message representing the end of a competition, using the
     * following format:
     * <pre>
     * {@code
     *
     * ========================================
     * End competition IDENTIFIER
     * ========================================
     *
     * Results:
     * --------
     * COMPETITOR1 12
     * COMPETITOR2 9
     * ...
     * }
     * </pre>
     * @param event Event indicating that a competition was ended
     */
    public void competitionEnded(CompetitionEndEvent<T> event) {
        this.printNewline();
        this.printSeparator();
        this.displayer.displayMessage("End competition " + event.getCompetitionId() + "\n");
        this.printSeparator();
        this.printNewline();
        this.displayer.displayMessage("Results:\n");
        this.displayer.displayMessage("--------\n");

        Map<T, Integer> sortedRanking = MapUtil.sortByDescendingValue(event.getScores());
        int lengthOfLongestName = this.longestNameLength(sortedRanking.keySet());
        for (T competitor : sortedRanking.keySet()) {
            this.displayer.displayMessage(competitor.getName());
            int nbSpaces = lengthOfLongestName - competitor.getName().length() + 1;
            for (int i = 0; i < nbSpaces; i++) {
                this.displayer.displayMessage(" ");
            }
            this.displayer.displayMessage(sortedRanking.get(competitor) + "\n");
        }
    }

    /**
     * Display a message representing the fact that groups were formed in a
     * competition, using the following format:
     * <pre>
     * {@code
     * Groups:
     * -------
     * Group1      Group2      Group3      ...
     * COMPETITOR1 COMPETITOR3 COMPETITOR5 ...
     * COMPETITOR2 COMPETITOR4 COMPETITOR6 ...
     * ...         ...         ...
     * }
     * </pre>
     */
    public void groupsFormed(GroupsFormedEvent<T> event) {
        this.displayer.displayMessage("Groups:\n");
        this.displayer.displayMessage("-------\n");

        // preparation
        List<ListIterator<T>> iterators = new ArrayList<>();
        Map<ListIterator<T>, Integer> lengthsOfLongestNames = new HashMap<>();
        int sizeOfLargestGroup = 0;
        for (List<T> group : event.getGroups()) {
            ListIterator<T> it = group.listIterator();
            iterators.add(it);
            lengthsOfLongestNames.put(it, longestNameLength(group));
            if (group.size() > sizeOfLargestGroup) {
                sizeOfLargestGroup = group.size();
            }
        }

        // display group names
        for (int i= 0; i < event.getGroups().size(); i++) {
            int padTo = Math.max(lengthsOfLongestNames.get(iterators.get(i)) + 1,
                                 GROUP_DESC_LEN);
            this.printGroupDescription(i + 1, padTo);
        }
        this.printNewline();

        // display competitor names
        for (int i = 0; i < sizeOfLargestGroup; i++) {
            for (ListIterator<T> it : iterators) {
                int padTo = Math.max(lengthsOfLongestNames.get(it) + 1,
                    GROUP_DESC_LEN);
                this.printNextCompetitor(it, padTo);
            }
            this.printNewline();
        }
    }

    /**
     * Display a message representing the fact that competitors were selected in
     * a competition, using the following format:
     * <pre>
     * {@code
     * Selected groups:
     * ----------------
     * Group1         Group2         Group3         ...
     * COMPETITOR1 *  COMPETITOR3    COMPETITOR5 *  ...
     * COMPETITOR2    COMPETITOR4 *  COMPETITOR6    ...
     * ...            ...            ...
     * }
     * </pre>
     */
    public void qualifiedCompetitorsSelected(QualifiedCompetitorsSelectedEvent<T> event) {
        this.printNewline();
        this.displayer.displayMessage("Selected groups:\n");
        this.displayer.displayMessage("----------------\n");

        // preparation
        List<List<T>> sortedGroups = this.getSortedGroups(event.getScores());
        List<ListIterator<T>> iterators = new ArrayList<>();
        Map<ListIterator<T>, Integer> lengthsOfLongestNames = new HashMap<>();
        int sizeOfLargestGroup = 0;
        for (List<T> group : sortedGroups) {
            ListIterator<T> it = group.listIterator();
            iterators.add(it);
            lengthsOfLongestNames.put(it, longestNameLength(group));
            if (group.size() > sizeOfLargestGroup) {
                sizeOfLargestGroup = group.size();
            }
        }

        // display group names
        for (int i= 0; i < sortedGroups.size(); i++) {
            int padTo = Math.max(lengthsOfLongestNames.get(iterators.get(i)) + 4,
                                 GROUP_DESC_LEN);
            this.printGroupDescription(i + 1, padTo);
        }
        this.printNewline();

        // display competitor names
        for (int i = 0; i < sizeOfLargestGroup; i++) {
            for (ListIterator<T> it : iterators) {
                int padTo = Math.max(lengthsOfLongestNames.get(it) + 4,
                                     GROUP_DESC_LEN);
                this.printNextCompetitorStar(it, event.getQualified(), padTo);
            }
            this.printNewline();
        }
    }

    /**
     * Display a message representing the fact that a tournament phase has
     * started in a competition, using the format:
     * <pre>
     * {@code
     *
     * 1/4 finales
     *
     * }
     * </pre>
     */
    public void tournamentPhaseStarted(TournamentPhaseStartedEvent<T> event) {
        this.printNewline();
        if (event.getPhase() == 1) {
            this.displayer.displayMessage("Finale\n");
        } else {
            this.displayer.displayMessage("1/" + event.getPhase() + " finales\n");
        }
        this.printNewline();
    }

    /**
     * Return the length of the longest name of all the competitors in the
     * collection passed as argument (in number of characters).
     * @param competitors A collection of named competitors
     * @return Length of the longest name
     */
    private int longestNameLength(Collection<T> competitors) {
        int length = 0;
        for (T c : competitors) {
            if (c.getName().length() > length) {
                length = c.getName().length();
            }
        }
        return length;
    }

    /**
     * Print the description of a group, right-padded to a total of padTo
     * characters. May print more characters than padTo.
     * @param number Number of the group
     * @param padTo Total number of columns to use if possible
     */
    private void printGroupDescription(int number, int padTo) {
        this.displayer.displayMessage(String.format("%s %d ", GROUP_DESC, number));
        int nbSpaces = Math.max(0, padTo - GROUP_DESC_LEN);
        this.printSpaces(nbSpaces);
    }

    /**
     * Print the name of the next competitor in the iterator, right-padded to a
     * total of padTo characters. If there is no next competitor, print padTo
     * spaces. May print more characters than padTo.
     * @param it Iterator referencing a list of competitors
     * @param padTo Total number of columns to use if possible
     */
    private void printNextCompetitor(ListIterator<T> it, int padTo) {
        if (it.hasNext()) {
            String name = it.next().getName();
            this.displayer.displayMessage(name);
            int nbSpaces = Math.max(0, padTo - name.length());
            this.printSpaces(nbSpaces);
        } else {
            this.printSpaces(padTo);
        }
    }

    /**
     * Print the name of the next competitor in the iterator, right-padded to a
     * total of padTo characters. If the competitor is qualifed, their name is
     * followed by an asterisk. If there is no next competitor, print padTo
     * spaces. May print more characters than padTo.
     * @param it Iterator referencing a list of competitors
     * @param qualified List of qualified competitors
     * @param padTo Total number of columns to use if possible
     */
    private void printNextCompetitorStar(ListIterator<T> it, List<T> qualified, int padTo) {
        if (it.hasNext()) {
            T competitor = it.next();
            String name = competitor.getName();
            this.displayer.displayMessage(name);
            if (qualified.contains(competitor)) {
                this.displayer.displayMessage(" *");
            } else {
                this.displayer.displayMessage("  ");
            }
            int nbSpaces = Math.max(0, padTo - name.length() - 2);
            this.printSpaces(nbSpaces);
        } else {
            this.printSpaces(padTo);
        }
    }

    /**
     * Return a list of lists of competitors. Each sub-list represents a group,
     * in which competitors are sorted based on their scores in their
     * group. Competitors with the most points come first.
     * @param scores Collection of maps, where each map associates competitors
     * to scores in a particular group
     * @return List of sorted lists of competitors
     */
    private List<List<T>> getSortedGroups(Collection<Map<T, Integer>> scores) {
        List<List<T>> res = new ArrayList<>();
        for (Map<T, Integer> m : scores) {
            Map<T, Integer> sortedMap = MapUtil.sortByDescendingValue(m);
            List<T> sortedCompetitors = new ArrayList<>();
            for (T competitor : sortedMap.keySet()) {
                sortedCompetitors.add(competitor);
            }
            res.add(sortedCompetitors);
        }
        return res;
    }

    /**
     * Print a number of spaces using the displayer.
     * @param n Number of spaces to display
     */
    private void printSpaces(int n) {
        for (int i = 0; i < n; i++) {
            this.displayer.displayMessage(" ");
        }
    }

    /**
     * Print a separator line.
     */
    private void printSeparator() {
        this.displayer.displayMessage("========================================\n");
    }

    /**
     * Print a new line character.
     */
    private void printNewline() {
        this.displayer.displayMessage("\n");
    }

    /** Displayer used by the journalist instance */
    private Displayer displayer;

    private static final String GROUP_DESC = "Group";

    private static final int GROUP_DESC_LEN = 8;

}
