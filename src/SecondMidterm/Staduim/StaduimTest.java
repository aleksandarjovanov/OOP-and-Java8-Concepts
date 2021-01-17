package SecondMidterm.Staduim;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Sector implements Comparable<Sector>{
    private String name;
    private int size;
    private Map<Integer, Boolean> seats;
    private int type;
    private Comparator<Sector> comparator = Comparator.comparingInt(Sector::numOfFreeSeats).reversed().thenComparing(Sector::getName);

    public Sector(String name, int size) {
        this.name = name;
        this.size = size;
        seats = fillMap();
        type = 0;
    }

    private Map<Integer, Boolean> fillMap() {
        return IntStream.range(1, size+1)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i -> false));
    }

    private int numOfFreeSeats() {
        return (int) seats.values()
                .stream()
                .filter(v -> !v)
                .count();
    }

    private int numOfTakenSeats() {
        return size - numOfFreeSeats();
    }

    private double percentFill() {
        return (1 - (double)numOfFreeSeats()/size) * 100;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", name, numOfFreeSeats(), size, percentFill());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sector)) return false;
        Sector sector = (Sector) o;
        return size == sector.size &&
                name.equals(sector.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Map<Integer, Boolean> getSeats() {
        return seats;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(Sector sector) {
        return comparator.compare(this, sector);
    }
}

class Stadium {
    private String name;
    private Map<String, Sector> sectorsByName;

    public Stadium(String name) {
        this.name = name;
        sectorsByName = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        sectorsByName = IntStream.range(0, sectorNames.length)
                .mapToObj(i -> makeSector(sectorNames[i], sectorSizes[i]))
                .collect(Collectors.toMap(Sector::getName,
                        sector -> sector));
    }

    private Sector makeSector(String name, int size) {
        return new Sector(name, size);
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatNotAllowedException, SeatTakenException {
        Sector sector = sectorsByName.get(sectorName);
        int sectorType = sector.getType();

        if(sector.getSeats().get(seat)) throw new SeatTakenException();
        else if(type == 0)  sector.getSeats().put(seat, true);
        else if(sectorType == 0 || type == sectorType) {
            sector.getSeats().put(seat, true);
            sector.setType(type);
        }
        else if((type == 1 && sector.getType() == 2) || (type == 2 && sector.getType() == 1) )
            throw new SeatNotAllowedException();
    }


    public void showSectors() {
        sectorsByName.values()
                .stream()
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);
    }
}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

@SuppressWarnings("serial")
class SeatNotAllowedException extends RuntimeException {}
@SuppressWarnings("serial")
class SeatTakenException extends RuntimeException {}

