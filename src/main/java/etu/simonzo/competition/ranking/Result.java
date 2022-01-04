package etu.simonzo.competition.ranking;

import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.match.MatchOutcome;

/**
 * Represents the context of the outcome of a confrontation between two competitor.
 * In other words, tells which competitor confronted with another competitor and what
 * is the outcome of that confrontation.
 * Then, a result is defined by the two confronted competitors and
 * the outcome of the match i.e there is a winner (and a loser), or the confrontation
 * is tied.
 * For identification purpose, one competitor will be called "first" and the other one
 * "second".
 */
public interface Result<T extends Competitor> {
   /**
    * give the "first" competitor: the competitor confronted to the other one returned
    * by {@link Result#getCompetitor2()}.
    * @return the "first" competitor
    */
   public T getCompetitor1();

   /**
    * give the "second" competitor: the competitor confronted to the other one returned
    * by {@link Result#getCompetitor1()}
    * @return the "second" competitor
    */
   public T getCompetitor2();

   /**
    * The outcome of the confrontation between <code>c1</code> and <code>c2</code>.
    * With:
    * <ul>
    *   <li><code>c1</code>: the competitor returned by {@link Result#getCompetitor1()}</li>
    *   <li><code>c2</code>: the competitor returned by {@link Result#getCompetitor2()}</li>
    * </ul>
    * @return
    * <ul>
    *   <li>{@link MatchOutcome#TIE} iff tie</li>
    *   <li>{@link MatchOutcome#FIRST_PLAYER_WIN} iff <code>c1</code> won</li>
    *   <li>{@link MatchOutcome#SECOND_PLAYER_WIN} iff <code>c2</code> won</li>
    * </ul>
    */
   public MatchOutcome getOutcome();
}
