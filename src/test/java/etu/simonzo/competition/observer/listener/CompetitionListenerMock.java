package etu.simonzo.competition.observer.listener;

import etu.simonzo.competition.observer.event.*;
import etu.simonzo.competition.competitors.Competitor;

public class CompetitionListenerMock<T extends Competitor> implements CompetitionListener<T> {

    public CompetitionListenerMock() {
        this.nbCallsMatchPlayed = 0;
        this.nbCallsCompetitionStarted = 0;
        this.nbCallsCompetitionEnded = 0;
        this.nbCallsGroupsFormed = 0;
        this.nbCallsQualifiedCompetitorsSelected = 0;
        this.nbCallsTournamentPhaseStarted = 0;
    }

    public void matchPlayed(MatchEvent<T> event) {
        this.nbCallsMatchPlayed++;
    }

    public void competitionStarted(CompetitionStartEvent<T> event) {
        this.nbCallsCompetitionStarted++;
    }

    public void competitionEnded(CompetitionEndEvent<T> event) {
        this.nbCallsCompetitionEnded++;
    }

    public void groupsFormed(GroupsFormedEvent<T> event) {
        this.nbCallsGroupsFormed++;
    }

    public void qualifiedCompetitorsSelected(QualifiedCompetitorsSelectedEvent<T> event) {
        this.nbCallsQualifiedCompetitorsSelected++;
    }

    public void tournamentPhaseStarted(TournamentPhaseStartedEvent<T> event) {
        this.nbCallsTournamentPhaseStarted++;
    }

    public int nbCallsMatchPlayed;

    public int nbCallsCompetitionStarted;

    public int nbCallsCompetitionEnded;

    public int nbCallsGroupsFormed;

    public int nbCallsQualifiedCompetitorsSelected;

    public int nbCallsTournamentPhaseStarted;

}
