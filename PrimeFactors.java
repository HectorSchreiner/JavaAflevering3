package JavaAflevering3;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PrimeFactors {
    static final long MAX_PRIME_SMALL_LIST = 104729L;
           
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        List<Long> primeSmallList = getLookupTable().get();
        System.out.println(trialDivision(getInput(scanner).get(), primeSmallList));   
        
        scanner.close();
    }

    public static List<Long> trialDivision(long input, List<Long> primeSmallList) {
        List<Long> outputList = new ArrayList<>();

        for (Long prime : primeSmallList) {
            while (input % prime == 0) {
                outputList.add(prime);
                input /= prime;
            }
        }
        
        if (outputList.isEmpty()) { // original input was a prime
            outputList.add(input);
        }

        return outputList;
    }

    // This method gets the user input, and returns the input as an optional Long
    private static Optional<Long> getInput(Scanner scanner) {
        System.out.print("Enter integer greater than 1 (0 to terminate): ");
        
        if (scanner.hasNextLong()) {
            Long input = scanner.nextLong();
            return Optional.of(input);

        } else {
            scanner.next(); // used to remove the bad user input
            System.out.println("Invalid input");
            return Optional.empty();
        }
    }

    // Iterates through the lookup table, and returns an option of a list that contains the first 10000 prime numbers
    private static Optional<List<Long>> getLookupTable() throws IOException {
        Path path = Paths.get("./10000.txt");

        try {
            // Iterates through the file, trims and splits at whitespaces, then maps all elements to long.
            List<Long> primeList = Arrays.asList(Files.readString(path)
                .toString()
                .trim()
                .split("\\s+"))
                .stream()
                .map(s -> Long.parseLong(s))
                .collect(Collectors.toList());

            return Optional.of(primeList);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
   
}