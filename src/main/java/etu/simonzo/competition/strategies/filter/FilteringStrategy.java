package etu.simonzo.competition.strategies.filter;

import java.util.Collection;
import java.util.Map;

import etu.simonzo.competition.competitors.Competitor;

/**
 * Represents an algorithm which, knowing the scores of each competitor of
 * each group, filters a set of groups (each group
 * containing a set of competitors) to get a set of competitors.
 * At the end of the filtering, the result will be a collection of competitors
 * that passed through the filtering algorithm.
 * Only competitors with known scores can be filtered.
 * With a set of groups and the scores of each competitor,
 * there are many ways to filter them.
 *
 * For example, given 3 groups:
 *
 * <table>
 *  <caption>Group 1</caption>
 *      <thead>
 *          <tr>
 *              <th>Competitor</th>
 *              <th>Score</th>
 *          </tr>
 *      </thead>
 *      <tbody>
 *          <tr>
 *              <td>&nbsp;Alice</td>
 *              <td>&nbsp;42</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Bob</td>
 *              <td>&nbsp;16</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Charlie</td>
 *              <td>&nbsp;11</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Delta</td>
 *              <td>&nbsp;10</td>
 *          </tr>
 *      </tbody>
 * </table>
 *
 * <table>
 *  <caption>Group 2</caption>
 *      <thead>
 *          <tr>
 *              <th>Competitor</th>
 *              <th>Score</th>
 *          </tr>
 *      </thead>
 *      <tbody>
 *          <tr>
 *              <td>&nbsp;Enum</td>
 *              <td>&nbsp;30</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Finley</td>
 *              <td>&nbsp;20</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Goat</td>
 *              <td>&nbsp;19</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Hello</td>
 *              <td>&nbsp;17</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Ienessey</td>
 *              <td>&nbsp;17</td>
 *          </tr>
 *      </tbody>
 * </table>
 *
 * <table>
 *  <caption>Group 3</caption>
 *      <thead>
 *          <tr>
 *              <th>Competitor</th>
 *              <th>Score</th>
 *          </tr>
 *      </thead>
 *      <tbody>
 *          <tr>
 *              <td>&nbsp;Jean</td>
 *              <td>&nbsp;31</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Klay</td>
 *              <td>&nbsp;21</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Lauren</td>
 *              <td>&nbsp;21</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Marie</td>
 *              <td>&nbsp;16</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Nichols</td>
 *              <td>&nbsp;15</td>
 *          </tr>
 *          <tr>
 *              <td>&nbsp;Oakland</td>
 *              <td>&nbsp;10</td>
 *          </tr>
 *      </tbody>
 * </table>
 *
 * We can (for example):
 * <ul>
 *  <li>
 *      filter the 2 first of each group, so we get at the end [Alice, Bob
 *      , Enum, Finley, Jean, Klay]
 *  </li>
 *  <li>
 *      filter the 2 first of each group and add the 2 last of each group
 *      combined so we get at the end [Alice, Bob, Enum, Finley, Jean, Klay, Charlie, Delta]
 *  </li>
 * </ul>
 * And many other (infinite) ways to filter groups ...
 * So, an instance of a class that implements this interface defines a (precise)
 * way to implement the filtering.

 */
public interface FilteringStrategy {
    /**
     * Filter (select) some competitors among the given groups using this
     * filtering algorithm
     *
     * @param <T> a subclass of competitors
     * @param scores (each element of this collection represents a group)
     * a collection of groups to be filtered
     * @return a collection of selected competitors among the given groups
     * using this filtering algorithm
     * @throws IllegalArgumentException if selection cannot be performed
     */
    <T extends Competitor> Collection<T>
    filter(Collection<Map<T, Integer>> scores);
}
