package JavaAflevering3;
/*
 * 
 */
import java.io.IOException;
import java.math.BigInteger;
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
    // Highest prime in the LUT
    private static final long MAX_PRIME_SMALL_LIST = 104729L;
        
    // main method to handle the programs loop. 
    // Input is handled and the trialDivision method is called.
    public static void main(String[] args) throws IOException {
        String filePath = "./10000.txt";
        Optional<List<Long>> primeList = getLookupTable(filePath);

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

            if (number <= 1) {
                System.out.println("Input must be an integer greater than 1.");
                continue;
            }

            final PrimeFactorResult primeFactors = trialDivisionWithList(number, primeList, cache);
            System.out.println(primeFactors);
        }

        System.out.println("Program stopped.");
        scanner.close();
    }

    /*
     * This method runs the trialdivision algoritm 
     * First the cache is checked, if the number is contained in the cache, then there is no need to run the algorithm again.
     * Then we check wether or not the input is prime (This can be done really fast, and is an important step to make the algorithm run fast.)
     * Then the lookup table is checked, this can give a small performance boost with small numbers as input, and could also be integrated with the cache in the future.
     * If none of the above checks then we run the trial division algorithm, which will either start from 2 or from the largest prime in the lookup table.
     */
    public static PrimeFactorResult trialDivisionWithList(long input, Optional<List<Long>> primeSmallList, Cache cache) {
        Optional<PrimeFactorResult> cacheOutput = cache.checkCache(input);
        PrimeFactorResult outputPrimeFactor = new PrimeFactorResult(input, new ArrayList<>());

        if (isPrime(input, 30) == true) {
            outputPrimeFactor.addPrimeFactor(input);
            return outputPrimeFactor;
        }

        // If the primeSmallList returns a None (this means that the file is not found),
        // Then the algorithm should start at 2.
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

    // Returns wether or not a give input is a prime number, with a given certancy
    // This is essentially just a wrapper around the isProbablePrime method.
    private static boolean isPrime(Long input, int certancy) {
        BigInteger bigInt = BigInteger.valueOf(input);

        // isPropablePrime kører lucas lehmer og rabin millers algoritme i baggrunden. 
        return bigInt.isProbablePrime(certancy);
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
    private static Optional<List<Long>> getLookupTable(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        try {
            // Iterates through the file, trims and splits at whitespaces, then maps all elements to the long type.
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

// this is a cache to hold the results, this makes looking up the same number a lot faster.
// the cache is just an object that holds a hashmap, with two methods to push and check the cache.
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

// Primefactorresult is an object that consists of a number, and its prime factors.
// The class consists mainly of getters to easily access the data.
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

    // the default method is overridden, hence the usage of the @override attribute.
    @Override
    public String toString() {
        String factorsString = primeFactorList.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
        return "List of prime factors: " + factorsString;
    }
}