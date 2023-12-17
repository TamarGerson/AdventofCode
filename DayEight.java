import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DayEight {

private static final Map<String, String[]> instructions = new HashMap<>();

    /**
     * this function iterate over all the nodes and add them to a map of
     * string key and string[] arr of left and right directions as value
     * @param filePath filepath to data
     */
    private static void insertMap(String filePath){
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine() ;
            line = scanner.nextLine() ; //ignore 2 first lines
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                String[] divided = line.split("=") ;
                divided[0] = divided[0].trim() ;
                divided[1] = divided[1].trim() ;
                String[] leftRight = divided[1].replaceAll("[ ()]", "").split(",") ;
                instructions.put(divided[0], leftRight) ;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * this function gets the next i
     * @param i index
     * @param instructions instruction string
     * @return next index
     */
    private static long nextIndex(long i, String instructions){
        if(i == instructions.length()-1){
            i = 0 ;
        }
        else{
            i++ ;
        }
        return i ;
    }

    /**
     * this function gets a key and direction and returns the next node
     * @param key key in the map
     * @param direction l or r
     * @return the next node coordinate to direction
     */
    private static String getNext(String key, char direction){
        String [] options = instructions.get(key) ;
        if (direction == 'L'){
            return options[0] ;
        }
        return options[1] ; //R
    }

    /**
     * this function checks what is the path length from node AAA to node ZZZ
     * @param filePath path to data
     * @return the path length from node AAA to node ZZZ
     */
    public static long pathLength(String filePath){
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            insertMap(filePath) ;
            String instructions = scanner.nextLine().trim() ;
            long i = 0;
            long counter = 0;
            String key = "AAA" ;
            while (!key.equals("ZZZ")) {
                key = getNext(key,instructions.charAt((int) i)) ;
                i = nextIndex(i,instructions) ;
                counter ++ ;
            }
            scanner.close();
            return counter ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }

    /**
     * this function returns all nodes that ends with A
     * @return arr of string that each ends with A
     */
    private static ArrayList<String> getStart(){
        ArrayList<String> startKeys = new ArrayList<>() ;
        for (String key : instructions.keySet()) {
            if (key.endsWith("A")) {
                startKeys.add(key);
            }
        }
        return startKeys ;
    }

    /**
     * this function ches if a string ends with Z
     * @param key string to check
     * @return true if valid, else false
     */
    private static boolean isValid(String key){
        return key.endsWith("Z");
    }

    /**
     * this function gets steps array, start nodes array and instructions
     * and checks how many steps doest it take to each vertex to get to a
     * node that ends with z and update the number of steps in the same
     * index in steps arr
     * @param steps steps arr, init to 0
     * @param keys all nodes that ends with A
     * @param instructions string of instructions
     */
    private static void update(long[] steps, ArrayList<String> keys, String instructions){
        for (long i = 0 ; i< keys.size() ; i++){
            String key = keys.get((int) i);
            long counter = steps[(int) i] ;
            while (!isValid(key)){
                key = getNext(key, instructions.charAt((int) (counter%instructions.length()))) ;
                counter ++ ;
            }
            steps[(int) i] = counter ;
            keys.set((int) i, key) ;
        }
    }

    /**
     * this function calculate gcd of 2 numbers
     * @param a first num
     * @param b sec num
     * @return gcd
     */
    private static long calculateGCD(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    /**
     * this function calculate the lcm of an array of numbers
     * @param numbers array of numbers
     * @return lcm of all the numbers
     */
    private static long calculateLCM(long[] numbers) {
        long lcm = 1;
        for (long i = 0; i < numbers.length; i++) {
            lcm = calculateLCM(lcm, numbers[(int) i]);
        }
        return lcm;
    }

    /**
     * this function calculate the lcm of 2 numbers
     * @param a first num
     * @param b sec num
     * @return lcm
     */
    private static long calculateLCM(long a, long b) {
        return Math.abs(a * b) / calculateGCD(a, b);
    }

    /**
     * this function use the cycled input and calculate "simultaneously"
     * the number to reach from every node that ends with A to node that ends
     * with Z at the same time for all of them. the trick is to use lcm because
     * every node has always the same amount of steps between start point to end point
     * @param filePath
     * @return
     */
    public static long simultPaths(String filePath){
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            insertMap(filePath) ;
            String instructions = scanner.nextLine().trim() ;
            ArrayList<String> keys = getStart() ;
            long[] steps = new long[keys.size()] ;
            update(steps,keys,instructions);
            long lcmAns = calculateLCM(steps) ;
            scanner.close();
            return lcmAns ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }
    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        else {
            long eightDayAnswerPt1 = pathLength(args[0]) ; //first challenge
            System.out.println(eightDayAnswerPt1);

            long eightDayAnswerPt2 = simultPaths(args[0]) ;
            System.out.println(eightDayAnswerPt2);
        }
    }
}
