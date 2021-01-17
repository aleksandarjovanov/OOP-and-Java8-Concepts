package SecondMidterm.FootballTabel;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class Team implements Comparable<Team> {
    private String name;
    private int played, win, draw, lose, scoredGoals, concededGoals, points;
    private static Comparator<Team> comparator = Comparator.comparingInt(Team::getPoints)
            .thenComparing(Team::goalDifference)
            .reversed()
            .thenComparing(Team::getPlayed);

    public Team(String name, int played, int win, int draw, int lose, int scoredGoals, int concededGoals, int points) {
        this.name = name;
        this.played = played;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.scoredGoals = scoredGoals;
        this.concededGoals = concededGoals;
        this.points = points;
    }

    public void updateStats(int win, int draw, int lose, int scoredGoals, int concededGoals, int points) {
        this.win += win;
        this.draw += draw;
        this.lose += lose;
        this.scoredGoals += scoredGoals;
        this.concededGoals += concededGoals;
        this.points += points;
        played += 1;
    }

    public int getPlayed() {
        return played;
    }

    public int getPoints() {
        return points;
    }

    private int goalDifference() {
        return scoredGoals - concededGoals;
    }

    @Override
    public int compareTo(Team team) {
        return comparator.compare(this, team);
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, played, win, draw, lose, points);
    }
}

class FootballTable {

    private HashMap<String, Team> teamsByName;

    public FootballTable() {
        teamsByName = new HashMap<>();
    }

    private void updateTeam(String team, int win, int draw, int lose, int scoredGoals, int concededGoals, int points) {
        teamsByName.computeIfPresent(team, (k, v) -> {
            v.updateStats(win, draw, lose, scoredGoals, concededGoals, points);
            return v;
        });
        teamsByName.computeIfAbsent(team, k -> new Team(k,1, win, draw, lose, scoredGoals, concededGoals, points));
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {

        if(homeGoals > awayGoals) {
            updateTeam(homeTeam, 1, 0, 0, homeGoals, awayGoals, 3);
            updateTeam(awayTeam, 0, 0, 1, awayGoals, homeGoals, 0);
        }
        else if(awayGoals > homeGoals) {
            updateTeam(awayTeam, 1, 0, 0, awayGoals, homeGoals, 3);
            updateTeam(homeTeam, 0, 0, 1, homeGoals, awayGoals,0);
        }
        else {
            updateTeam(homeTeam, 0, 1, 0, homeGoals, awayGoals, 1);
            updateTeam(awayTeam, 0, 1, 0, awayGoals, homeGoals, 1);
        }
    }

    public void printTable() {
        List<Team> list = teamsByName.values()
                .stream()
                .sorted(Team::compareTo)
                .collect(Collectors.toList());

        int i = 1;
        for (Team t : list) {
            System.out.printf("%2d. %s\n",i, t);
            i++;
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

