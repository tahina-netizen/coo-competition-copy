package etu.simonzo.competition.competitions;

import java.util.List;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.match.MatchOutcome;
import etu.simonzo.competition.ranking.SimpleRankingHandler;
import etu.simonzo.competition.ranking.SimpleResult;

/**
 * A competition in which every competitor faces each opponent twice. At the end
 * of the competition, the competitor with the most victories wins. The class
 * is parameterized by a sub-type of the Competitor class, because the Match
 * singleton used depends on the type of Competitor
 * @param <T> A Competitor sub-type
 */
public class League<T extends Competitor> extends Competition<T> {
    /**
     * Construct a league defined by a match singleton and a list of
     * competitors. The match object is used the determine the outcome of every
     * game. The number of points associated to match outcomes are used to
     * construct the ranking after the competition is played
     * @param match Match singleton which determines games outcomes
     * @param competitors List of competitors
     * @param id an string identifier for this competition. Should be unique, otherwise,
     * undefined behavior may occurs
     * @param victoryPoints Number of points awarded for a victory
     * @param defeatPoints Number of points awarded for a defeat
     * @param tiePoints Number of points awarded to each competitor in case of a
     * tie
     */
    public League(Match<T> match,
                  List<T> competitors,
                  String id,
                  int victoryPoints, int defeatPoints, int tiePoints) {
        super(match,
              competitors,
              new SimpleRankingHandler<T>(competitors, victoryPoints, defeatPoints, tiePoints),
              id);
    }

    /**
     * Play the matches between the competitors. Matches are organized in a
     * round trip fashion, where each competitor meets with every other
     * competitor twice. If the list has <code>n</code> elements, then
     * <code>n * (n - 1)</code> matches are played. The competitors are paired
     * using the following rule : given 3 competitors A, B, C and the list
     * [A, B, C], then the played matches are (A, B), (A, C), (B, A), (B, C),
     * (C, A), (C, B)
     * @param competitors List of competitors enlisted to play
     */
    protected void play(List<T> competitors) {
        int i = 0;
        for (T c1 : competitors) {
            int j = 0;
            for (T c2 : competitors) {
                if (i != j) {
                    this.playMatch(c1, c2);
                }
                j++;
            }
            i++;
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
     * Update the internal structure used to keep track of points. The result
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
}
