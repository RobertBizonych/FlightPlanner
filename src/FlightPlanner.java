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
        if(localRoutes.get(race[0]) != null){
            localRoutes.get(race[0]).add(race[1]);
        }else{
            createNewEntry(localRoutes, race);
        }
    }

    private void createNewEntry(HashMap<String, LinkedHashSet<String>> localRoutes, String[] race) {
        LinkedHashSet<String> newValue = new LinkedHashSet<String>();
        newValue.add(race[1]);
        localRoutes.put(race[0], newValue);
    }

    public HashMap<String,LinkedHashSet<String>> getRoutes() {
        return routes;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FlightPlanner flightPlanner = new FlightPlanner();
        HashMap<String, LinkedHashSet<String>> localRoutes = flightPlanner.getRoutes();
        for (Map.Entry<String, LinkedHashSet<String>> pair : localRoutes.entrySet()) {
            System.out.println(pair.getKey() + "->" + pair.getValue());
        }
    }
}