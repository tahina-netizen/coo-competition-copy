package etu.simonzo.competition.match;

/**
 * Represents the final outcome of a confrontation between two
 * competitors.
 * First of all, there is the notion of "first" player and "second" player,
 * that notion is used just to distinguish the two confronting competitors.
 * So, the outcome of the match can be:
 * <ul>
 *  <li>A tie (nobody win) represented by {@link MatchOutcome#TIE}</li>
 *  <li>Not a tie, so there is a winner, represented by:
 *   {@link MatchOutcome#FIRST_PLAYER_WIN} if the "first" player won,
 *   or {@link MatchOutcome#SECOND_PLAYER_WIN} if the "second" player won.
 *   </li>
 * </ul>
 * If one competitor won, the other lost
 */
public enum MatchOutcome {
    TIE,
    FIRST_PLAYER_WIN,
    SECOND_PLAYER_WIN,
}
