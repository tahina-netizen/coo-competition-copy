package etu.simonzo.competition.observer.listener;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitions.CompetitionMock;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.displayers.StdoutDisplayer;
import etu.simonzo.competition.match.MatchOutcome;

class BookmakerTest {

    private CompetitionMock<Competitor> competition;
    private Bookmaker<Competitor> bookmaker;
    private List<Competitor> competitors;

    @BeforeEach
    public void init() {
        competition = new CompetitionMock<>();
        bookmaker =  createBookmaker();
        competition.addCompetitionListener(bookmaker);
        competitors = new LinkedList<>();
        competitors.add(new Competitor("A"));
        competitors.add(new Competitor("B"));
        competitors.add(new Competitor("C"));
    }

    protected Bookmaker<Competitor> createBookmaker() {
        return new Bookmaker<Competitor>(StdoutDisplayer.getInstance());
    }

    /**
     * A winner must have his odd decremented by 1
     */
    @Test
    public void oddOfWinnerShouldDecrement() {
        Competitor c1 = competitors.get(0);
        Competitor c2 = competitors.get(1);

        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);

        assertEquals(Bookmaker.START_ODD - 1, bookmaker.getCompetitorOdd(c1).get(), 0.001f);
    }

    /**
     * A looser must have his odd decremented by 1
     */
    @Test
    public void oddOfLooserShouldIncrement() {
        Competitor c1 = competitors.get(0);
        Competitor c2 = competitors.get(1);

        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);

        assertEquals(Bookmaker.START_ODD + 1, bookmaker.getCompetitorOdd(c2).get(), 0.001f);
    }

    /**
     * When there is a tie, the odds of the two competitors does not change
     */
    @Test
    public void oddsDoesNotChangeWhenTie() {
        Competitor c1 = competitors.get(0);
        Competitor c2 = competitors.get(1);
        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);
        float beforeScore1 = bookmaker.getCompetitorOdd(c1).get();
        float beforeScore2 = bookmaker.getCompetitorOdd(c2).get();

        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.TIE);

        assertEquals(beforeScore1, bookmaker.getCompetitorOdd(c1).get(), 0.001f);
        assertEquals(beforeScore2, bookmaker.getCompetitorOdd(c2).get(), 0.001f);
    }

    /**
     * Even after an odd raise, that odd cannot be more that MAX_ODD: 5
     */
    @Test
    public void oddCannotBeMoreThanTheMaximum() {
        Competitor c1 = competitors.get(0);
        Competitor c2 = competitors.get(1);

        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);
        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);
        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);
        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);

        assertEquals(Bookmaker.MAX_ODD, bookmaker.getCompetitorOdd(c2).get(), 0.001f);
    }

    /**
     * Even after a decrease, an odd cannot be less than MIN_ODD: 1
     */
    @Test
    public void oddCannotBeLessThanTheMinimum() {
        Competitor c1 = competitors.get(0);
        Competitor c2 = competitors.get(1);

        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);
        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);
        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.FIRST_PLAYER_WIN);

        assertEquals(Bookmaker.MIN_ODD, bookmaker.getCompetitorOdd(c1).get(), 0.001f);
    }

    /**
     * If the bookmaker didn't assist to a match of a given competitor,
     * the odd associated to it should be empty
     */
    @Test
    public void oddOfAnUnknownCompetitor() {
        Competitor c1 = competitors.get(0);
        Competitor c2 = competitors.get(1);

        // no match played

        assertTrue(bookmaker.getCompetitorOdd(c1).isEmpty());
        assertTrue(bookmaker.getCompetitorOdd(c2).isEmpty());
    }


    /**
     * Each competitors should have the initial odd at the beginning: 2
     */
    @Test
    public void initialOddShouldBeRight() {
        Competitor c1 = competitors.get(0);
        Competitor c2 = competitors.get(1);

        competition.forceFireMatchPlayed(c1, c2, MatchOutcome.TIE);

        assertEquals(Bookmaker.START_ODD, bookmaker.getCompetitorOdd(c1).get(), 0.001f);
        assertEquals(Bookmaker.START_ODD, bookmaker.getCompetitorOdd(c2).get(), 0.001f);
    }

}
