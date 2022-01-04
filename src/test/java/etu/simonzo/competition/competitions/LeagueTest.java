package etu.simonzo.competition.competitions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.MatchMock;
import etu.simonzo.competition.match.MatchOutcome;
import etu.simonzo.competition.ranking.Result;

class LeagueTest extends CompetitionTest {

    private static final int TIE_POINTS = 1;
    private static final int DEFEAT_POINTS = -1;
    private static final int VICTORY_POINTS = 3;
    protected MatchMock<Competitor> matchMockDemo;
    private List<Competitor> competitorsDemo;

    @BeforeEach
    void setUp() throws Exception {
        matchMockDemo = new MatchMock<>(MatchOutcome.FIRST_PLAYER_WIN);
        competitorsDemo = new ArrayList<Competitor>();
        competitorsDemo.add(new Competitor("Alice"));
        competitorsDemo.add(new Competitor("Bob"));
        competitorsDemo.add(new Competitor("Charlie"));
    }

    /* Implement factory method */
    public Competition<Competitor> createCompetition() {
        return createLeague();
    }

    /* Check that the playMatch method has been called the expected number of
     * times, ie (number of competitors) * (number of competitors - 1) */
    @Test
    public void playMatchShouldHaveBeenCalledNTimes() /* With N = n * (n-1) */ {
        League<Competitor> leagueDemo = createLeague();

        leagueDemo.play();

        int n = competitorsDemo.size();
        assertEquals(n * (n - 1), matchMockDemo.getNbCalls());
    }

    /* Check that the ranking (ie the map of scores) contains the correct
     * results after a call to play */
    @Test
    public void rankingShouldReturnWhatIsExpectedAfterPlay() {
        League<Competitor> leagueDemo = createLeague();

        leagueDemo.play();

        Map<Competitor, Integer> ranking = leagueDemo.ranking();
        assertEquals(2 * DEFEAT_POINTS + 2 * VICTORY_POINTS,
                     ranking.get(this.competitorsDemo.get(0)));
        assertEquals(2 * DEFEAT_POINTS + 2 * VICTORY_POINTS,
                     ranking.get(this.competitorsDemo.get(1)));
        assertEquals(2 * DEFEAT_POINTS + 2 * VICTORY_POINTS,
                     ranking.get(this.competitorsDemo.get(2)));
    }

    /* Check that each matchup is played exactly once, and that matches are
     * played in the right order (first competitor plays against everyone else,
     * then second competitor does the same, etc) */
    @Test
    public void competitorsShouldBePairedCorrectly() {
        League<Competitor> leagueDemo = createLeague();
        int n = competitorsDemo.size();
        Competitor alice = this.competitorsDemo.get(0);
        Competitor bob = this.competitorsDemo.get(1);
        Competitor charlie = this.competitorsDemo.get(2);

        leagueDemo.play(this.competitorsDemo);

        List<Result<Competitor>> results = this.matchMockDemo.getResults();
        assertEquals(n * (n - 1), results.size());

        assertEquals(alice, results.get(0).getCompetitor1());
        assertEquals(bob, results.get(0).getCompetitor2());

        assertEquals(alice, results.get(1).getCompetitor1());
        assertEquals(charlie, results.get(1).getCompetitor2());

        assertEquals(bob, results.get(2).getCompetitor1());
        assertEquals(alice, results.get(2).getCompetitor2());

        assertEquals(bob, results.get(3).getCompetitor1());
        assertEquals(charlie, results.get(3).getCompetitor2());

        assertEquals(charlie, results.get(4).getCompetitor1());
        assertEquals(alice, results.get(4).getCompetitor2());

        assertEquals(charlie, results.get(5).getCompetitor1());
        assertEquals(bob, results.get(5).getCompetitor2());
    }

    public League<Competitor> createLeague() {
        return new League<>(matchMockDemo, competitorsDemo, "League for test",
                            VICTORY_POINTS, DEFEAT_POINTS, TIE_POINTS);
    }

}
