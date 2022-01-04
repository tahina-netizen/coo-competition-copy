package etu.simonzo.competition;

import java.util.*;

import etu.simonzo.competition.competitions.Master;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.displayers.StdoutDisplayer;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.match.RandomMatch;
import etu.simonzo.competition.strategies.filter.FilteringStrategy;
import etu.simonzo.competition.strategies.filter.TakeNFirstFilteringStrategy;
import etu.simonzo.competition.strategies.group.GroupingStrategy;
import etu.simonzo.competition.strategies.group.MakeNGroupsStrategy;
import etu.simonzo.competition.strategies.sort.ArbitrarySortingStrategy;
import etu.simonzo.competition.strategies.sort.SortingStrategy;
import etu.simonzo.competition.observer.listener.Journalist;
import etu.simonzo.competition.observer.listener.Bookmaker;
import etu.simonzo.competition.observer.listener.Speaker;

public class MainMaster {

    private static int nbInGroup = 2;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java MainMaster NBGROUPS NAME [NAME ...]");
            return;
        }

        int nbGroups;
        try {
            nbGroups = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Usage: First argument should be an integer");
            return;
        }

        List<Competitor> competitors = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            Competitor competitor = new Competitor(args[i]);
            competitors.add(competitor);
        }

        if (nbGroups <= 0 ||
            competitors.size() % nbGroups != 0 ||
            competitors.size() / nbGroups < MainMaster.nbInGroup) {
            System.err.println("Usage: Incompatible number of groups and competitors");
            return;
        }

        Match<Competitor> match = new RandomMatch<>();
        GroupingStrategy gstrat = new MakeNGroupsStrategy(nbGroups);
        FilteringStrategy fstrat = new TakeNFirstFilteringStrategy(MainMaster.nbInGroup);
        SortingStrategy sstrat = new ArbitrarySortingStrategy();

        Master<Competitor> master =
            new Master<>(match, competitors, "Demo Master", gstrat, fstrat, sstrat, 3, 0, 1);

        master.addCompetitionListener(new Journalist<>(StdoutDisplayer.getInstance()));
        master.addCompetitionListener(new Bookmaker<>(StdoutDisplayer.getInstance()));
        master.addCompetitionListener(new Speaker<>(StdoutDisplayer.getInstance()));

        try {
            master.play();
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return;
        }
    }

}
