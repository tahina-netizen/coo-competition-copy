package etu.simonzo.competition.competitions;

import java.util.List;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.MatchOutcome;

/**
 * A mock class for Competition.
 * Used to "force" fireXXX methods i.e to "force" events to happen. 
 *
 * @param <T> a sub-type of Competitor
 */
public class CompetitionMock<T extends Competitor> extends Competition<T> {

    public CompetitionMock() {
        super(null, null, null, "CompetitionMock");
    }

    @Override
    protected void play(List<T> competitors) {
        // nothing
    }

    @Override
    protected void playMatch(T c1, T c2) {
        // nothing        
    }
    
    public void forceFireMatchPlayed(T c1, T c2, MatchOutcome outcome) {
        super.fireMatchPlayed(c1, c2, outcome);
    }
}
