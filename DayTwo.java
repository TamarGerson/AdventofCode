import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class DayTwo {

    private static  Map<String, Integer> ballAmounts = new HashMap<>();

    /**
     * initialize the hash map with given colors and amount
     */
    private static void initializeMap() {
        ballAmounts.clear();
        ballAmounts.put("red", 12) ;
        ballAmounts.put("green", 13) ;
        ballAmounts.put("blue", 14) ;
    }

    /**
     * initialize the hash map with given colors and zero amount
     */
    private static void initializeZero(){
        ballAmounts.clear();
        ballAmounts.put("red", 0) ;
        ballAmounts.put("green", 0) ;
        ballAmounts.put("blue", 0) ;
    }

    /**
     * this function will get a filepath, check its validity and calculate
     * the numbers of lines that were possible with the given colors of
     * balls and amount
     * @param filePath file path to clone the text from
     * @return num of all lines that contain possible amount of balls
     */
    public static int sunOfPossibles(String filePath){
        initializeMap();
        int answer = 0 ;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                answer += sumGameId(data) ;
            }
            scanner.close();
            return answer ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }


    /**
     * this function gets a string that we know holds at the beginning
     * a number (with n digits). the function will get this number
     * @param data string with a number at the beginning
     * @return the number as integer
     */
    private static int getNumber(String data){
        String number = "" ;
        for (int i = 0 ; i < data.length() ; i++){
            if(Character.isDigit(data.charAt(i))){
                number = number + data.charAt(i) ;
            }
            else {
                break;
            }
        }
        return Integer.parseInt(number) ;
    }


    /**
     * this function gets a line of a text that contains number of game and
     * games seperated by semicolons. it will split the data into single
     * games and check validity of each amount of balls according to the
     * given amount of balls
     * @param data string of data about balls and their amount
     * @return game number if data was valid, else 0
     */
    private static int sumGameId( String data){
        data = data.replace("Game ", ""); //trim beginning
        int gameNumber = getNumber(data) ;
        data = data.substring((String.valueOf(gameNumber).length())+2) ; // +2 is for default spaces until starting
        String[] dataArr = data.split(";") ; // split the data into games
        for(String game : dataArr){
            String[] splitGame = game.split(",") ; //each game is trimmed to array of strings of num+ color
            for(int i = 0 ; i < splitGame.length ; i++){
                splitGame[i] = splitGame[i].trim() ;
            }
            for (String amount : splitGame){
               String[] keyValue = amount.split(" ") ;
               if(Integer.parseInt(keyValue[0]) > ballAmounts.get(keyValue[1])){
                   return 0 ;
               }
            }
        }
        return  gameNumber ;
    }

    /**
     * this function will get a filepath, check its validity and calculate
     * in each row the double of the minimun amount of balls needed in
     * each color
     * @param filePath file path to clone the text from
     * @return num of all lines that contain possible amount of balls
     */
    public static int sumMinimal(String filePath){
        int answer = 0 ;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                answer += minBallAmount(data) ;
            }
            scanner.close();
            return answer ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }


    /**
     * this function gets a line of a text that contains number of game and
     * games seperated by semicolons. it will split the data into single
     * games and check what's the minimum amount of balls required in
     * each color
     * @param data string of data about balls and their amount
     * @return the double of minimum balls required in each color
     */

    private static int minBallAmount(String data){
        int sum = 1 ;
        initializeZero();
        data = data.replace("Game ", ""); //trim beginning
        int gameNumber = getNumber(data) ;
        data = data.substring((String.valueOf(gameNumber).length())+2) ; // +2 is for default spaces until starting
        String[] dataArr = data.split(";") ; // split the data into games
        for(String game : dataArr){
            String[] splitGame = game.split(",") ; //each game is trimmed to array of strings of num+ color
            for(int i = 0 ; i < splitGame.length ; i++){
                splitGame[i] = splitGame[i].trim() ;
            }
            for (String amount : splitGame){
                String[] keyValue = amount.split(" ") ;
                if(Integer.parseInt(keyValue[0]) > ballAmounts.get(keyValue[1])){
                    ballAmounts.put(keyValue[1],Integer.parseInt(keyValue[0])) ;
                }
            }
        }
        for (int value : ballAmounts.values()) {
            sum *= value;
        }
        return sum ;
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        //int secDayAnswerPt1 = sunOfPossibles(args[0]) ;
        //System.out.println(secDayAnswerPt1);

        int secDayAnswerPt2 = sumMinimal(args[0]) ;
        System.out.println(secDayAnswerPt2);
    }
}
