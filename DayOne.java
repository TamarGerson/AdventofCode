/**
 * this class represents the first challenge of advent of code 2023.
 * each class will contain only one public static method. the class name
 * is the day of the challenge.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class DayOne {
    static Set<String> digits = new HashSet<>();
    private static void initializeDigits() {
        digits.add("one");
        digits.add("two");
        digits.add("three");
        digits.add("four");
        digits.add("five");
        digits.add("six");
        digits.add("seven");
        digits.add("eight");
        digits.add("nine");
        digits.add("zero");
    }


    /**
     * the function will find for each line the first and last digits,
     * concatenate them into a number and will sum up the numbers of each
     * line to a final number that will be the answer to the first
     * challenge of advent the code
     * @param filePath file path name
     * @return the answer of all file calibrations
     */
    public static int sumCalibrations(String filePath){
        initializeDigits();
        int answer = 0 ;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                answer += calculateLineCalibration(data) ;
            }
            scanner.close();
            return answer ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            //e.printStackTrace();
        }
        return Integer.MIN_VALUE ;
    }

    /**
     * the function is getting a line of a text, finds the first and last
     * numbers appears and concatenate them into a new number. this
     * function doesn't check validity of the string and assume there are
     * at least 2 numbers.
     * @param data a string to find the first and last digits in
     * @return new number as a concatenation of the numbers.
     */
    private static int calculateLineCalibration(String data){
        String first = specialFind(data, true) ;
        String last = specialFind(data, false) ;
        return Integer.parseInt(first+last);
    }

    /**
     * this function finds the first or last number in a given string.
     * the function doesn't check validity of the string and assume there
     * are at least 2 numbers. works only for actual digits
     * @param data the string to search in
     * @param direction true for find the first number and false for
     * finding the last number
     * @return the number as string
     */
    private static String find(String data, Boolean direction){
        if(direction){
            for(int i = 0 ; i < data.length(); i++){
                char c = data.charAt(i);
                if (Character.isDigit(c)){
                    return Character.toString(c) ;
                }
            }
        }
        else{
            for(int i = data.length() -1 ; i >= 0; i--){
                char c = data.charAt(i);
                if (Character.isDigit(c)){
                    return Character.toString(c) ;
                }
            }
        }
        return  "" ;
    }

    /**
     * this function finds the first or last number in a given string.
     * the function doesn't check validity of the string and assume there
     * are at least 2 numbers. works both for actual digits and word digits
     * (itc "one", 1)
     * @param data the string to search in
     * @param direction true for find the first number and false for
     * finding the last number
     * @return the number as string
     */
    private static  String specialFind(String data, Boolean direction){
        String[] wordDetails ;
        char c ;

        wordDetails = findWord(data, direction) ;
        if(direction){
            for(int i = 0 ; i < data.length(); i++){
                c = data.charAt(i);
                if (Character.isDigit(c)){
                    if(!(wordDetails[0].isEmpty()) && (Integer.parseInt(wordDetails[0]) < i)){
                        return  stringToInt(wordDetails[1]) ;
                    }
                    else{
                        return Character.toString(c) ;
                    }
                }
            }
        }
        else{
            for(int i = data.length() -1 ; i >= 0; i--){
                c = data.charAt(i);
                if (Character.isDigit(c)){
                    if(!(wordDetails[0].isEmpty()) && (Integer.parseInt(wordDetails[0]) > i)){
                        return  stringToInt(wordDetails[1]) ;
                    }
                    else{
                        return Character.toString(c) ;
                    }
                }
            }
        }
        return  "" ;
    }

    private static String[] findWord(String data, Boolean direction){
        String[] wordDetails = new String[2] ;
        wordDetails[0] = "" ;
        wordDetails[1] = "" ;

        if(direction) {
            for (String digit : digits) {
                if (data.contains(digit)) {
                    int index = data.indexOf(digit);
                    if (wordDetails[0].isEmpty() || index < Integer.parseInt(wordDetails[0])) {
                        wordDetails[0] = Integer.toString(index);
                        wordDetails[1] = digit;
                    }
                }
            }
        }
        else{
            for (String digit : digits) {
                if (data.contains(digit)) {
                    int index = data.lastIndexOf(digit);
                    if (wordDetails[0].isEmpty() || index > Integer.parseInt(wordDetails[0])) {
                        wordDetails[0] = Integer.toString(index);
                        wordDetails[1] = digit;
                    }
                }
            }
        }
        return wordDetails ;
    }

    private static String stringToInt(String number){
        String num = switch (number) {
            case ("one") -> "1";
            case ("two") -> "2";
            case ("three") -> "3";
            case ("four") -> "4";
            case ("five") -> "5";
            case ("six") -> "6";
            case ("seven") -> "7";
            case ("eight") -> "8";
            case ("nine") -> "9";
            case ("zero") -> "0";
            default -> "";
        };
        return num ;
    }





    public static void main (String[] args){
        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        int firstDayAnswer = sumCalibrations(args[0]) ;
        System.out.println(firstDayAnswer);

    }
}
