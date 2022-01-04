package etu.simonzo.competition.match;

import java.util.Optional;

import etu.simonzo.competition.competitors.Competitor;

/**
 * An interface which defines a method which should designate the winner
 * between two competitors. The interface is parameterized by a sub-type of the
 * Competitor type because it should be able to use methods of the specific
 * Competitor sub-type to determine the outcome of the match
 * @param <T> A Competitor sub-type
 */
public interface Match<T extends Competitor> {
    /**
     * Perform the match between the competitors. Return an integer indicating
     * the outcome of the match
     * @param competitor1 First competitor
     * @param competitor2 Second competitor
     * @return Instance of enumerated type MatchOutcome indicating the result of
     * the match between the competitors
     */
    public MatchOutcome playWith(T competitor1, T competitor2);

    /**
     * Perform the match between the competitors and if there is a winner, give it.
     * @param competitor1 First competitor
     * @param competitor2 Second competitor
     * @return
     * <ul>
     *  <li> if there is a winner, return an <code>Optional</code> instance
     *  containing the winner.
     *  </li>
     *  <li> if there is no winner (when the match is tie), return an empty
     *      <code>Optional</code> instance
     *  </li>
     * </ul>
     * This is just an <strong>utility method</strong> aimed to make the use of
     * {@link Match#playWith(Competitor, Competitor)} easier by avoiding repeated
     * "if" statement when trying to determine the winner
     */
    default Optional<T> playWithAndGetWinner(T competitor1, T competitor2) {
        switch (playWith(competitor1, competitor2)) {
            case FIRST_PLAYER_WIN:
                return Optional.of(competitor1);
            case SECOND_PLAYER_WIN:
                return Optional.of(competitor2);
            default:
                return Optional.empty();
        }
    }
}
