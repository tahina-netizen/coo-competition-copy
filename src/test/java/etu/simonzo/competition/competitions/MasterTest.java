package etu.simonzo.competition.competitions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.MatchMock;
import etu.simonzo.competition.match.MatchOutcome;
import etu.simonzo.competition.ranking.RankingHandler;
import etu.simonzo.competition.ranking.Result;
import etu.simonzo.competition.ranking.SimpleRankingHandler;
import etu.simonzo.competition.strategies.filter.FilteringStrategy;
import etu.simonzo.competition.strategies.filter.TakeNFirstFilteringStrategy;
import etu.simonzo.competition.strategies.group.GroupingStrategy;
import etu.simonzo.competition.strategies.group.MakeNGroupsStrategy;
import etu.simonzo.competition.strategies.sort.ArbitrarySortingStrategy;
import etu.simonzo.competition.strategies.sort.SortingStrategy;
import etu.simonzo.competition.util.MapUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MasterTest extends CompetitionTest {

    private static final int TIE_POINTS = 3;
    private static final int DEFEAT_POINTS = 0;
    private static final int VICTORY_POINTS = 1;
    private MatchMock<Competitor> match;
    private List<Competitor> competitors;
    private GroupingStrategy gstrat;
    private FilteringStrategy fstrat;
    private SortingStrategy sstrat;

    private int gstratN;
    private int fstratN;
    private int groupSize;
    private int firstPhaseNbMatches;
    private int secondPhaseNbMatches;

    /* Implement factory method */
    @Override
    protected Competition<Competitor> createCompetition() {
        return createMaster();
    }

    @BeforeEach
    void setUp() throws Exception {
        this.match = new MatchMock<>(MatchOutcome.FIRST_PLAYER_WIN);

        this.competitors = new ArrayList<>();
        this.competitors.add(new Competitor("A"));
        this.competitors.add(new Competitor("B"));
        this.competitors.add(new Competitor("C"));
        this.competitors.add(new Competitor("D"));
        this.competitors.add(new Competitor("E"));
        this.competitors.add(new Competitor("F"));

        this.gstrat = new MakeNGroupsStrategy(2);
        this.fstrat = new TakeNFirstFilteringStrategy(2);
        this.sstrat = new ArbitrarySortingStrategy();

        /*utility variables*/
        this.gstratN = ((MakeNGroupsStrategy) this.gstrat).getN();
        this.fstratN = ((TakeNFirstFilteringStrategy) fstrat).getN();
        this.groupSize = this.competitors.size() / this.gstratN;
        this.firstPhaseNbMatches = 2 * this.groupSize * (this.groupSize - 1);
        this.secondPhaseNbMatches = fstratN * gstratN - 1;
    }

    /* Check that play throws when the combination of strategies used is
     * invalid */
    @Test
    public void playWithBadCombinationOfStrategyShouldThrow() {
        Master<Competitor> master = new Master<>(
            match, competitors, "Master with bad combination of strategy",
            new MakeNGroupsStrategy(3),
            new TakeNFirstFilteringStrategy(2),
            new ArbitrarySortingStrategy(),
            VICTORY_POINTS, DEFEAT_POINTS, TIE_POINTS);

        assertThrows(IllegalStateException.class, () -> master.play());
    }

    /* Check that the Match.playWith method has been called the expected number
     * of times, ie the sum of the expected number of matches in the group phase
     * and the expected number of matches in the tournament phase  */
    @Test
    public void playCallsPlayWithCorrectNumberOfTimes() {
        assertEquals(0, this.match.getNbCalls());
        Master<Competitor> master = createMaster();
        int expectedNbMatches = firstPhaseNbMatches + secondPhaseNbMatches;
        master.play(this.competitors);
        assertEquals(expectedNbMatches, this.match.getNbCalls());
    }

    /* Check that the correct matches were played during the group phase of the
     * master */
    @Test
    public void playProducesCorrectMatchUpsInFirstPhase() {
        Master<Competitor> master = createMaster();
        master.play(this.competitors);
        List<Result<Competitor>> results = this.match.getResults();
        assertTrue(containsMatchup(results, competitors.get(0), competitors.get(1)));
        assertTrue(containsMatchup(results, competitors.get(1), competitors.get(0)));
        assertTrue(containsMatchup(results, competitors.get(0), competitors.get(2)));
        assertTrue(containsMatchup(results, competitors.get(2), competitors.get(0)));
        assertTrue(containsMatchup(results, competitors.get(1), competitors.get(2)));
        assertTrue(containsMatchup(results, competitors.get(2), competitors.get(1)));
        assertTrue(containsMatchup(results, competitors.get(3), competitors.get(4)));
        assertTrue(containsMatchup(results, competitors.get(4), competitors.get(3)));
        assertTrue(containsMatchup(results, competitors.get(3), competitors.get(5)));
        assertTrue(containsMatchup(results, competitors.get(5), competitors.get(3)));
        assertTrue(containsMatchup(results, competitors.get(4), competitors.get(5)));
        assertTrue(containsMatchup(results, competitors.get(5), competitors.get(4)));
    }

    /* Check that the winners of the tournament phase were part of the qualified
     * subset of competitors. Multiple winners allowed in case of an ex aequo */
    @Test
    public void winnerIsAmongTheBestAfterFirstPhase() {
        Master<Competitor> master = createMaster();
        master.play(this.competitors);

        Collection<Map<Competitor, Integer>> groupPhaseRankings =
            master.groupPhaseRankings();
        Collection<Competitor> qualified = fstrat.filter(groupPhaseRankings);
        List<Competitor> winners = extractWinnersExAequo(master.ranking());

        for(Competitor winner: winners) {
            assertTrue(qualified.contains(winner));
        }
    }

    /* Check that each competitor in the list used to initialize a master has
     * a score in one of the ranking maps of the group phase */
    @Test
    public void groupPhaseRankingContainsEveryAndOnlyCompetitors() {
        Master<Competitor> master = createMaster();
        master.play(this.competitors);

        Collection<Map<Competitor, Integer>> groupPhaseRankings =
            master.groupPhaseRankings();
        List<Competitor> extractedCompetitors =
            extractCompetitorsFromGroups(groupPhaseRankings);
        assertEquals(competitors.size(), extractedCompetitors.size());

        for (Competitor c: extractedCompetitors) {
            assertTrue(competitors.contains(c));
        }
    }

    /* Check that each score in each ranking map in the collection returned by
     * groupPhaseRankings is correct, ie that the group phase rankings are
     * correct */
    @Test
    public void groupPhaseRankingsReturnExpectedScores() {
        Master<Competitor> master = createMaster();
        master.play(this.competitors);

        Collection<Map<Competitor, Integer>> groupPhaseRankings =
            master.groupPhaseRankings();
        for (Map<Competitor, Integer> groupScore: groupPhaseRankings) {
            for (Integer score: groupScore.values()) {
                int nbMatchup = this.groupSize - 1;
                assertEquals(nbMatchup * VICTORY_POINTS +
                             nbMatchup * DEFEAT_POINTS, score);
            }
        }
    }

    /* Check that all competitors who are present in the final ranking of the
     * master (ie are associated to a score in the final ranking map) were
     * qualified competitors at the end of the group phase */
    @Test
    public void finalRankingShouldContainsExpectedCompetitors() {
        Master<Competitor> master = createMaster();
        master.play(this.competitors);
        Collection<Competitor> qualified = master.ranking().keySet();

        Collection<Map<Competitor, Integer>> groupPhaseRankings =
            master.groupPhaseRankings();
        Collection<Competitor> expectedQualified =
            fstrat.filter(groupPhaseRankings);

        for (Competitor c: qualified) {
            assertTrue(expectedQualified.contains(c));
        }
    }

    /* Check that the number of competitors who are associated to a score in the
     * final ranking is equal to the number of qualified competitors at the end
     * of the group phase */
    @Test
    public void finalRankingShouldContainsRightNbOfCompetitors() {
        Master<Competitor> master = createMaster();
        master.play(this.competitors);
        Collection<Competitor> qualified = master.ranking().keySet();

        Collection<Map<Competitor, Integer>> groupPhaseRankings =
            master.groupPhaseRankings();
        Collection<Competitor> expectedQualified =
            fstrat.filter(groupPhaseRankings);

        assertSame(expectedQualified.size(), qualified.size());
    }

    /* Check that the final ranking map contains the expected score */
    @Test
    public void finalRankingShouldContainsExpectedScores() {
        Master<Competitor> master = createMaster();
        master.play(this.competitors);

        // extract results of the first phase
        List<Result<Competitor>> results = this.match.getResults();
        List<Result<Competitor>> secondPhaseResults = results.subList(
            firstPhaseNbMatches, results.size());

        Map<Competitor, Integer> ranking = master.ranking();

        // using a ranking handler to compute the expected result
        List<Competitor> qualified = new LinkedList<>();
        for (Competitor c: ranking.keySet()) {
            qualified.add(c);
        }
        RankingHandler<Competitor> rh = new SimpleRankingHandler<>(
            qualified, VICTORY_POINTS, DEFEAT_POINTS, TIE_POINTS);
        for(Result<Competitor> r: secondPhaseResults) {
            rh.addResult(r);
        }
        Map<Competitor, Integer> expectedRanking = rh.getRanking();

        assertEquals(expectedRanking, ranking);
    }

    /* Ancillary methods */

    protected Master<Competitor> createMaster() {
        String id = "Master for test";
        return new Master<>(match, competitors, id, gstrat, fstrat, sstrat,
                            VICTORY_POINTS, DEFEAT_POINTS, TIE_POINTS);
    }

    /**
     * @param results list of result
     * @param ca
     * @param cb
     * @return true iff <code>results</code> contains a matchup between
     * <code>ca</code> and <code>cb</code> (<code>ca</code> vs <code>cb</code>
     * exactly in that order)
     */
    private static <T extends Competitor> boolean containsMatchup(
            List<Result<T>> results, Competitor ca, Competitor cb) {
        for (Result<T> result: results) {
            Competitor resC1 = result.getCompetitor1();
            Competitor resC2 = result.getCompetitor2();
            if ((resC1 == ca && resC2 == cb) || (resC1 == cb && resC2 == ca)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param <T>
     * @param scores
     * @return all competitor with the maximum score
     */
    private static <T extends Competitor>
    List<T> extractWinnersExAequo(Map<T, Integer> scores) {
        List<T> res = new LinkedList<>();
        Map<T, Integer> sorted = MapUtil.sortByDescendingValue(scores);
        int maxScore = -1;
        for (T c: sorted.keySet()) {
            maxScore = sorted.get(c);
        }
        for (T c: sorted.keySet()) {
            if (sorted.get(c) == maxScore) {
                res.add(c);
            }
        }
        return res;
    }

    private static <T extends Competitor> List<T>
    extractCompetitorsFromGroups(Collection<Map<T, Integer>> rankings) {
        List<T> res = new LinkedList<>();
        for (Map<T, Integer> ranking: rankings) {
            for (T c: ranking.keySet()) {
                res.add(c);
            }
        }
        return res;
    }
}
