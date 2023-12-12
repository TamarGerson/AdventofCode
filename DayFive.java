import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayFive {

    private static final ArrayList<long[]> sts = new ArrayList<>() ;
    private static final ArrayList<long[]> stf = new ArrayList<>() ;
    private static final ArrayList<long[]> ftw = new ArrayList<>() ;
    private static final ArrayList<long[]> wtl = new ArrayList<>() ;
    private static final ArrayList<long[]> ltt = new ArrayList<>() ;
    private static final ArrayList<long[]> tth = new ArrayList<>() ;
    private static final ArrayList<long[]> htl = new ArrayList<>() ;


    /**
     * this function reset the arrays
     */
    private static void initMaps(){
        sts.clear(); //seed-to-soil map
        stf.clear(); //soil-to-fertilizer map
        ftw.clear(); //fertilizer-to-water map
        wtl.clear(); //water-to-light map
        ltt.clear(); //light-to-temperature map
        tth.clear(); //temperature-to-humidity map
        htl.clear(); //humidity-to-location map
    }

    /**
     * this function get a seed and pass it through a given function
     * @param num num to pass
     * @param func a function given
     * @return the output num
     */
    private static long funcCalc(long num, ArrayList<long[]> func){
        long cur = num ;
        for (long[] curData : func) {
            long dest = curData[0];
            long source = curData[1];
            long range = curData[2];
            if (cur >= source && (cur < source + range)) {
                long diff = Math.abs(cur - source);
                cur = dest + diff;
                break;
            }
        }
        return cur ;
    }

    /**
     * this function get a seed num and calculate its location
     * @param num num of a seed
     * @return location according to data
     */
    private static long finalPath(long num){
        long cur = num ;
        cur = funcCalc(cur,sts) ;
        cur = funcCalc(cur,stf) ;
        cur = funcCalc(cur,ftw) ;
        cur = funcCalc(cur,wtl) ;
        cur = funcCalc(cur,ltt) ;
        cur = funcCalc(cur,tth) ;
        cur = funcCalc(cur,htl) ;
        return cur ;
    }

    /**
     * this function gets a line of 3 numbers and add them as one set to
     * the right array according to the map num
     * @param data line of 3 numbers
     * @param mapNum code for knowing which category to add to
     */

    private static void  addToMap(String data, int mapNum){
        String line = data.trim() ;
        String[] ranges = line.split(" +") ;
        long range = Long.parseLong(ranges[2]);
        long source = Long.parseLong(ranges[1]) ;
        long dest = Long.parseLong(ranges[0]) ;
        long[] curTrio = {dest,source,range} ;
        switch (mapNum){
            case 1:
                sts.add(curTrio);
                break;
            case 2:
                stf.add(curTrio);
                break;
            case 3:
                ftw.add(curTrio);
                break;
            case 4:
                wtl.add(curTrio);
                break;
            case 5:
                ltt.add(curTrio);
                break;
            case 6:
                tth.add(curTrio);
                break;
            case 7:
                htl.add(curTrio);
                break;
            default:
                break;
        }
    }

    /**
     * this function get a line of seeds and parse them into array
     * @param data seeds as string
     * @return arr of seeds as long numbers
     */
    private static ArrayList<Long> seeds(String data){
        ArrayList<Long> seeds = new ArrayList<>();
        Pattern pattern = Pattern.compile("^seeds: +");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            String line = data.substring(matcher.end());
            line = line.trim() ;
            String[] seedStr = line.split(" +") ;
            for (String string : seedStr) {
                seeds.add(Long.parseLong(string)); // todo check right order
            }
        }
        return seeds ;
    }

    /**
     * this function gets filepath and calculate for the given seeds what's
     * the smallest location
     * @param filePath filepath to data
     * @return smallest location from all the seeds given
     */
    public static long lowestLocation(String filePath){
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String data = scanner.nextLine();
            initMaps();
            int mapNum = 0 ;
            ArrayList<Long> seeds = seeds(data) ;
            data = scanner.nextLine();
            while (scanner.hasNextLine()) {
                if(data.trim().isEmpty()){
                    mapNum ++ ;
                    data = scanner.nextLine() ; //from spaces to text line
                    data = scanner.nextLine() ; //from text to numbers line
                }
                addToMap(data,mapNum) ;
                data = scanner.nextLine();
            }
            long lowest = Long.MAX_VALUE;
            for(long seed : seeds){
                long location = finalPath(seed);
                if (location < lowest){
                    lowest = location ;
                }
            }
            scanner.close();
            return lowest ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }

    /**
     * this function uses the idea of binary search to find a seed that on
     * operation on the function the output of it is linear to the given seed
     * @param seed seed to operate on
     * @param range the range of seeds coming after
     * @return a seed that its operation on the function output if linear to the original seed.
     */
    private static long rangeFromBeg(long seed, long range){
        long left = seed ;
        long right = seed+range;
        long mid = left+(right-left)/ 2;
        if (finalPath(mid)== finalPath(seed)+(mid-seed)){
            return mid ;
        }
        return rangeFromBeg(seed,range/2) ;
    }

    /**
     * this function add and return array of seeds to check. the seeds came
     * from an inner function that using idea of binary search to check if
     * a function operation on the middle number in the range is bigger
     * linear than the given seed. if so we add the given seed, and we
     * start to look from this middle to the right until we finished thw whole array
     * @param seed seed to start from
     * @param range range of seeds that coming after it
     * @return an arr of possible seeds to check if minimal
     */
    private static ArrayList<Long> seedsToCheck(long seed, long range){
        ArrayList<Long> seedsToCheck = new ArrayList<>() ;
        long beg = seed ;
        long end = seed+range;
        while (beg<end){
          seedsToCheck.add(beg);
          beg = rangeFromBeg(beg, end-beg) +1;
        }
        return seedsToCheck ;
    }

    /**
     * this function get a filepath al calculate from the given instructions
     * what is the smallest location from the range of seeds
     * @param filePath filepath to input
     * @return the smallest location in this file
     */
    public static long lowestLocationRange(String filePath){
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String data = scanner.nextLine();
            initMaps();
            int mapNum = 0 ;
            ArrayList<Long> seeds = seeds(data) ;
            data = scanner.nextLine();
            while (scanner.hasNextLine()) {
                if(data.trim().isEmpty()){
                    mapNum ++ ;
                    data = scanner.nextLine() ; //from spaces to text line
                    data = scanner.nextLine() ; //from text to numbers line
                }
                addToMap(data,mapNum) ;
                data = scanner.nextLine();
            }
            long lowest = Long.MAX_VALUE;
            int i = 0 ;
            while (i<seeds.size()-1){
                long curRange = seeds.get(i+1) ;
                long seed = seeds.get(i) ;
                ArrayList<Long> toCheck = seedsToCheck(seed,curRange) ;
                for(long seedToCheck: toCheck){
                    if(finalPath(seedToCheck) < lowest){
                        lowest = finalPath(seedToCheck) ;
                    }
                }
                i +=2 ;
            }
            scanner.close();
            return lowest ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        long fifthDayAnswerPt1 = lowestLocation(args[0]) ; //first challenge
        System.out.println("day 5 part 1 answer is: "+ fifthDayAnswerPt1);

        long fifthDayAnswerPt2 = lowestLocationRange(args[0]) ;
        System.out.println("day 5 part 2 answer is: "+ fifthDayAnswerPt2);
    }
}
