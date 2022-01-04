package etu.simonzo.competition;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.MatchOutcome;
import etu.simonzo.competition.ranking.RankingHandler;
import etu.simonzo.competition.ranking.Result;
import etu.simonzo.competition.ranking.SimpleRankingHandler;
import etu.simonzo.competition.ranking.SimpleResult;

import java.util.*;

public class SimpleRankingHandlerTest {

    protected Competitor c1, c2, c3;

    @BeforeEach
    public void setUp() {
        this.c1 = new Competitor("Alice");
        this.c2 = new Competitor("Bob");
        this.c3 = new Competitor("Charlie");
    }

    protected RankingHandler<Competitor> createRankingHandler() {
        return createSimpleRankingHandler();
    }

    protected SimpleRankingHandler<Competitor> createSimpleRankingHandler() {
        List<Competitor> competitors = new ArrayList<>();
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        return new SimpleRankingHandler<Competitor>(competitors, 3, 0, 1);
    }

    /* Check that the constructor does not throw */
    @Test
    public void constructorShouldNotThrowException() {
        createSimpleRankingHandler();
    }

    /* Check that addResult doest not throw when the result passed as argument
     * refers to competitors which are part of the list of competitors used to
     * initialize the ranking handler */
    @Test
    public void addResultWorksProperlyWhenGivenAResultWithKnownCompetitors() {
        SimpleRankingHandler<Competitor> srh = createSimpleRankingHandler();
        srh.addResult(new SimpleResult<Competitor>(c1, c2, MatchOutcome.FIRST_PLAYER_WIN));
    }

    /* Check that addResult throws when a result refers to an unknown competitor
     * (missing from the list of competitors) */
    @Test
    public void addResultThrowExceptionWhenGivenAResultWithUnknownCompetitor() {
        SimpleRankingHandler<Competitor> srh = createSimpleRankingHandler();
        Result<Competitor> res = new SimpleResult<>(
            c1, new Competitor("Incognito"), MatchOutcome.SECOND_PLAYER_WIN);
        assertThrows(IllegalArgumentException.class,
                     () -> srh.addResult(res)
            );
    }

    /* Check that the collection returned by getResults contains as many
     * elements as the number of added results, and the same results */
    @Test
    public void getResultsGivesTheRightCollectionOfResult() {
        SimpleRankingHandler<Competitor> srh = createSimpleRankingHandler();
        Result<Competitor> r1, r2, r3;
        r1 = new SimpleResult<>(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);
        r2 = new SimpleResult<>(c1, c3, MatchOutcome.SECOND_PLAYER_WIN);
        r3 = new SimpleResult<>(c2, c3, MatchOutcome.TIE);

        srh.addResult(r1);
        srh.addResult(r2);
        srh.addResult(r3);

        Collection<Result<Competitor>> results = srh.getResults();

        assertEquals(3, results.size());
        assertTrue(results.contains(r1));
        assertTrue(results.contains(r2));
        assertTrue(results.contains(r3));
    }

    /* Check that the collection returned by getResults is empty when no results
     * were added */
    @Test
    public void getResultsReturnsEmptyCollectionWhenNoAddedResults() {
        SimpleRankingHandler<Competitor> srh = createSimpleRankingHandler();
        Collection<Result<Competitor>> results = srh.getResults();
        assertTrue(results.isEmpty());
    }

    /* Check that the map returned by getRanking contains correct associations
     * between competitors and scores */
    @Test
    public void getRankingReturnsMapWithRightContent() {
        SimpleRankingHandler<Competitor> srh = createSimpleRankingHandler();
        srh.addResult(new SimpleResult<>(this.c1, this.c2, MatchOutcome.FIRST_PLAYER_WIN));
        srh.addResult(new SimpleResult<>(this.c1, this.c3, MatchOutcome.SECOND_PLAYER_WIN));
        srh.addResult(new SimpleResult<>(this.c2, this.c3, MatchOutcome.TIE));

        Map<Competitor, Integer> ranking = srh.getRanking();

        assertTrue(ranking.containsKey(this.c1));
        assertTrue(ranking.containsKey(this.c2));
        assertTrue(ranking.containsKey(this.c3));
        assertEquals(3, ranking.get(this.c1));
        assertEquals(1, ranking.get(this.c2));
        assertEquals(4, ranking.get(this.c3));
    }

    /* Check that the map returned by getRanking contains associations where
     * every competitor has 0 points, if no results were added yet */
    @Test
    public void whenNoResultsYetGetRankingShouldBeAMapWhereEachCompetitorsHas0Point() {
        SimpleRankingHandler<Competitor> srh = createSimpleRankingHandler();
        Map<Competitor, Integer> ranking = srh.getRanking();
        assertEquals(3, ranking.size());
        for (Competitor c: ranking.keySet()) {
            assertEquals(0, ranking.get(c));
        }
    }
}
