package etu.simonzo.competition.competitions;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.match.MatchOutcome;
import etu.simonzo.competition.observer.event.CompetitionEndEvent;
import etu.simonzo.competition.observer.event.CompetitionStartEvent;
import etu.simonzo.competition.observer.event.GroupsFormedEvent;
import etu.simonzo.competition.observer.event.MatchEvent;
import etu.simonzo.competition.observer.event.QualifiedCompetitorsSelectedEvent;
import etu.simonzo.competition.observer.event.TournamentPhaseStartedEvent;
import etu.simonzo.competition.observer.listener.CompetitionListener;
import etu.simonzo.competition.ranking.RankingHandler;

/**
 * <p>
 * Represents a competition. A competition maybe seen as set of match between
 * several competitors. Each match is between two competitors. All match is
 * under an unique "rule"/"context"/"mechanism" defined at the creation of a
 * competition.  A current ranking between all competitors can be provided at
 * the end of the competition. For this purpose, a RankingHandler instance is
 * passed as a constructor argument. The number of points associated with a
 * victory, a loss or a tie are the responsibility of the ranking handler. If
 * some players are duplicated in the list of competitors, all matches will be
 * played nonetheless eg. if the list contains two references to the same
 * Competitor, and if the pairing rule sets up a match between him and himself,
 * this match will be played. Users must make sure that the list contains no
 * duplicate if they want to avoid this behaviour.
 * </p>
 * <p>
 * This class is also observable. So (almost) every "event" inside an instance
 * of this class is notified to its observers.
 * What is meant by "event" is:
 * </p>
 * <ul>
 *   <li>when the competition begins</li>
 *   <li>when the competition ends</li>
 *   <li>when a match is played</li>
 *   <li>and <strong>potentially</strong> when some intermediate event happens
 *   in the middle of the competition</li>
 * </ul>
 */
public abstract class Competition<T extends Competitor> {

    public Competition(Match<T> match, List<T> competitors, RankingHandler<T> handler, String id) {
        this.match = match;
        this.competitors = competitors;
        this.rankingHandler = handler;
        this.listeners = new ArrayList<>();
        this.identifier = id;
    }

    /**
     * Launch this competition from its beginning to its end.  Matches between
     * competitors are organized (i.e how many match to do? which competitor
     * play against which competitor ?)  Matches between competitors are played.
     * The final ranking ({@link Competition#ranking()} of each competitor can
     * be provided after the call to this method.
     * <strong>This method should not be called more than once. Else the
     * behaviour is undefined</strong>
     */
    public void play() {
        fireCompetitionStarted();
        this.play(this.competitors);
        fireCompetitionEnded(ranking());
    }

    /**
     * Organize the matches between the given competitors (i.e how many match to
     * do? which competitor play against which competitor ?). Play each match.
     * @param competitors List of competitors to organize and play match with.
     */
    protected abstract void play(List<T> competitors);

    /**
     * Play the match between <code>c1</code> and <code>c2</code> (of course,
     * according to the "rule").
     * Update the ranking of the competitors according to the outcome of the
     * match (i.e if there is a winner or if it's a tie).
     * @param c1 competitor that will play against <code>c2</code>
     * @param c2 competitor that will play against <code>c1</code>
     */
    protected abstract void playMatch(T c1, T c2);

    /**
     * Return the ranking at the end of the competition. Each competitor gain
     * some points depending on the outcome of each match it played. The gained
     * point for a competitor for a win, tie and loss are the responsibility of
     * the ranking handler.
     * <strong>This method should only be called after a call to
     * {@link Competition#play()}, else the behaviour is undefined</strong>
     * @return a map: key= a competitor, value=the score of that competitor
     */
    public Map<T, Integer> ranking() {
        return this.rankingHandler.getRanking();
    }

    /**
     * Return the match "rule"/"context"/"mechanism" used in this competition.
     * @return the match "rule"/"context"/"mechanism" used in this competition.
     */
    protected Match<T> getMatch() {
        return this.match;
    }

    /**
     * Return the list of competitors involved in the competition, and
     * registered on instanciation
     * @return the list of competitors playing in this competition.
     */
    protected List<T> getCompetitors() {
        return this.competitors;
    }

    /**
     * Return the ranking handler instance associated with this competition
     * @return Ranking handler used to rank competitors
     */
    protected RankingHandler<T> getRankingHandler() {
        return this.rankingHandler;
    }

    /**
     * Add a listener that will observe this competition's "state change".
     * @param listener the listener that will be notified
     */
    public synchronized void addCompetitionListener(CompetitionListener<T> listener) {
        if (! this.listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Ensure that a listener is not observing this competition anymore.
     * So, that listener will not receive notification from this competition.
     * If <code>listener</code> wasn't listening, this methods "does nothing".
     *
     * @param listener listener to remove from observing this competition
     */
    public synchronized void removeCompetitionListener(CompetitionListener<T> listener) {
       this.listeners.remove(listener);
    }

    /**
     * Gives the list of listeners subscribed to this competition
     * @return the list of listeners subscribed to this competition
     */
    protected List<CompetitionListener<T>> getListeners() {
        return this.listeners;
    }

    /**
     * Create and spread a
     * {@link etu.simonzo.competition.observer.event.MatchEvent}
     * to every listeners of this competition. That event represents the
     * information that a match was played. Given parameters describe the played
     * match.
     *
     * @param c1 the first competitor of the played match
     * @param c2 the second competitor of the played match
     * @param outcome the outcome of the played match.
     * <strong>The order of the first and second argument is critical:</strong>
     * <ul>
     *      <li>{@link MatchOutcome#FIRST_PLAYER_WIN} means <code>c1</code> won
     *      <li>{@link MatchOutcome#SECOND_PLAYER_WIN} means <code>c2</code> won
     *      <li>{@link MatchOutcome#TIE} means no one won (tie).
     * </ul>
     */
    protected void fireMatchPlayed(T c1, T c2, MatchOutcome outcome) {
       List<CompetitionListener<T>> listenersCopy = List.copyOf(this.listeners);
       MatchEvent<T> event = new MatchEvent<>(this, c1, c2, outcome);
       for (CompetitionListener<T> l: listenersCopy) {
           l.matchPlayed(event);
       }
    }

    /**
     * Create and spread a
     * {@link etu.simonzo.competition.observer.event.CompetitionStartEvent} to
     * every listeners of this competition. That event represents the
     * information that this competition started.
     */
    protected void fireCompetitionStarted() {
        List<CompetitionListener<T>> listenersCopy = List.copyOf(this.listeners);
        CompetitionStartEvent<T> event =
                new CompetitionStartEvent<>(this, this.identifier, this.getCompetitors());
        for (CompetitionListener<T> l: listenersCopy) {
            l.competitionStarted(event);
        }
    }

    /**
     * Create and spread a
     * {@link etu.simonzo.competition.observer.event.CompetitionEndEvent} to
     * every listeners of this competition. That event represents the
     * information that this competition ended.
     * @param scores the map that associates each competitor to its scores at the
     * the end of this competition
     */
    protected void fireCompetitionEnded(Map<T, Integer> scores) {
        List<CompetitionListener<T>> listenersCopy = List.copyOf(this.listeners);
        CompetitionEndEvent<T> event =
                new CompetitionEndEvent<>(this, this.identifier, scores);
        for (CompetitionListener<T> l: listenersCopy) {
            l.competitionEnded(event);
        }
    }

    /**
     * Create and spread a
     * {@link etu.simonzo.competition.observer.event.GroupsFormedEvent} to
     * every listeners of this competition. That event represents the
     * information that some groups has been formed in this competition
     * (typically at the group stage).
     * @param groups a collection of list where each list represents a group
     * formed in this competition
     */
    protected void fireGroupsFormed(Collection<List<T>> groups) {
        List<CompetitionListener<T>> listenersCopy = List.copyOf(this.listeners);
        GroupsFormedEvent<T> event =
                new GroupsFormedEvent<>(this, groups);
        for (CompetitionListener<T> l: listenersCopy) {
            l.groupsFormed(event);
        }
    }

    /**
     * Create and spread a
     * {@link etu.simonzo.competition.observer.event.QualifiedCompetitorsSelectedEvent}
     * to every listeners of this competition. That event represents the
     * information that some competitors has been selected (typically, those
     * selected ones will advance to the next stage of the competition).
     * @param qualified the selected (qualified) competitors
     * @param scores a collection of maps where each map associtates competitors to scores
     * in their particular group (each map may contain unselected competitors)
     */
    protected void fireQualifiedCompetitorsSelectedEvent(List<T> qualified, Collection<Map<T, Integer>> scores) {
        List<CompetitionListener<T>> listenersCopy = List.copyOf(this.listeners);
        QualifiedCompetitorsSelectedEvent<T> event =
                new QualifiedCompetitorsSelectedEvent<>(this, qualified, scores);
        for (CompetitionListener<T> l: listenersCopy) {
            l.qualifiedCompetitorsSelected(event);
        }
    }

    /**
     * Create and spread a
     * {@link etu.simonzo.competition.observer.event.TournamentPhaseStartedEvent}
     * to every listeners of this competition. That event represents the
     * information that a tournament phase (such as eight final, quarter final,
     * semi final, ...) started, i.e matches concerning that phase will be soon
     * played.
     * @param phase an integer representing the concerned phase
     * (<code>8</code> for <code>1/8th</code>, <code>4</code>
     * for <code>1/4th</code>, ...)
     *
     */
    protected void fireTournamentPhaseStartedEvent(int phase) {
        List<CompetitionListener<T>> listenersCopy = List.copyOf(this.listeners);
        TournamentPhaseStartedEvent<T> event =
                new TournamentPhaseStartedEvent<>(this, phase);
        for (CompetitionListener<T> l: listenersCopy) {
            l.tournamentPhaseStarted(event);
        }
    }

    /** Match rule used for each confrontation of competitors */
    private Match<T> match;

    /** List of all competitors of this competition */
    private List<T> competitors;

    /** Ranking handler to manage points and ranking of competitors */
    private RankingHandler<T> rankingHandler;

    /**
     * List of listeners subscribed to this competition
     */
    private List<CompetitionListener<T>> listeners;

    /**
     * An identifier of this competition. (should be unique)
     */
    protected String identifier;
}
