package etu.simonzo.competition.observer.event;

import java.util.EventObject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitors.Competitor;

/**
 * Event which can be emitted to indicate that competitors have been selected
 * at the end of a group phase. This events encapsulates the list of qualified
 * competitors, and the collection of score maps (each group represented by a
 * map associating competitors to scores).
 * @param <T> Sub-type of Competitor
 */
public class QualifiedCompetitorsSelectedEvent<T extends Competitor> extends EventObject {

    /**
     * Create an event to indicate that competitors were selected in a
     * competition (for instance, at the end of a group phase).
     * @param source Competition which emitted the event
     * @param qualified List of qualified competitors
     * @param scores Collection of score maps. Each map represents the scores of
     * the competitors in a group
     */
    public QualifiedCompetitorsSelectedEvent(Competition<T> source, List<T> qualified,
                                             Collection<Map<T, Integer>> scores) {
        super(source);
        this.qualified = qualified;
        this.scores = scores;
    }

    /**
     * Return the list of qualified competitors
     * @return List of qualified competitors
     */
    public List<T> getQualified() {
        return this.qualified;
    }

    /**
     * Return the collection of score maps
     * @return Collection of maps, each map represents the scores of the
     * competitors in a group
     */
    public Collection<Map<T, Integer>> getScores() {
        return this.scores;
    }

    /** List of qualified competitors. */
    private List<T> qualified;

    /** Collection of score maps. */
    private Collection<Map<T, Integer>> scores;

}
