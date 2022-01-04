package etu.simonzo.competition.match;

import java.util.*;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.ranking.Result;
import etu.simonzo.competition.ranking.SimpleResult;

/**
 * A simple class implementing Match, which can be used as a mock for testing.
 * This class provides a method to set the returned value of subsequent calls to
 * the playWith method. It also keeps track of the number of calls made to
 * playWith and allows the user to retrieve the set of match results at any
 * point
 * @see etu.simonzo.competition.ranking.Result
 */
public class MatchMock<T extends Competitor> implements Match<T> {
    private int nbCalls;

    private MatchOutcome currentOutcome;

    private List<Result<T>> results;

    public MatchMock(MatchOutcome initialOutcome) {
        this.nbCalls = 0;
        this.currentOutcome = initialOutcome;
        this.results = new ArrayList<>();
    }

    public int getNbCalls() {
        return this.nbCalls;
    }

    public void setOutcome(MatchOutcome outcome) {
        this.currentOutcome = outcome;
    }

    public List<Result<T>> getResults() {
        return this.results;
    }

    public MatchOutcome playWith(T c1, T c2) {
        this.nbCalls++;
        this.results.add(new SimpleResult<T>(c1, c2, this.currentOutcome));
        return this.currentOutcome;
    }
}
