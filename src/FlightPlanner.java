import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tigra
 * Date: 18.02.12
 * Time: 17:59
 * To change this template use File | Settings | File Templates.
 */
public class FlightPlanner {
    private File flightFile;
    private HashMap routes;

    public FlightPlanner() throws FileNotFoundException {
        flightFile = new File("files\\flights.txt");
        createRoutes();
    }

    public void createRoutes() throws FileNotFoundException {
        Scanner scanner = new Scanner(flightFile);
        try {
            HashMap<String, LinkedHashSet<String>> localRoutes = new HashMap<String, LinkedHashSet<String>>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] race = line.split("->");
                    race[0] = race[0].trim();
                    race[1] = race[1].trim();
                    if (localRoutes.entrySet().isEmpty()) {
                        createNewEntry(localRoutes, race);
                    } else {
                        fillRoutes(localRoutes, race);
                    }
                }
            }
            routes = localRoutes;
        } finally {
            scanner.close();
        }
    }

    private void fillRoutes(HashMap<String, LinkedHashSet<String>> localRoutes, String[] race) {
        if (localRoutes.get(race[0]) != null) {
            localRoutes.get(race[0]).add(race[1]);
        } else {
            createNewEntry(localRoutes, race);
        }
    }

    private void createNewEntry(HashMap<String, LinkedHashSet<String>> localRoutes, String[] race) {
        LinkedHashSet<String> newSet = new LinkedHashSet<String>();
        newSet.add(race[1]);
        localRoutes.put(race[0], newSet);
    }

    public HashMap<String, LinkedHashSet<String>> getRoutes() {
        return routes;
    }

    public static void main(String[] args) throws IOException {
        FlightPlanner flightPlanner = new FlightPlanner();
        HashMap<String, LinkedHashSet<String>> localRoutes = flightPlanner.getRoutes();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to Flight Planner!\nHere's a list of all the cities in our database:");
        String input = firstChoice(localRoutes, bufferedReader);
        trip(localRoutes, bufferedReader, input);
    }

    private static String firstChoice(HashMap<String, LinkedHashSet<String>> localRoutes, BufferedReader bufferedReader) throws IOException {
        for (Map.Entry<String, LinkedHashSet<String>> pair : localRoutes.entrySet()) {
            System.out.println(" " + pair.getKey());
        }
        System.out.println("Let's plan a round-trip route!");
        System.out.print("Enter the starting city: ");
        return bufferedReader.readLine();
    }

    private static void trip(HashMap<String, LinkedHashSet<String>> localRoutes, BufferedReader bufferedReader, String input) throws IOException {

        ArrayList<String> finalJourney = new ArrayList<String>();
        input = input.trim();
        if (localRoutes.get(input) != null) {
            String firstInput = input;
            makingChoice(localRoutes, input);
            finalJourney.add(firstInput);

            while (true) {
                String oldInput = input;
                input = bufferedReader.readLine();

                if (input.equals(firstInput)){
                    finalJourney.add(firstInput);
                    Iterator iterator = finalJourney.iterator();
                    while(iterator.hasNext()){
                        System.out.print(iterator.next());
                        if(iterator.hasNext()){
                           System.out.print(" -> ");
                        }
                    }
                    break;
                }

                if (localRoutes.get(input) != null) {
                    makingChoice(localRoutes, input);
                }else{
                    System.out.println("You have chose wrong city!Please, type correct one!");
                    makingChoice(localRoutes, oldInput);
                }
                finalJourney.add(input);
            }
        }else{
            System.out.println("You have chose wrong city!Please, type correct one!");
            input = firstChoice(localRoutes, bufferedReader);
            trip(localRoutes, bufferedReader, input);
        }
    }

    private static void makingChoice(HashMap<String, LinkedHashSet<String>> localRoutes, String input) {
        LinkedHashSet<String> cities = localRoutes.get(input);
        System.out.println("From " + input + " you can fly directly to: ");
        printCities(cities);
        System.out.print("Where do you want to go from " + input + "? ");
    }

    private static void printCities(LinkedHashSet<String> cities) {
        for (Object city : cities)
            System.out.println(" "+city);
    }
}
