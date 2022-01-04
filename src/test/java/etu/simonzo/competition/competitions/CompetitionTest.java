package etu.simonzo.competition.competitions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.observer.listener.CompetitionListenerMock;

public abstract class CompetitionTest {

    /* Factory method */
    protected abstract Competition<Competitor> createCompetition();

    private CompetitionListenerMock<Competitor> cl1;
    private CompetitionListenerMock<Competitor> cl2;

    @BeforeEach
    void initCompetitionTest() throws Exception {
        this.cl1 = new CompetitionListenerMock<>();
        this.cl2 = new CompetitionListenerMock<>();
    }

    /* Check that Competition.play does not throw */
    @Test
    public void playShouldNotThrowException() {
        Competition<Competitor> competitionDemo = createCompetition();
        competitionDemo.play();
    }

    /* Check that listeners can be added to the competition */
    @Test
    public void canAddCompetitionListener() {
        Competition<Competitor> competition = this.createCompetition();
        competition.addCompetitionListener(this.cl1);
        competition.addCompetitionListener(this.cl2);
    }

    /* Check that previously added listeners can be removed from the set of
     * listeners without error */
    @Test
    public void canRemoveListeningCompetitionListener() {
        Competition<Competitor> competition = this.createCompetition();
        competition.addCompetitionListener(this.cl1);
        competition.addCompetitionListener(this.cl2);
        competition.removeCompetitionListener(this.cl1);
        competition.removeCompetitionListener(this.cl2);
    }

    /* Check that listeners which never were added to the set of registered
     * listeners for a competition can be removed without error */
    @Test
    public void canRemoveNotListeningCompetitionListener() {
        Competition<Competitor> competition = this.createCompetition();
        competition.removeCompetitionListener(this.cl1);
    }

    /* Check that emitted events are received by all registered listeners */
    @Test
    public void canEmitEventsForAllListeners() {
        Competition<Competitor> competition = this.createCompetition();
        competition.addCompetitionListener(this.cl1);
        competition.fireMatchPlayed(null, null, null);
        assertEquals(1, this.cl1.nbCallsMatchPlayed);
        competition.fireCompetitionStarted();
        assertEquals(1, this.cl1.nbCallsCompetitionStarted);
        competition.fireCompetitionEnded(null);
        assertEquals(1, this.cl1.nbCallsCompetitionEnded);
        competition.fireGroupsFormed(null);
        assertEquals(1, this.cl1.nbCallsGroupsFormed);
        competition.fireQualifiedCompetitorsSelectedEvent(null, null);
        assertEquals(1, this.cl1.nbCallsQualifiedCompetitorsSelected);
        competition.fireTournamentPhaseStartedEvent(0);
        assertEquals(1, this.cl1.nbCallsTournamentPhaseStarted);
    }

    /* Check that emitted events are not received by listeners which were
     * previously removed from the set of listening competitors */
    @Test
    public void eventsAreNotSentToRemovedListeners() {
        Competition<Competitor> competition = this.createCompetition();
        competition.addCompetitionListener(this.cl1);
        competition.addCompetitionListener(this.cl2);
        competition.removeCompetitionListener(this.cl2);
        competition.fireMatchPlayed(null, null, null);
        assertEquals(1, this.cl1.nbCallsMatchPlayed);
        assertEquals(0, this.cl2.nbCallsMatchPlayed);
        competition.fireCompetitionStarted();
        assertEquals(1, this.cl1.nbCallsCompetitionStarted);
        assertEquals(0, this.cl2.nbCallsCompetitionStarted);
        competition.fireCompetitionEnded(null);
        assertEquals(1, this.cl1.nbCallsCompetitionEnded);
        assertEquals(0, this.cl2.nbCallsCompetitionEnded);
        competition.fireGroupsFormed(null);
        assertEquals(1, this.cl1.nbCallsGroupsFormed);
        assertEquals(0, this.cl2.nbCallsGroupsFormed);
        competition.fireQualifiedCompetitorsSelectedEvent(null, null);
        assertEquals(1, this.cl1.nbCallsQualifiedCompetitorsSelected);
        assertEquals(0, this.cl2.nbCallsQualifiedCompetitorsSelected);
        competition.fireTournamentPhaseStartedEvent(0);
        assertEquals(1, this.cl1.nbCallsTournamentPhaseStarted);
        assertEquals(0, this.cl2.nbCallsTournamentPhaseStarted);
    }

    /* Check that when the same listener is added multiple times to a
     * competition, it only receives events once, as if it was added only
     * once */
    @Test
    public void multipleAddsOfAListenerHaveNoEffect() {
        Competition<Competitor> competition = this.createCompetition();
        competition.addCompetitionListener(this.cl1);
        competition.addCompetitionListener(this.cl1);
        competition.fireMatchPlayed(null, null, null);
        assertEquals(1, this.cl1.nbCallsMatchPlayed);
        competition.fireCompetitionStarted();
        assertEquals(1, this.cl1.nbCallsCompetitionStarted);
        competition.fireCompetitionEnded(null);
        assertEquals(1, this.cl1.nbCallsCompetitionEnded);
        competition.fireGroupsFormed(null);
        assertEquals(1, this.cl1.nbCallsGroupsFormed);
        competition.fireQualifiedCompetitorsSelectedEvent(null, null);
        assertEquals(1, this.cl1.nbCallsQualifiedCompetitorsSelected);
        competition.fireTournamentPhaseStartedEvent(0);
        assertEquals(1, this.cl1.nbCallsTournamentPhaseStarted);
    }

}
