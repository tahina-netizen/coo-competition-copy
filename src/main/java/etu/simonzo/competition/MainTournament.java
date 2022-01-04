package etu.simonzo.competition;

import java.util.*;

import etu.simonzo.competition.competitions.Competition;
import etu.simonzo.competition.competitions.Tournament;
import etu.simonzo.competition.competitors.Competitor;
import etu.simonzo.competition.displayers.StdoutDisplayer;
import etu.simonzo.competition.match.Match;
import etu.simonzo.competition.match.RandomMatch;
import etu.simonzo.competition.util.Math;
import etu.simonzo.competition.observer.listener.Journalist;
import etu.simonzo.competition.observer.listener.Bookmaker;
import etu.simonzo.competition.observer.listener.Speaker;

public class MainTournament {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java MainTournament NAME [NAME ...]");
            return;
        }

        Match<Competitor> match = new RandomMatch<>();
        List<Competitor> competitors = new ArrayList<>();
        for (String name : args) {
            Competitor competitor = new Competitor(name);
            competitors.add(competitor);
        }

        if (!Math.isPowerOfTwo(competitors.size())) {
            System.err.println("Usage: number of competitors must be a power of two");
            return;
        }

        Competition<Competitor> tournament =
            new Tournament<>(match, competitors, "Demo Tournament", 3, 0, 1);

        tournament.addCompetitionListener(new Journalist<>(StdoutDisplayer.getInstance()));
        tournament.addCompetitionListener(new Bookmaker<>(StdoutDisplayer.getInstance()));
        tournament.addCompetitionListener(new Speaker<>(StdoutDisplayer.getInstance()));

        try {
            tournament.play();
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return;
        }
    }
}
