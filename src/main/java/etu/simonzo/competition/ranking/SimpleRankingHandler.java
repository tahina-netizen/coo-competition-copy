package etu.simonzo.competition.ranking;

import java.util.*;

import etu.simonzo.competition.competitors.Competitor;

/**
 * A simple implementation of the RankingHandler abstraction, based on the use
 * of a collection of match results, and the possibility to convert these
 * results to a ranking of competitors using points information passed on
 * construction. The list of competitors to rank is passed on construction.
 * All the competitors of the subsequent results must be part of this list, else
 * an exception will be thrown
 */
public class SimpleRankingHandler<T extends Competitor> implements RankingHandler<T> {
    /** Number of points awarded for a victory */
    protected int victoryPoints;

    /** Number of points awarded for a defeat */
    protected int defeatPoints;

    /** Number of points awarded to each player in case of a tie */
    protected int tiePoints;

    /** List of results added since instanciation */
    protected List<Result<T>> results;

    /** Association of competitors and scores */
    protected Map<T, Integer> ranking;

    /**
     * Construct a simple ranking handler, with the points value of a victory,
     * defeat and tie. These values are used when converting the results to a
     * ranking of competitors
     * @param competitors List of competitors registered to be ranked
     * @param victoryPoints Number of points awarded for a victory
     * @param defeatPoints Number of points awarded for a defeat
     * @param tiePoints Number of points awarded for a tied match
     */
    public SimpleRankingHandler(List<T> competitors, int victoryPoints,
                                int defeatPoints, int tiePoints) {
        this.victoryPoints = victoryPoints;
        this.defeatPoints = defeatPoints;
        this.tiePoints = tiePoints;
        this.results = new ArrayList<>();
        this.ranking = mapOfCompetitorsWithoutPoints(competitors);
    }

    /**
     * Add a result to the collection of results tracked by the ranking handler
     * @param result Result object to add to the collection
     * @throws IllegalArgumentException if one of the competitors is unknown,
     * ie. was not part of the list passed on instanciation
     * {@link etu.simonzo.competition.ranking.RankingHandler#addResult}
     */
    public void addResult(Result<T> result) throws IllegalArgumentException {
        if (!this.ranking.containsKey(result.getCompetitor1()) ||
            !this.ranking.containsKey(result.getCompetitor2())) {
            throw new IllegalArgumentException("Unknown competitor in result");
        }
        this.results.add(result);
        this.updateRanking(result);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Result<T>> getResults() {
        return this.results;
    }

    /**
     * Return the ranking computed using the results added to the handler.
     * Competitors who do not appear in any results have a score of 0
     * {@link etu.simonzo.competition.ranking.RankingHandler#getRanking}
     */
    public Map<T, Integer> getRanking() {
        return this.ranking;
    }

    /**
     * Return a hashmap in which all competitors were added as keys, each
     * associated to a score of 0. The returned map should be filled with actual
     * match outcomes
     * @param competitors List of competitors registered to be ranked
     * @return Map associating each competitor to the value 0
     */
    private Map<T, Integer>
    mapOfCompetitorsWithoutPoints(List<T> competitors) {
        Map<T, Integer> ranking = new HashMap<>();
        for (T c : competitors) {
            ranking.put(c, 0);
        }
        return ranking;
    }

    /**
     * Add a number of points to the score of the competitor in the map
     * @param competitor Competitor whose score must change
     * @param points Number of points to add
     */
    private void incrementScore(T competitor, int points) {
        int initial = this.ranking.containsKey(competitor) ?
            this.ranking.get(competitor) : 0;
        this.ranking.put(competitor, initial + points);
    }

    /**
     * Modify points in the scores map using the result passed
     * @param result Result to be taken into account for the ranking
     */
    private void updateRanking(Result<T> result) {
        switch (result.getOutcome()) {
        case FIRST_PLAYER_WIN:
            this.incrementScore(result.getCompetitor1(), this.victoryPoints);
            this.incrementScore(result.getCompetitor2(), this.defeatPoints);
            break;
        case SECOND_PLAYER_WIN:
            this.incrementScore(result.getCompetitor1(), this.defeatPoints);
            this.incrementScore(result.getCompetitor2(), this.victoryPoints);
            break;
        case TIE:
            this.incrementScore(result.getCompetitor1(), this.tiePoints);
            this.incrementScore(result.getCompetitor2(), this.tiePoints);
            break;
        }
    }
}
