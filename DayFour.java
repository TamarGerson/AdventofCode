import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class DayFour {

    private static final Map<Integer,Integer> cardsMap = new HashMap<>() ;

    /**
     * this function calculates how many points are earned in one round
     * @param input a valid string "Card   {num}: {winning numbers} | {got numbers}"
     * @return how many point the card gained: calculation is 2 pow winnings-1
     */
    private static int pointsPerGame(String input){
        int twoToThe = 0 ;
        Pattern pattern = Pattern.compile("^Card +\\d+:"); //"Card   {num}:"
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()){
            String line = input.substring(matcher.end()) ;

            String[] data = line.split("\\|") ;
            data[0] = data[0].strip() ;
            data[1] = data[1].strip();
            String[] winNumbers = data[0].split(" +");
            String[] actualNumbers = data[1].split(" +") ;

            List<String> winners = Arrays.asList(winNumbers);
            for (String actualNumber : actualNumbers) {
                if (winners.contains(actualNumber)) {
                    twoToThe++;
                }
            }
            if (twoToThe>0){
                return (int) Math.pow(2, (twoToThe-1));
            }
        }
        return 0 ;
    }

    /**
     * this function checks only how many winning numbers are in a given card
     * @param input a card line input
     * @return how many winning numbers are found in the card
     */
    private static int matchingNumbers(String input){
        int twoToThe = 0 ;
        //String[] data = input.replace("Card   1: ","").split("\\|");
        Pattern pattern = Pattern.compile("^Card +\\d+:"); //"Card   {num}:"
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String line = input.substring(matcher.end());

            String[] data = line.split("\\|");
            data[0] = data[0].strip();
            data[1] = data[1].strip();
            String[] winNumbers = data[0].split(" +");
            String[] actualNumbers = data[1].split(" +");

            List<String> winners = Arrays.asList(winNumbers);
            for (String actualNumber : actualNumbers) {
                if (winners.contains(actualNumber)) {
                    twoToThe++;
                }
            }
        }
        return twoToThe ;
    }

    /**
     * this function gets a filepath of a list of card games and calculate
     * how many points have earned in the round while
     * the calculation is 2 pow num winnings-1
     * @param filePath filepath
     * @return how many point earned in all the games
     */
    public static int winCard(String filePath){
        int answer = 0 ;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                answer += pointsPerGame(data) ;
            }
            scanner.close();
            return answer ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }

    /**
     * this function checks what is the number of lines of a file
     * @param filePath filepath
     * @return how many line are in the file
     */
    private static int fileLength(String filePath){
        int length = 0;
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                length++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return length ;
    }

    /**
     * this function checks how many copies of all the cards there are at
     * the end of reading the whole input
     * @param filePath filepath to an input
     * @return how many copies of all cards there are in total.
     */
    public static int cards(String filePath){
        int answer;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            int length = fileLength(filePath) ;
            initializeMap(length);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                updateCards(data) ;
            }
            scanner.close();
            answer = howManyCards() ;
            return answer ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }

    /**
     * this function iterate over the whole map and checks how many copies
     * of all card the map contains.
     * @return how many card are in total.
     */
    private static int howManyCards(){
        int sum = 0;
        for (int cardNum: cardsMap.keySet()){
            sum += cardsMap.get(cardNum) ;
        }
        return sum ;
    }

    /**
     * this function checks what number of card is
     * @param input card line
     * @return what card it is
     */
    private static int whatNum(String input){
        String num = "" ;
        int i = 0 ;
        Pattern pattern = Pattern.compile("^Card +"); //"Card{spaces}"
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()){
            String line = input.substring(matcher.end()) ;
            while (i< line.length()){
                if (!Character.isDigit(line.charAt(i))){
                    break;
                }
                num += line.charAt(i) ;
                i++ ;
            }
            return Integer.parseInt(num) ;
        }
        return 0 ;
    }

    /**
     * this function get a card, check what game it is and how many winning
     * its having and update the following copies coming after this value by one.
     * it does it many times as are current cards value in the map
     * @param data a line of a card
     */
    private static void updateCards(String data){
        int curr = whatNum(data);
        int wins = matchingNumbers(data) ;
        for(int j = 0 ; j< cardsMap.get(curr); j++){
            int i = 1 ;
            while (i <= wins){
                int newVal = cardsMap.get(curr+i) + 1 ;
                cardsMap.put((curr+i), newVal) ;
                i++ ;
            }
        }
    }

    /**
     * this function initialize the key value map of an int key (card number)
     * and int value: how many cards. to initialize will be key, value=1
     * @param numOfLines number of lines of the input
     */
    private static void initializeMap(int numOfLines) {
        cardsMap.clear();
        for (int i =1 ; i<= numOfLines ; i++){
            cardsMap.put(i,1) ;
        }
    }

    public static void main(String[] args) {

        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        int forthDayAnswerPt1 = winCard(args[0]) ; //first challenge
        System.out.println("day 4 part 1 answer is: "+ forthDayAnswerPt1);

        int forthDayAnswerPt2 = cards(args[0]) ;
        System.out.println("day 4 part 2 answer is: "+ forthDayAnswerPt2);
    }
}
