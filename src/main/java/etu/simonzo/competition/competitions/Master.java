package etu.simonzo.competition.competitions;

import java.util.*;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.observer.listener.CompetitionListener;
import etu.simonzo.competition.ranking.SimpleRankingHandler;
import etu.simonzo.competition.strategies.filter.FilteringStrategy;
import etu.simonzo.competition.strategies.group.GroupingStrategy;
import etu.simonzo.competition.strategies.sort.SortingStrategy;

/**
 * <p>A competition consisting of two phases: a group phase followed by a
 * tournament phase. During the first phase, competitors are first split into
 * groups. Then matches are carried out inside of each group (in a round trip
 * fashion, each competitor playing against every other competitor in his group
 * exactly twice). Then group results are used to determine which competitors
 * are qualified for the second phase of the Master. The second phase consists
 * in a tournament in which matches are played in a knock-out manner. After the
 * tournament phase is over, the final ranking may be obtained.
 *
 * A Master is defined by the method used to select competitors between the two
 * phases of the competition. The selection process is divided in three steps,
 * each of these steps represented by an instance of a particular strategy
 * class: a strategy to split competitors into groups, a strategy to choose
 * qualified players given the results of the different groups, and a strategy
 * to order the list of qualified competitors. This last step is relevant
 * because the order in which competitors are registered for a competition
 * determines the way they will be paired up.
 *
 * Strategy objects are passed as constructor arguments. <strong>The user is
 * responsible of the adequation between the strategies.</strong> Most notably,
 * the user should make sure that the size of the final list of qualified
 * competitors is a power of two, so that a tournament can be played.
 * </p>
 *
 * <p>
 * In a master, there are many intermediate event that should be interesting to
 * observers. Since a master is composed of many (sub-)competition, an observer
 * of a master also observes its components' events, that are:
 * </p>
 * <ul>
 *   <li>
 *   When groups are formed,
 *   {@link etu.simonzo.competition.observer.event.GroupsFormedEvent};
 *   </li>
 *   <li>
 *   When matches inside a group start, a
 *   {@link etu.simonzo.competition.observer.event.CompetitionStartEvent}
 *   for each group and another one for the tournament part;
 *   </li>
 *   <li>
 *   When all matches inside a group is completed, a
 *   {@link etu.simonzo.competition.observer.event.CompetitionEndEvent}
 *   for each group and another one for the tournament part;
 *   </li>
 *   <li>
 *   When all qualified competitors that will advance to the tournament
 *   part are selected,
 *   {@link etu.simonzo.competition.observer.event.QualifiedCompetitorsSelectedEvent}
 *   </li>
 *   <li>
 *   When a tournament phase (..., 1/8 final, quarter final, semi final,
 *   final) starts, a
 *   {@link etu.simonzo.competition.observer.event.TournamentPhaseStartedEvent}
 *   for each phase.
 *   </li>
 * </ul>
 * @param <T> Sub-type of Competitor
 */
public class Master<T extends Competitor> extends Competition<T> {

    /**
     * Create a master defined by an instance of match, a list of competitors,
     * a grouping strategy, a filtering strategy and a sorting strategy. These
     * strategies will be used to select competitors which are allowed to
     * compete in the tournament of the second phase.
     * @param match Match object used to determine the outcome of matches
     * @param competitors List of competitors enlisted to play in the master
     * @param id an string identifier for this competition. Should be unique, otherwise,
     * undefined behavior may occurs
     * @param gstrat Strategy which determines how competitors are split into
     * groups for the first phase of the master
     * @param fstrat Strategy which determines which competitors are qualified
     * to enter the tournament phase following the group phase
     * @param sstrat Strategy which determines the order in which qualified
     * competitors are enlisted in the tournament phase
     * @param victoryPoints Number of points awarded for a victory
     * @param defeatPoints Number of points awarded for a defeat
     * @param tiePoints Number of points awarded to each competitor in case of a
     * tie
     */
    public Master(Match<T> match, List<T> competitors, String id,
                  GroupingStrategy gstrat, FilteringStrategy fstrat, SortingStrategy sstrat,
                  int victoryPoints, int defeatPoints, int tiePoints) {
        super(match, competitors,
              // this ranking handler is unused, because points are handled by
              // the sub-phases of the master
              new SimpleRankingHandler<T>(competitors, victoryPoints, defeatPoints, tiePoints),
              id);
        this.gstrat = gstrat;
        this.fstrat = fstrat;
        this.sstrat = sstrat;
        this.victoryPoints = victoryPoints;
        this.defeatPoints = defeatPoints;
        this.tiePoints = tiePoints;
    }

    /**
     * Play all the matches in the master, using the strategy objects passed at
     * construction to select competitors between the two phases.
     * @param competitors List of competitors enlisted to play
     * @throws IllegalStateException if the strategies used are incompatible. In
     * this case, the instance of master is in an undefined state.
     */
    protected void play(List<T> competitors) {
        try {
            Collection<List<T>> groups = this.prepareFirstPhase(competitors);
            fireGroupsFormed(groups);
            Collection<Map<T, Integer>> rankings = this.playFirstPhase(groups);
            List<T> qualified = this.prepareSecondPhase(rankings);
            fireQualifiedCompetitorsSelectedEvent(qualified, rankings);
            this.playSecondPhase(qualified);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Possibly incompatible strategies");
        }
    }

    /**
     * <strong>Unsupported operation.</strong> This method should not be called.
     * @param competitor1 Unused first competitor
     * @param competitor2 Unused second competitor
     * @throws UnsupportedOperationException iff the method is called
     */
    protected void playMatch(T competitor1, T competitor2) {
        throw new UnsupportedOperationException("Master#playMatch is unsupported");
    }

    /**
     * Return the ranking of the tournament phase of the master.
     * <strong>This method should only be called after a call to
     * {@link Competition#play()}, else the behaviour is undefined</strong>
     * @return Map associating competitors to scores
     */
    public Map<T, Integer> ranking() {
        return this.tournament.ranking();
    }

    /**
     * Return the collection of the rankings of the group stage of the
     * master.
     * <strong>This method should only be called after a call to
     * {@link Competition#play()}, else the behaviour is undefined</strong>
     * @return Collection of ranking maps, each associating competitors to
     * their score in their particular group
     */
    public Collection<Map<T, Integer>> groupPhaseRankings() {
        Collection<Map<T, Integer>> rankings = new ArrayList<>();
        for (League<T> league : this.leagues) {
            rankings.add(league.ranking());
        }
        return rankings;
    }

    /**
     * Split competitors into groups.
     * @param competitors Initial list of competitors
     * @return Groups of competitors for the group phase
     */
    private Collection<List<T>> prepareFirstPhase(List<T> competitors) {
        return this.gstrat.group(competitors);
    }

    /**
     * Instantiate a league for each group and play all matches in each
     * league. Return a collection of rankings, one for each league. Set the
     * <code>leagues</code> private field as a side-effect.
     * @param groups Groups of competitors, each group represents the
     * competitors of a league
     * @return Collection of maps associating competitors to their score in
     * their own group
     */
    private Collection<Map<T, Integer>> playFirstPhase(Collection<List<T>> groups) {
        this.leagues = new ArrayList<>();
        // Create leagues
        for (List<T> group : groups) {
            League<T> league = new League<>(
                this.getMatch(), group, generateGroupId(), this.victoryPoints, this.defeatPoints, this.tiePoints);
            this.leagues.add(league);
        }
        subscribeListenersToLeagues();
        // Play all matches in each league
        for (League<T> league : this.leagues) {
            league.play();
        }
        // Collect rankings
        Collection<Map<T, Integer>> rankings = new ArrayList<>();
        for (League<T> league : this.leagues) {
            rankings.add(league.ranking());
        }
        return rankings;
    }

    private int groupCounter = 0;
    /**
     * Generate an unique identifier for a league for each call
     * @return a unique identifier for a league
     */
    private String generateGroupId() {
        groupCounter++;
        return String.format("%s-> Group %d", this.identifier, groupCounter);
    }

    /**
     * Make each listeners of this master subscribe to each created league.
     * The collection of league is assumed to be in the attribute
     * <code>leagues</code>.
     */
    private void subscribeListenersToLeagues() {
        for (League<T> league: this.leagues) {
            for (CompetitionListener<T> listener: getListeners()) {
                league.addCompetitionListener(listener);
            }
        }
    }

    /**
     * Select the qualified competitors after the group phase and order them for
     * the second phase.
     * @param rankings Collection of maps associating competitors to their score
     * in their own group
     * @return List of qualified competitors
     */
    private List<T> prepareSecondPhase(Collection<Map<T, Integer>> rankings) {
        Collection<T> qualifiedCompetitors = this.fstrat.filter(rankings);
        return this.sstrat.sort(qualifiedCompetitors, rankings);
    }

    /** Instantiate a tournament for the qualified competitors and play it. Set
     * the <code>tournament</code> private field as a side effect.
     * @param qualifiedCompetitors List of competitors who are playing in the
     * tournament phase
     */
    private void playSecondPhase(List<T> qualifiedCompetitors) {
        String id = String.format("%s-> Tournament", this.identifier);
        this.tournament = new Tournament<T>(
            this.getMatch(), qualifiedCompetitors, id,
            this.victoryPoints, this.defeatPoints, this.tiePoints);
        subscribeListenersToTournament();
        this.tournament.play();
    }

    /**
     * Make each listeners of this master subscribe to the created tournament.
     * The collection of league is assumed to be in the attribute
     * <code>tournament</code>.
     */
    private void subscribeListenersToTournament() {
        for (CompetitionListener<T> listener: getListeners()) {
            tournament.addCompetitionListener(listener);
        }
    }

    /** Collection of league competitions corresponding to the first phase */
    private Collection<League<T>> leagues;

    /** Tournament corresponding to the second phase */
    private Tournament<T> tournament;

    /** Strategy to split competitors into groups */
    private GroupingStrategy gstrat;

    /** Strategy to select qualified players based on group rankings */
    private FilteringStrategy fstrat;

    /** Strategy to sort a list of qualified players */
    private SortingStrategy sstrat;

    /** Number of points awarded for a victory */
    private int victoryPoints;

    /** Number of points awarded for a defeat */
    private int defeatPoints;

    /** Number of points awarded to each competitor in case of a tie */
    private int tiePoints;

}
