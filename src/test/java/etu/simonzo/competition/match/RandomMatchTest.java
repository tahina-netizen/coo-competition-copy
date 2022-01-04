package etu.simonzo.competition.match;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;

public class RandomMatchTest {

    /* Check that RandomMatch.playWith returns either FIRST_PLAYER_WIN or
     * SECOND_PLAYER_WIN */
    @Test
    void testPlayWith() {
        Match<Competitor> rm = new RandomMatch<>();
        Competitor c1 = new Competitor("Alpha");
        Competitor c2 = new Competitor("Bravo");
        MatchOutcome ret = rm.playWith(c1, c2);
        assertTrue(ret == MatchOutcome.FIRST_PLAYER_WIN ||
                   ret == MatchOutcome.SECOND_PLAYER_WIN);
    }

}
