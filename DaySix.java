import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.* ;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaySix {

    /**
     * this function parse a string of time numbers into array
     * @param data input
     * @return an arr of time-params
     */
    private static ArrayList<Long> funcParam(String data){
        ArrayList<Long> funcParam = new ArrayList<>() ;
        Pattern pattern = Pattern.compile("^Time: +");
        Matcher matcher = pattern.matcher(data);
        if(matcher.find()){
            String line = data.substring(matcher.end());
            line = line.trim() ;
            String[] params = line.split(" +") ;
            for (String param : params) {
                funcParam.add(Long.parseLong(param));
            }
        }
        return funcParam ;
    }

    /**
     * this function parse a string of records numbers into array
     * @param data input
     * @return an arr of records
     */
    private static ArrayList<Long> records(String data){
        ArrayList<Long> records = new ArrayList<>() ;
        Pattern pattern = Pattern.compile("^Distance: +");
        Matcher matcher = pattern.matcher(data);
        if(matcher.find()){
            String line = data.substring(matcher.end());
            line = line.trim() ;
            String[] recs = line.split(" +") ;
            for (String rec : recs) {
                records.add(Long.parseLong(rec));
            }
        }
        return records ;
    }

    /**
     * this function take time parameter and records and do this calculation:
     * it creates the function x(t-x) and subtract the record from it and then
     * find the range that x(x-t) -r > 0 and give back only the natural numbers
     * are valid to the range
     * @param funcParam t param
     * @param record r param
     * @return only the natural numbers valid for the range
     */
    private static long winRangeCalc(long funcParam, long record){
        double left = (double) (-funcParam+Math.sqrt(Math.pow(funcParam,2)-(4*-1*(-record))))/-2 ;
        double right =  (double) (-funcParam-Math.sqrt(Math.pow(funcParam,2)-(4*-1*(-record))))/-2 ;
        if( right % 1 == 0){
            right -- ;
        }
        long leftLong = (long) Math.floor(left);
        long rightLong = (long) Math.floor(right);
        return rightLong- leftLong ;

    }

    /**
     * this function calculates the range of time-velocity possible to break
     * the record given in each race, then multiply each range to a final answer
     * @param filePath filepath to data input
     * @return multiplication of all the specific ranges that possible to break each record
     */
    public static long rangeOfWin(String filePath){
        long answer = 1 ;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String data = scanner.nextLine();
            ArrayList<Long> funcParam = funcParam(data) ;
            data = scanner.nextLine() ;
            ArrayList<Long> records = records(data) ;
            for (int i = 0 ; i < funcParam.size() ; i++){
                answer *= winRangeCalc(funcParam.get(i), records.get(i)) ;
            }
            scanner.close();
            return answer ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Long.MIN_VALUE ;
    }

    /**
     * this function gets a string of time-params and parse them to one long parameter
     * @param data data string
     * @return record concatenate of all records given
     */
    private static long parseParam(String data){
        String num = "" ;
        Pattern pattern = Pattern.compile("^Time: +");
        Matcher matcher = pattern.matcher(data);
        if(matcher.find()){
            String line = data.substring(matcher.end());
            line = line.trim() ;
            String[] params = line.split(" +") ;
            for (String param : params) {
                num += param ;
            }
        }
        return Long.parseLong(num) ;
    }

    /**
     * this function gets a string of records and parse them to one long record
     * @param data data string
     * @return record concatenate of all records given
     */
    private static long parseRecord(String data){
        String num = "" ;
        Pattern pattern = Pattern.compile("^Distance: +");
        Matcher matcher = pattern.matcher(data);
        if(matcher.find()){
            String line = data.substring(matcher.end());
            line = line.trim() ;
            String[] records = line.split(" +") ;
            for (String rec : records) {
                num += rec ;
            }
        }
        return Long.parseLong(num) ;
    }

    /**
     * this function get a filepath for 2 lines of times and records,
     * calculate the whole time and the whole record and find the range of time-velocity to break the record
     * @param filePath filepath to data
     * @return what is the range of time-velocity to break the record
     */
    public static long rangeOfWinCombined(String filePath){
        long answer = 1 ;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String data = scanner.nextLine();
            long funcParam = parseParam(data) ;
            data = scanner.nextLine() ;
            long records = parseRecord(data) ;
            answer *= winRangeCalc(funcParam, records) ;
            scanner.close();
            return answer ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Long.MIN_VALUE ;
    }
    public static void main(String[] args) {

        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        else{
            long sixthDayAnswerPt1 = rangeOfWin(args[0]) ;
            System.out.println("day 6 part 1 answer is: "+ sixthDayAnswerPt1);

            long sixthDayAnswerPt2 = rangeOfWinCombined(args[0]) ;
            System.out.println("day 6 part 2 answer is: "+ sixthDayAnswerPt2);
        }
    }

}
