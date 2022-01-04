package etu.simonzo.competition.match;

import java.util.Random;

import etu.simonzo.competition.competitors.Competitor;

/**
 * Represents a match "rule" where match are played just randomly.
 * That is to say that the each confronted competitors has equal
 * probability to win. So the issue of the match can't be predicted.
 * @param <T> Sub-type of Competitor
 */
public class RandomMatch<T extends Competitor> implements Match<T> {
    /**
     * used to generate random value for deciding the issue of a match
     */
    private Random randomGenerator;
    /**
     * Create a match "rule" where match are played just randomly.
     */
    public RandomMatch() {
        this.randomGenerator = new Random();
    }

    /**
     * Play a match between <code>c1</code> and <code>c2</code>.
     * The issue of the match is decided randomly.
     * For this match mechanism, there must be a winner at the end
     * of the match <strong>(there is no tie)</strong>.
     * @param c1 competitor that will confront <code>c2</code>
     * @param c2 competitor that will confront <code>c1</code>
     * @return <ul>
     *          <li><code>MatchOutcome.FIRST_PLAYER_WIN</code> iff c1 wins</li>
     *          <li><code>MatchOutcome.SECOND_PLAYER_WIN</code> iff c2 wins</li>
     *         </ul>
     */
    public MatchOutcome playWith(T c1, T c2) {
        return this.randomGenerator.nextBoolean() ?
            MatchOutcome.FIRST_PLAYER_WIN :
            MatchOutcome.SECOND_PLAYER_WIN;
    }
}
