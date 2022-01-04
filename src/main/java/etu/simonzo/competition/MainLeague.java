package etu.simonzo.competition;

import java.util.*;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitions.League;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.displayers.StdoutDisplayer;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.match.RandomMatch;
import etu.simonzo.competition.observer.listener.Journalist;
import etu.simonzo.competition.observer.listener.Bookmaker;
import etu.simonzo.competition.observer.listener.Speaker;

public class MainLeague {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java MainLeague NAME [NAME ...]");
            return;
        }

        Match<Competitor> match = new RandomMatch<>();
        List<Competitor> competitors = new ArrayList<>();
        for (String name : args) {
            Competitor competitor = new Competitor(name);
            competitors.add(competitor);
        }

        Competition<Competitor> league =
            new League<>(match, competitors, "Demo League", 3, 0, 1);

        league.addCompetitionListener(new Journalist<>(StdoutDisplayer.getInstance()));
        league.addCompetitionListener(new Bookmaker<>(StdoutDisplayer.getInstance()));
        league.addCompetitionListener(new Speaker<>(StdoutDisplayer.getInstance()));

        try {
            league.play();
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return;
        }
    }
}
