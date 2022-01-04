package etu.simonzo.competition.observer.listener;

import java.util.EventListener;

import etu.simonzo.competition.observer.event.*;
import etu.simonzo.competition.competitors.Competitor;

/**
 * Interface representing entities which can listen to events emitted by
 * competitions. Classes implementing this interface must listen to the
 * following events:
 * {@link MatchEvent}, {@link CompetitionStartEvent} and
 * {@link CompetitionEndEvent}
 * @param <T> Sub-type of Competitor
 */
public interface CompetitionListener<T extends Competitor> extends EventListener {

    /**
     * Listen and react to a {@link MatchEvent} emitted by a competition.
     * @param event Event indicating that a match was played
     */
    public void matchPlayed(MatchEvent<T> event);

    /**
     * Listen and react to a {@link CompetitionStartEvent} emitted by a
     * competition.
     * @param event Event indicating that a competition was started
     */
    public void competitionStarted(CompetitionStartEvent<T> event);

    /**
     * Listen and react to a {@link CompetitionEndEvent} emitted by a
     * competition.
     * @param event Event indicating that a competition was ended
     */
    public void competitionEnded(CompetitionEndEvent<T> event);

    /**
     * Listen and react to a {@link GroupsFormedEvent} emitted by a
     * competition.
     * @param event Event indicating that groups were formed in a groups-based
     * competition
     */
    public void groupsFormed(GroupsFormedEvent<T> event);

    /**
     * Listen and react to a {@link QualifiedCompetitorsSelectedEvent} emitted
     * by a competition.
     * @param event Event indicating that qualified competitors were selected
     */
    public void qualifiedCompetitorsSelected(QualifiedCompetitorsSelectedEvent<T> event);

    /**
     * Listen and react to a {@link TournamentPhaseStartedEvent} emitted by a
     * competition.
     * @param event Event indicating that the tournament phase of a competition
     * has started
     */
    public void tournamentPhaseStarted(TournamentPhaseStartedEvent<T> event);

}
