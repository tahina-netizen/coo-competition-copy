package etu.simonzo.competition.observer.event;

import java.util.EventObject;
import java.util.Collection;
import java.util.List;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitors.Competitor;

/**
 * Event which can be emitted to indicate that groups of competitors have been
 * created in a competition. It encapsulates a collection of lists of
 * competitors, which correspond to the groups.
 * @param <T> Sub-type of Competitor
 */
public class GroupsFormedEvent<T extends Competitor> extends EventObject {

    /**
     * Create an event to indicate that groups of competitors were created.
     * @param source Competition which emitted the event
     * @param groups Collection of lists of competitors, representing the
     * created groups
     */
    public GroupsFormedEvent(Competition<T> source, Collection<List<T>> groups) {
        super(source);
        this.groups = groups;
    }

    /**
     * Return the collection of groups of competitors.
     * @return Collection of lists representing groups
     **/
    public Collection<List<T>> getGroups() {
        return this.groups;
    }

    /** Collection of groups of competitors */
    private Collection<List<T>> groups;

}
