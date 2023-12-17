import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DaySevenPt2 {
    private static final ArrayList<String[]> fiveKind = new ArrayList<>() ;
    private static final ArrayList<String[]> fourKind = new ArrayList<>() ;
    private static final ArrayList<String[]> threeKind = new ArrayList<>() ;
    private static final ArrayList<String[]> fullhouse = new ArrayList<>() ;
    private static final ArrayList<String[]> twoPair = new ArrayList<>() ;
    private static final ArrayList<String[]> onePair = new ArrayList<>() ;
    private static final ArrayList<String[]> highCard = new ArrayList<>() ;

    private static final Map<Character, Integer> charTimes = new HashMap<>();

    private static final Map<Character, Integer> keyValue = new HashMap<>();

    /**
     * this function init the chars value map
     */
    private static void initKV(){
        keyValue.clear();
        keyValue.put('A', 13) ;
        keyValue.put('K', 12) ;
        keyValue.put('Q', 11) ;
        keyValue.put('T', 9) ;
        keyValue.put('9', 8) ;
        keyValue.put('8', 7) ;
        keyValue.put('7', 6) ;
        keyValue.put('6', 5) ;
        keyValue.put('5', 4) ;
        keyValue.put('4', 3) ;
        keyValue.put('3', 2) ;
        keyValue.put('2', 1) ;
        keyValue.put('J', 0) ;
    }

    /**
     * this function init the arrays of cards + bids.
     */
    private static void initArr(){
        fiveKind.clear();
        fourKind.clear();
        threeKind.clear();
        twoPair.clear();
        onePair.clear();
        highCard.clear();
    }


    /**
     * this function init the chars+ how many times appear in a hand map
     */
    private static void initCharMap(){
        charTimes.clear();
        charTimes.put('A', 0) ;
        charTimes.put('K', 0) ;
        charTimes.put('Q', 0) ;
        charTimes.put('J', 0) ;
        charTimes.put('T', 0) ;
        charTimes.put('9', 0) ;
        charTimes.put('8', 0) ;
        charTimes.put('7', 0) ;
        charTimes.put('6', 0) ;
        charTimes.put('5', 0) ;
        charTimes.put('4', 0) ;
        charTimes.put('3', 0) ;
        charTimes.put('2', 0) ;
    }


    /**
     * this function checks what is the kind of given hand
     * @param hand hand with 5 cards string
     * @return number for each kind: 1..5 for one..five kind. 6 for fullhouse. 0 for highcard.
     */
    private  static int whatKind(String hand){
        initCharMap();
        int is2 = 0 ;
        int isJ = 0 ;
        boolean is3 = false ;
        boolean is4 = false ;
        for (int i = 0 ; i < hand.length() ; i++){
            int value = charTimes.get(hand.charAt(i)) ;
            value ++ ;
            charTimes.put(hand.charAt(i), value) ;
        }
        for (Character key : charTimes.keySet()) {
            int numTimes = charTimes.get(key);
            if(numTimes == 5){
                return  5;
            }
            if (numTimes == 4 && key != 'J'){
                is4 = true ;
                continue;
            }
            if (numTimes == 3 && key != 'J'){
                is3 = true ;
                continue;
            }
            if (numTimes == 2 && key != 'J'){
                is2 ++ ;
                continue;
            }
            if(key == 'J'){
                isJ = charTimes.get(key) ;
            }
        }
        if (is4){
            if (isJ > 0){
                return 4 +isJ ; //j == 1 always, having 5 kind with j
            }
            return 4; //no j
        }
        if (is3){
            if(is2 == 1){
                return 6; //fullhouse no j
            }
            return 3 + isJ ; // no j == 3 kind. 1/2 j's == four/five kind
        }
        if (is2 == 2){
            if (isJ == 1){
                return 6 ; //fullhouse with the j
            }
            return 2 ; // no j 2 pairs
        }
        if (is2 == 1){
            if(isJ > 0){
                return 2 + isJ ; //two pair+ the other extra j will make 3/4/5 kind/ no fullhouse option
            }
            return 1 ; //no j only one pair
        }
        if (isJ>0 && isJ != 1){
            return isJ+1 ; //no option for fullhouse only 4,3,2,1 kind
        }
        if (isJ== 1){
            return 1 ;
        }
        return 0 ;
    }

    /**
     * this function checks what is the value of a given card
     * @param c card with symbol
     * @return value from the map
     */
    private static int value(char c){
        return keyValue.get(c) ;
    }

    /**
     * this function get a hand that is currently at the arr and a hand
     * that we want to insert and checks if the given hand rank is bigger
     * than the already in the arr hand
     * @param curNum hand in the arr
     * @param newNum hand to insert to arr
     * @return true if the current hand is bigger than new hand, else false
     */
    private static boolean rank(String curNum, String newNum){
        for (int i = 0 ; i < curNum.length() ; i ++){
            if(value(curNum.charAt(i)) > value(newNum.charAt(i))){
                return true ;
            }
            if (value(curNum.charAt(i)) < value(newNum.charAt(i))){
                return false ;
            }
        }
        return false ;
    }


    /**
     * this function get a hand and an index to push forward all the numbers
     * coming after and insert the hand in the given index
     * @param arr current array
     * @param pair the pair to add into the array
     * @param index given index to push the pair into
     */
    private static void pushAndPut(ArrayList<String[]> arr, String[] pair, int index){
        if (index >= 0 && index <= arr.size()) {
            String[] lastPair = arr.get(arr.size()-1) ;
            for (int i = arr.size() - 1; i > index; i--) {
                arr.set(i, arr.get(i - 1)); // Shift elements to the right
            }
            arr.set(index, pair); // Insert the new value at the specified index
            arr.add(lastPair) ; // so we don't override it
        }
    }

    /**
     * this function getting a pair of hand and bid and an array and adds
     * the pair to the array in the correct place
     * @param arr array working on
     * @param pair hand and bid String[]
     */
    private static void addToArr(ArrayList<String[]> arr, String[] pair){
        if(arr.isEmpty()){
            arr.add(pair);
            return;
        }
        for (int i = 0 ; i< arr.size() ;i++){
            if(!rank(arr.get(i)[0], pair[0])){
                pushAndPut(arr,pair, i) ;
                return;
            }
        }
        arr.add(pair); // if the rank is smaller than all the others
    }

    /**
     * this function gets a string of hand and id and checks which category
     * the hand is belonged to and add it to the right array
     * @param data string of 5 chars hand and bid number
     */

    private static void category(String data){
        // create 2 cell arr and parse hand in arr[0] and bid in arr[1]
        String[] pair = data.trim().split(" +") ;
        String hand = pair[0] ;
        int kind = whatKind(hand) ;
        switch (kind){
            case 0:
                addToArr(highCard,pair) ;
                break;
            case 1:
                addToArr(onePair,pair) ;
                break;
            case 2:
                addToArr(twoPair,pair) ;
                break;
            case 3:
                addToArr(threeKind,pair) ;
                break;
            case 4:
                addToArr(fourKind,pair) ;
                break;
            case 5:
                addToArr(fiveKind,pair) ;
                break;
            case 6:
                addToArr(fullhouse,pair) ;
                break;
        }

    }

    /**
     * this function iterate all over the arrays and sum up the bids with
     * the right rank to each bid. the highest rank is for the five kind arr
     * in index 0 and the lowest rank is for high card in the last index
     * @param rank the overall card numbers arr in the file
     * @return the sum of all ranks*bids
     */
    private static long sum(long rank){
        long counter = rank ;
        long answer = 0 ;
        for(String[] pair : fiveKind){
            answer += Long.parseLong(pair[1])*counter ;
            counter -- ;
        }
        for(String[] pair : fourKind){
            answer += Long.parseLong(pair[1])*counter ;
            counter -- ;
        }
        for(String[] pair : fullhouse){
            answer += Long.parseLong(pair[1])*counter ;
            counter -- ;
        }
        for(String[] pair : threeKind){
            answer += Long.parseLong(pair[1])*counter ;
            counter -- ;
        }
        for(String[] pair : twoPair){
            answer += Long.parseLong(pair[1])*counter ;
            counter -- ;
        }
        for(String[] pair : onePair){
            answer += Long.parseLong(pair[1])*counter ;
            counter -- ;
        }
        for(String[] pair : highCard){
            answer += Long.parseLong(pair[1])*counter ;
            counter -- ;
        }
        return answer ;
    }

    /**
     * this function gets a filepath. each line in file contain 2 words:
     * one is 5 chars length: card and one is a number: bid. the function will
     * check what rank is correct for each card and sum up the multiplication
     * between bid*rank. there are as many ranks as cards amount. rank depends
     * on the category of the card+ the strength of it compare to the other same
     * category cards. every card has a special rank bigger by 1 from the card below it
     * @param filePath filepath to data
     * @return the number of summing the rank*bod of all cards
     */
    public static long totalWinningsJokers(String filePath){
        long answer;
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            initArr();
            initKV();
            long rank = DaySeven.fileLength(filePath) ;
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                category(data) ;
            }
            answer = sum(rank) ;
            scanner.close();
            return answer ;
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
            long sixthDayAnswerPt2 = totalWinningsJokers(args[0]) ; //first challenge
            System.out.println(sixthDayAnswerPt2);
        }
    }
}
