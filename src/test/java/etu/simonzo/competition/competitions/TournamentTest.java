package etu.simonzo.competition.competitions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.match.MatchMock;
import etu.simonzo.competition.match.MatchOutcome;
import etu.simonzo.competition.ranking.Result;

import java.util.*;

public class TournamentTest extends CompetitionTest {

    protected MatchMock<Competitor> match;

    protected List<Competitor> competitors;

    protected Tournament<Competitor> tournament;

    /* Implement factory method */
    protected Competition<Competitor> createCompetition() {
        Match<Competitor> m = new MatchMock<>(MatchOutcome.FIRST_PLAYER_WIN);
        List<Competitor> c = new ArrayList<>();
        c.add(new Competitor("Alpha"));
        c.add(new Competitor("Bravo"));
        return new Tournament<>(m, c, "Tournament for test", 3, 0, 1);
    }

    @BeforeEach
    public void init() {
        this.match = new MatchMock<>(MatchOutcome.FIRST_PLAYER_WIN);
        this.competitors = new ArrayList<>();
        this.competitors.add(new Competitor("Alpha"));
        this.competitors.add(new Competitor("Bravo"));
        this.competitors.add(new Competitor("Charlie"));
        this.competitors.add(new Competitor("Delta"));
        String id = "Another tournament for test";
        this.tournament = new Tournament<>(this.match, this.competitors, id, 3, 0, 1);
    }

    /* Check that the constructor throws IllegalArgumentException when the
     * number of competitors is not a power of 2 */
    @Test
    public void constructorThrowsIfListSizeNotPowerOfTwo() {
        this.competitors.add(new Competitor("Echo"));
        assertThrows(IllegalArgumentException.class, () -> {
                String id = "Tournament with bad number of competitors";
                new Tournament<Competitor>(this.match, this.competitors, id, 3, 0, 1);
            });
    }

    /* Check that the constructor does not throw when the number of competitors
     * is a power of 2 */
    @Test
    public void constructorDoesNotThrowIfListSizePowerOfTwo() {
        String id = "Tournament with good number of competitors";
        this.tournament = new Tournament<>(this.match, this.competitors, id, 3, 0, 1);
    }

    /* Check that the playMatch method has been called the expected number of
     * times, ie (number of competitors - 1) */
    @Test
    public void playCallsPlayWithCorrectNumberOfTimes() {
        assertEquals(0, this.match.getNbCalls());
        this.tournament.play(this.competitors);
        assertEquals(this.competitors.size() - 1, this.match.getNbCalls());
    }

    /* Check that each matchup is played exactly once, and that matches are
     * played in the right order (first against second, third against fourth
     * etc... then recursively the same over qualified competitors) */
    @Test
    public void playProducesCorrectMatchUps() {
        this.tournament.play(this.competitors);
        List<Result<Competitor>> results = this.match.getResults();
        assertSame(this.competitors.get(0), results.get(0).getCompetitor1());
        assertSame(this.competitors.get(1), results.get(0).getCompetitor2());
        assertSame(this.competitors.get(2), results.get(1).getCompetitor1());
        assertSame(this.competitors.get(3), results.get(1).getCompetitor2());
        assertSame(this.competitors.get(0), results.get(2).getCompetitor1());
        assertSame(this.competitors.get(2), results.get(2).getCompetitor2());
    }

    /* Check that the ranking (ie the map of scores) contains the correct
     * results after a call to play */
    @Test
    public void rankingProducesCorrectScores() {
        this.tournament.play(this.competitors);
        assertEquals(6, this.tournament.ranking().get(this.competitors.get(0)));
        assertEquals(0, this.tournament.ranking().get(this.competitors.get(1)));
        assertEquals(3, this.tournament.ranking().get(this.competitors.get(2)));
        assertEquals(0, this.tournament.ranking().get(this.competitors.get(3)));
    }

}
