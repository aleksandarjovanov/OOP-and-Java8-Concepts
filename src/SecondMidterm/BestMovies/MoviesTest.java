package SecondMidterm.BestMovies;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    private String name;
    private int [] ratings;
    private double avgRating;

    public Movie(String name, int[] ratings) {
        this.name = name;
        this.ratings = ratings;
        setAvgRating();
    }

    public String getName() {
        return name;
    }

    public int[] getRatings() {
        return ratings;
    }

    private void setAvgRating() {
        avgRating = Arrays.stream(ratings)
                .average()
                .getAsDouble();
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getNumRatings() {
        return ratings.length;
    }

    public double calculateRatingCoef(int biggestNumRatingsFromAllMovies) {
        return (avgRating * getNumRatings()) / biggestNumRatingsFromAllMovies;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", name, avgRating, getNumRatings());
    }
}

class MovieFactory {

    public static Movie getMovie(String name, int [] ratings) { // dummy case of FactoryDesignPattern
        return new Movie(name, ratings);
    }
}

class MoviesList {
    private List<Movie> movieList;

    public MoviesList() {
        movieList = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movieList.add(MovieFactory.getMovie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movieList.stream()
                .sorted(Comparator.comparingDouble(Movie::getAvgRating).reversed().thenComparing(Movie::getName))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        int i = getBiggestNumRatingsFromAllMovies();
        return movieList.stream()
                .sorted(Comparator.comparingDouble((Movie m) -> m.calculateRatingCoef(i)).reversed().thenComparing(Movie::getName))
                .limit(10)
                .collect(Collectors.toList());
    }

    public int getBiggestNumRatingsFromAllMovies() {
        return movieList.stream()
                .mapToInt(Movie::getNumRatings)
                .sum();
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde