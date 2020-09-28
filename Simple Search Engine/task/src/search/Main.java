package search;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    static private Scanner scanner = new Scanner(System.in);
    static private List<String> input;
    static private Map<String, List<Integer>> map = new HashMap<>();

    public static void main(String[] args) {
        String fileName = getFileName(args);

        switch (fileName) {
            case "":
                startProgram();
                break;
            default:
                startProgram(fileName);
                break;
        }

        fillMap();
        printMenu();
        makeChoice();
    }

    private static String getFileName(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if ("--data".equalsIgnoreCase(args[i])) {
                return args[++i];
            }
        }
        return "";
    }

    static private void startProgram() {
        System.out.println("Enter the number of people:");

        int number = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter all people:");

        for (int i = 0; i < number; i++) {
            input.add(scanner.nextLine());
        }
    }

    static private void startProgram(String fileName) {
        try {
            input = Files.readAllLines(Path.of(fileName));
        } catch (Exception ex) {
            System.out.println("'There is no such file exception'\nBye!");
            System.exit(0);
        }
    }

    static private void printMenu() {
        System.out.printf(
                "%s\n%s\n%s\n%s\n",
                "=== Menu ===",
                "1. Find a person",
                "2. Print all people",
                "0. Exit"
        );
    }

    static private void makeChoice() {
        boolean exit = false;

        do {
            int check = Integer.parseInt(scanner.nextLine());

            switch (check) {
                case 1:
                    findPerson();
                    break;
                case 2:
                    printAllPersons();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        } while (!exit);

        System.out.println("Bye!");
    }

    static private void printAllPersons() {
        System.out.println("=== List of people ===");

        for (String person : input) {
            System.out.println(person);
        }
    }

    static private void fillMap() {
        int i = 0;

        for (String line : input) {
            for (String word : line.split("\\s+")) {
                String lower = word.toLowerCase();
                if (!map.containsKey(lower)) {
                    map.put(lower, new ArrayList<>());
                }

                map.get(lower).add(i);
            }

            i++;
        }
    }

    static private void findPerson() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine();

        System.out.println("Enter a name or email to search all suitable people.");
        String searchValue = scanner.nextLine().toLowerCase();

        switch (strategy) {
            case "ALL":
                searchAll(searchValue);
                break;
            case "ANY":
                searchAny(searchValue);
                break;
            case "NONE":
                searchNone(searchValue);
                break;
            default:
                System.out.println("no such option");
                break;
        }
    }

    static private void searchAll(String value) {
        List<List<Integer>> lines = new ArrayList<>();

        for (String val : value.split("\\s+")) {
            if (map.containsKey(val)) {
                lines.add(map.get(val));
            }
        }

        if (lines.size() == 0) {
            System.out.println("No matching people found.");

            return;
        }

        List<Integer> firstLine = lines.get(0);
        List<Integer> resultLines = new ArrayList<>();

        for (Integer lineNum : firstLine) {
            boolean found = true;

            for (List<Integer> line : lines) {
                if (!line.contains(lineNum)) {
                    found = false;
                    break;
                }
            }

            if (found) {
                resultLines.add(lineNum);
            }
        }

        if (resultLines.size() == 0) {
            System.out.println("No matching people found.");
        } else {
            System.out.printf("%d persons found:\n", resultLines.size());

            for (Integer index : resultLines) {
                System.out.println(input.get(index));
            }
        }
    }

    static private void searchAny(String value) {
        Set<Integer> set = new HashSet<>();

        for (String val : value.split("\\s+")) {
            if (map.containsKey(val)) {
                set.addAll(map.get(val));
            }
        }

        List<Integer> resultLines = new ArrayList<>(set);

        if (resultLines.size() == 0) {
            System.out.println("No matching people found.");
        } else {
            System.out.printf("%d persons found:\n", resultLines.size());

            for (Integer index : resultLines) {
                System.out.println(input.get(index));
            }
        }
    }

    static private void searchNone(String value) {
        Set<Integer> set = new HashSet<>();

        for (String val : value.split("\\s+")) {
            if (map.containsKey(val)) {
                set.addAll(map.get(val));
            }
        }

        List<Integer> resultLinesExclude = new ArrayList<>(set);

        if (resultLinesExclude.size() == input.size()) {
            System.out.println("No matching people found.");
        } else {
            System.out.printf("%d persons found:\n", input.size() - resultLinesExclude.size());

            for (int i = 0; i < input.size(); i++) {
                if (!resultLinesExclude.contains(i)) {
                    System.out.println(input.get(i));
                }
            }
        }
    }
}
