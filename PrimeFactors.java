package JavaAflevering3;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PrimeFactors {
    private static final long MAX_PRIME_SMALL_LIST = 104729L;
           
    public static void main(String[] args) throws IOException {
        Optional<List<Long>> primeList = getLookupTable();

        Cache cache = new Cache();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter integer greater than 1 (0 to terminate): ");

            if (!scanner.hasNextLong()) {
                System.out.println("Invalid input.");
                scanner.next(); 
                continue;      
            }

            long number = scanner.nextLong();

            if (number == 0) {
                break;
            }

            if (number < 2) {
                System.out.println("Input must be an integer greater than 1.");
                continue;
            }

            final PrimeFactorResult primeFactors = trialDivisionWithList(number, primeList, cache);
            System.out.println(primeFactors);
        }

        System.out.println("Program stopped.");
        scanner.close();
    }

    public static PrimeFactorResult trialDivisionWithList(long input, Optional<List<Long>> primeSmallList, Cache cache) {
        Optional<PrimeFactorResult> cacheOutput = cache.checkCache(input);
        PrimeFactorResult outputPrimeFactor = new PrimeFactorResult(input, new ArrayList<>());

        if (primeSmallList.isEmpty()) {
            findRemainingFactors(input, outputPrimeFactor, 2L);
            cache.push(outputPrimeFactor);

            return outputPrimeFactor;
        }

        if (cacheOutput.isPresent()) {
            return cacheOutput.get();
        }

        for (Long prime : primeSmallList.get()) {
            while (input % prime == 0) {
                outputPrimeFactor.addPrimeFactor(prime);
                input /= prime;
            }
        }
        
        if (input > 1) {
            findRemainingFactors(input, outputPrimeFactor, MAX_PRIME_SMALL_LIST);
        }

        cache.push(outputPrimeFactor);

        return outputPrimeFactor;
    }

    private static void findRemainingFactors(Long input, PrimeFactorResult result, Long start) {
        if (start % 2 == 0) start++;

        for (long i = start; i * i <= input; i += 2) {
            while (input % i == 0) {
                result.addPrimeFactor(i);
                input /= i;
            }
        }

        if (input > 1) {
            result.addPrimeFactor(input);
        }
    }

    // Iterates through the lookup table, and returns an option of a list that contains the first 10000 prime numbers
    private static Optional<List<Long>> getLookupTable() throws IOException {
        Path path = Paths.get("./10001.txt");

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

public class Cache {
    private HashMap<Long, PrimeFactorResult> primeFactorResultMap;

    public Cache() {
        this.primeFactorResultMap = new HashMap<>();
    }    

    public void push(PrimeFactorResult primeFactorResult) {
        this.primeFactorResultMap.put(primeFactorResult.getNumber(), primeFactorResult);
    }

    public Optional<PrimeFactorResult> checkCache(Long input) {
        return Optional.ofNullable(this.primeFactorResultMap.get(input));
    }
}

public class PrimeFactorResult {
    private final Long number;
    private final List<Long> primeFactorList;

    public PrimeFactorResult(Long number, List<Long> primeFactorList) {
        this.number = number;
        this.primeFactorList = primeFactorList;
    }

    public void addPrimeFactor(Long primeFactor) {
        this.primeFactorList.add(primeFactor);
    }

    public boolean isEmpty() {
        return this.primeFactorList.isEmpty();
    }

    public Long getNumber() {
        return this.number;
    }

    public List<Long> getPrimeFactorList() {
        return this.primeFactorList;
    }

    @Override
    public String toString() {
        String factorsString = primeFactorList.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
        return "List of prime factors: " + factorsString;
    }
}