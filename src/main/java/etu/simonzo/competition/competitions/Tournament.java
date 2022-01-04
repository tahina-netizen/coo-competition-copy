package etu.simonzo.competition.competitions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.match.MatchOutcome;
import etu.simonzo.competition.ranking.SimpleRankingHandler;
import etu.simonzo.competition.ranking.SimpleResult;

/**
 * A competition in which matches are organized in a knock-out manner. Losers
 * are eliminated and winners face each other, until only one remains. The last
 * player is the winner of the competition. The number of initial competitors
 * must be a power of two. The class is parameterized by a sub-type of the
 * Competitor class, because the Match instance used depends on the type of
 * Competitor
 * @param <T> A Competitor sub-type
 */
public class Tournament<T extends Competitor> extends Competition<T> {
    /**
     * the number of points the winner of a match gets
     */
    private int victoryPoints;

    /**
     * Construct a tournament defined by a match singleton and a list of
     * competitors. The match object is used the determine the outcome of every
     * game. The number of points associated to match outcomes are used to
     * construct the ranking after the competition is played
     * @param match Match singleton which determines games outcomes
     * @param competitors List of competitors. The length of this list must be
     * a power of two
     * @param id an string identifier for this competition. Should be unique, otherwise,
     * undefined behavior may occurs
     * @param victoryPoints Number of points awarded for a victory
     * @param defeatPoints Number of points awarded for a defeat
     * @param tiePoints Number of points awarded to each competitor in case of a
     * tie
     * @throws IllegalArgumentException iff the number of competitors is not a
     * power of two
     */
    public Tournament(Match<T> match, List<T> competitors, String id,
                      int victoryPoints, int defeatPoints, int tiePoints)
        throws IllegalArgumentException {

        super(match, competitors,
              new SimpleRankingHandler<T>(competitors, victoryPoints, defeatPoints, tiePoints), id);
        if(! isPowerOfTwo(competitors.size())) {
            throw new IllegalArgumentException("competitors'size should be a power of two");
        }
        this.victoryPoints = victoryPoints;
    }

    /**
     * Play the matches between the competitors. Matches are organized in
     * rounds, where only the winning player advances to the next round.
     * If the list has <code>n = 2^k</code>
     * elements, then <code>n - 1</code> matches are played. The competitors are
     * paired using the following rule : given 4 competitors A, B, C and D
     * and the list [A, B, C, D], then neighbours are paired against eachother.
     * Here, A competes against B, and C competes against D. The next round of
     * the tournament is then represented by the following list :
     * [winner(A, B), winner(C, D)].
     *
     * How it works ? It plays matches in the initial round (..., eighth final,
     * quarter-final, semi-final, final). The set of competitors which advance
     * to the next round is those who have won <code>z</code> times in total
     * since the beginning of the tournament, with <code> z = 1 </code> here.
     * In the next round "r", the required number of victory to advance to the
     * next round (of "r") is <code>z + 1</code>. And so on and so on ... until
     * the competition is over (when the final round has been played).
     *
     * Let's give an example:
     * Let's have the list of participant of the tournament: [A, B, C, D, E, F, G, H]
     * <ol>
     *  <li>the initial round here is the quarter-final, and played match are
     * (A, B), (C, D) (E, F) and (G, H), the number of victories required to
     * advance to the next round (which is the semi-final)  is 1
     *  </li>
     *  <li>in the quarter final, the played matches are (winner of A vs B,
     * winner of C vs D), (winner of E vs F, winner of G vs H) and the number of
     * victories required to advance to the next round (which is the final) is 2
     *  </li>
     *  <li>...</li>
     *  <li>At the end, the winner of the tournament is the competitor which has
     * won 3 times in total since the beginning of the competition
     *  </li>
     * </ol>
     *
     * @param competitors List of competitors enlisted to play. Its length must
     * be a power of two
     */
    protected void play(List<T> competitors) {
        this.play(competitors, 0, 1);
    }

    /**
     * Play the matches between competitors until a final winner is determined.
     * How it works ? It plays matches in the initial round (a round is ...,
     * eighth final, quarter-final, semi-final, final). The set of competitors
     * which advance to the next round is those who have won
     * <code>nbVictory + winRequiredInEachStep</code> in total since the
     * beginning of the tournament. Then play the matches between competitors
     * who made to the next round. And so on, and so on until the tournament is
     * over (when the final round has been played).
     *
     * Let's give an example:
     * Let's have the list of competitors: [A, B, C, D, E, F, G, H] with
     * <code>nbVictory=3, winRequiredInEachStep=1</code> for example
     * <ol>
     *  <li>the initial round here is the quarter-final, and played match are
     * (A, B), (C, D) (E, F) and (G, H), the number of victories required to
     * advance to the next round (which is the semi-final) is 4
     * (<code>nbVictory + winRequiredInEachStep</code>).
     *  </li>
     *  <li>in the semi-final, the played matches are (winner of A vs B, winner
     * of C vs D), (winner of E vs F, winner of G vs H) and the number of
     * victories required to advance to the next round (which is the final) is 5
     * (<code>nbVictory + winRequiredInEachStep + 1</code>)
     *  </li>
     *  <li> in the final, the played match is ((winner of A vs B) vs (winner of
     * C vs D)) , (winner of(winner of E vs F) vs (winner of G vs H)). This next
     * round is special because there are only 2 competitors left so there is not
     * a next round anymore and the winner of the competition is determined.
     * Let's note that the total number of victories of the final's winner is
     * then 6 (<code>nbVictory + winRequiredInEachStep + 1 + 1</code>).
     *  </li>
     * </ol>
     * This methods is used an helper for {@link Tournament#play(List)}.
     *
     * @param competitors competitors that will play against each other.
     * @param nbVictory total number of victory (since the beginning of the
     * tournament) of the competitors that are playing in the initial round
     * @param winRequiredInEachStep number of wins a competitor need to get in
     * the initial round to advance to the next round
     */
    private void play(List<T> competitors, int nbVictory, int winRequiredInEachStep) {
        int n = competitors.size();
        if (n == 1) {
            // there is nothing to do
            // the winner is the only competitor in the list
        }
        else {
            fireTournamentPhaseStartedEvent(n / 2);
            Iterator<T> competitorsIterator = competitors.iterator();
            for (int i = 0; i < n / 2; i++) {
                T c1, c2;
                c1 = competitorsIterator.next();
                c2 = competitorsIterator.next();
                this.playMatch(c1, c2);
            }

            // filtering the competitors which won in this round
            Map<T, Integer> currentRanking = this.getRankingHandler().getRanking();

            List<T> victoriousCompetitors = new LinkedList<>();
            for (T competitor: this.getCompetitors()) {
                if(
                    currentRanking.get(competitor) ==
                    (nbVictory + winRequiredInEachStep) * this.victoryPoints
                ) {
                    victoriousCompetitors.add(competitor);
                }
            }

            // play the next round
            this.play(victoriousCompetitors,
                      nbVictory + winRequiredInEachStep,
                      winRequiredInEachStep);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void playMatch(T c1, T c2) {
        MatchOutcome outcome = this.getMatch().playWith(c1, c2);
        fireMatchPlayed(c1, c2, outcome);
        this.updateRanking(c1, c2, outcome);
    }

    /**
     * Update the internal structure used to keep track of points. The outcome
     * argument represents the outcome of the match between the two competitors,
     * as returned by playMatch
     * @param competitor1 First competitor
     * @param competitor2 Second competitor
     * @param outcome Instance of enumerated type MatchOutcome indicating the
     * result of the match between the competitors
     */
    protected void updateRanking(T competitor1, T competitor2, MatchOutcome outcome) {
        this.getRankingHandler().addResult(
            new SimpleResult<T>(competitor1, competitor2, outcome));
    }

    /**
     * @param n the number to check if is a power of two
     * @return true iff <code>n</code> is a power of two
     */
    private static boolean isPowerOfTwo(int n) {
        int square = 1;
        while(n >= square){
            if(n == square){
                return true;
            }
            square = square*2;
        }
        return false;
    }
}
