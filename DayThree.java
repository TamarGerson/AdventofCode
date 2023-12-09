import java.io.*;

public class DayThree {

    /**
     * the function will find for each line the asterisks that adjacent to
     * only 2 numbers, find the numbers and multiply them. it sums the
     * numbers to a final number that will be the answer to the sec
     * challenge of advent the code day 3
     * @param filePath file path name
     * @return the answer of all asterisks adjacent multiply + sum all the numbers
     */
    public static int gears(String filePath){
        int sum = 0 ;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            String previousLine = null;
            String nextLine;

            // Read the first line
            currentLine = reader.readLine();
            nextLine = reader.readLine();

            do {
                // Process the current line along with the previous and next lines
                sum += numNearAsterisks(previousLine, currentLine, nextLine);

                // Shift lines for the next iteration
                previousLine = currentLine;
                currentLine = nextLine;
                nextLine = reader.readLine();
            } while (nextLine != null);

            sum += numNearAsterisks(previousLine, currentLine, null);
            return sum ;

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }

    /**
     * this function get a row and looking for an asterisk in. while find
     * one, check if there's 2 and only 2 numbers adjacent to and if so
     * multiply them and at the end sum up all the multiplications came
     * from all the asterisks in the line.
     * @param prev prev line
     * @param curr current line to look for asterisks in
     * @param next next line
     * @return sum of the multiplications came from all the asterisks in the line.
     */
    private static int numNearAsterisks(String prev, String curr, String next){
        int i = 0 ;
        int sum = 0 ;
        int curNum ;
        while(i < curr.length()){
            if(curr.charAt(i) == '*'){
                int adjTo = howManyAdj(prev, curr, next,i) ;
                if(adjTo == 2){
                    curNum = findAndMulti(prev,curr,next,i) ;
                    sum += curNum ;
                }
            }
            i++ ;
        }
        return sum ;
    }

    /**
     * this function find all the numbers adjacent to an asterisk and multiply them
     * @param prev prev line, can be null
     * @param curr curr line, can be null
     * @param next next line, can be null
     * @param index valid index of an asterisk
     * @return multiply all the numbers adjacent to the given asterisk
     */
    private static int findAndMulti(String prev, String curr, String next, int index){
        int sum;
        if(prev == null && next == null){
            sum = curNums(curr, index) ;
        }
        else if(prev == null){
            sum = curNums(curr,index) * upBelowNums(next, index) ;
        }
        else if(next == null){
            sum = curNums(curr,index) * upBelowNums(prev,index) ;
        }
        else{
            sum = curNums(curr,index) * upBelowNums(prev,index) * upBelowNums(next,index) ;
        }
        return sum ;
    }

    /**
     * this function finds the numbers above/ below and adjacent to a given
     * asterisk and multiply them. only one line at the same operation
     * @param line line below/ above to operate
     * @param index index of a valid asterisk.
     * @return multiplication of the 2 numbers, if existed. one num if it's
     * the only one and 1 if none adjacent
     */
    private static int upBelowNums(String line, int index){
        String left = nums(line, index-1) ;
        String middle = nums(line, index) ;
        String right = nums(line, index+1) ;

        if(left.isEmpty() && middle.isEmpty() && right.isEmpty()){
            return 1 ;
        }
        if (middle.isEmpty() && right.isEmpty()){
            return Integer.parseInt(left) ;
        }
        if(middle.isEmpty() && left.isEmpty()){
            return Integer.parseInt(right) ;
        }
        if (right.isEmpty() && left.isEmpty()){
            return  Integer.parseInt(middle) ;
        }
        if(middle.isEmpty()){
            return Integer.parseInt(left) * Integer.parseInt(right) ;
        }
        return Integer.parseInt(middle) ;
    }

    /**
     * this function finds the numbers from the left and right to a given
     * asterisk and multiply them
     * @param line line to check in
     * @param index an index of an asterisk.
     * @return multiplication of the 2 numbers, if existed. one num if it's
     * the only one and 1 if none adjacent
     */
    private static int curNums(String line, int index){
        String numR = "" ;
        String numL = "";
        int i = index+1 ; //curr index is "*"
        while (i<line.length()){
            if(!Character.isDigit(line.charAt(i))){
                break;
            }
            numR += line.charAt(i) ;
            i++ ;
        }

        i = index-1 ; //curr index is "*"
        while (i >= 0){
            if(!Character.isDigit(line.charAt(i))){
                break;

            }
            numL = line.charAt(i) + numL ;
            i-- ;
        }
        if (numR.isEmpty() && numL.isEmpty()){
            return 1 ;
        }
        if ((!numL.isEmpty()) && (!numR.isEmpty()) ){
            return (Integer.parseInt(numL)* Integer.parseInt(numR)) ;
        }
        if(numL.isEmpty()){
            return Integer.parseInt(numR) ;
        }

        return Integer.parseInt(numL) ;
    }

    private static String nums(String line,int index){
        String num = "" ;
        int i = index ;
        if(i<0 || i>line.length() || !Character.isDigit(line.charAt(index))){
            return "" ;
        }
        while (i< line.length()){ //add from right to digit
            if(Character.isDigit(line.charAt(i))){
                num += line.charAt(i) ;
                i++ ;
            }
            else{
                break;
            }
        }
        i = index-1 ;
        while (i>= 0){ //add from left to digit
            if(Character.isDigit(line.charAt(i))){
                num = line.charAt(i) + num ;
                i-- ;
            }
            else{
                break;
            }
        }
        return num ;
    }


    /**
     * this function check how many numbers adjacent to an asterisks in current line.
     * @param prev prev line
     * @param curr current line
     * @param next next line
     * @param index index of an asterisk in curr line
     * @return num of adjacent numbers
     */
    private static int  howManyAdj(String prev, String curr, String next, int index){
        int[] prevBits = new int[3] ;
        int[] nextBits = new int[3] ;
        int curBits  ;
        if(prev == null && next == null){ //only cur (text.length == 1)
            if(index >0 && index< curr.length()-1) {
                curBits = (checkForNums(curr.charAt(index-1)) + checkForNums(curr.charAt(index+1)));
            }
            else{
                return 0; //doesnt relevant
            }
        }
        else if (prev == null) { //beginning of a text at lest length 2
            if(index >0 && index< curr.length()-1) {
                curBits = checkForNums(curr.charAt(index-1)) + checkForNums(curr.charAt(index+1));
                nextBits[0] = checkForNums(next.charAt(index-1));
                nextBits[1] = checkForNums(next.charAt(index));
                nextBits[2] = checkForNums(next.charAt(index+1));

            }
            else if(index == 0){
                curBits = checkForNums(curr.charAt(index+1));
                nextBits[1] = checkForNums(next.charAt(index));
                nextBits[2] = checkForNums(next.charAt(index+1)) ;
            }
            else {
                curBits = checkForNums(curr.charAt(index-1));
                nextBits[0] = checkForNums(next.charAt(index-1));
                nextBits[1] = checkForNums(next.charAt(index)) ;
            }
        }
        else if (next == null) { //end of text
            if(index >0 && index< curr.length()-1) {
                curBits = checkForNums(curr.charAt(index-1)) + checkForNums(curr.charAt(index+1));
                prevBits[0] = checkForNums(prev.charAt(index-1)) ;
                prevBits[1]= checkForNums(prev.charAt(index));
                prevBits[2] = checkForNums(prev.charAt(index+1)) ;
            }
            else if(index == 0){
                curBits = checkForNums(curr.charAt(index+1));
                prevBits[1] = checkForNums(prev.charAt(index)) ;
                prevBits[2] = checkForNums(prev.charAt(index+1)) ;
            }
            else {
                curBits = checkForNums(curr.charAt(index-1));
                prevBits[0] = checkForNums(prev.charAt(index-1));
                prevBits[1] = checkForNums(prev.charAt(index)) ;
            }
        }
        else { //middle of a text
            if(index >0 && index< curr.length()-1) {
                curBits = checkForNums(curr.charAt(index-1)) + checkForNums(curr.charAt(index+1)) ;
                prevBits[0]= checkForNums(prev.charAt(index-1)) ;
                prevBits[1] = checkForNums(prev.charAt(index)) ;
                prevBits[2] =checkForNums(prev.charAt(index+1)) ;

                nextBits[0] = checkForNums(next.charAt(index-1));
                nextBits[1] = checkForNums(next.charAt(index));
                nextBits[2] = checkForNums(next.charAt(index+1)) ;
            }
            else if(index == 0){
                curBits = checkForNums(curr.charAt(index+1));
                prevBits[1] = checkForNums(prev.charAt(index)) ;
                prevBits[2] = checkForNums(prev.charAt(index+1));
                nextBits[1] =checkForNums(next.charAt(index)) ;
                nextBits[2] = checkForNums(next.charAt(index+1)) ;
            }
            else {
                curBits = checkForNums(curr.charAt(index-1)) ;
                prevBits[0] =checkForNums(prev.charAt(index-1)) ;
                prevBits[1] = checkForNums(prev.charAt(index)) ;

                nextBits[0] =checkForNums(next.charAt(index-1));
                nextBits[1] =checkForNums(next.charAt(index)) ;
            }
        }
        return curBits+ handleBits(prevBits,nextBits) ;
    }

    /**
     * this function gets 2 arrays length 3 each. the function assume
     * there's an asterisk above next-bits and below prev-bits in the middle
     * and sum up the arrays separately so know were there are numbers adjacent to the asterisk.
     * @param prevBits prev bits array were there are 0'z where there's no digits and 1 where there are
     * @param nextBits next bits array were there are 0'z where there's no digits and 1 where there are
     * @return how many numbers adjacent to the asterisk in the middle of the arrays.
     */
    private static int handleBits(int[] prevBits, int[] nextBits){
        int prevSum = 0 ;
        int nextSum = 0;
        for (int prevBit : prevBits) {
            prevSum += prevBit;
        }
        for (int nextBit : nextBits) {
            nextSum += nextBit;
        }

        if(prevSum == 3){
            prevSum = 1; //its only one number
        }
        if (prevSum == 2 && prevBits[1] == 1){
            prevSum = 1 ;
        }

        if(nextSum == 3){
            nextSum = 1; //its only one number
        }
        if (nextSum == 2 && nextBits[1] == 1){
            nextSum = 1 ;
        }
        return nextSum+prevSum ;
    }

    private static int checkForNums(char c){
        if(Character.isDigit(c)){
            return 1 ;
        }
        return 0 ;
    }

    /**
     * this method get a file path, check its validity and return the sum of every number that has a symbol adjacent to it
     * @param filePath file to read from
     * @return sum of valid numbers
     */
    public static int engineSum(String filePath) {
        int sum = 0 ;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            String previousLine = null;
            String nextLine;

            // Read the first line
            currentLine = reader.readLine();
            nextLine = reader.readLine();

            do {
                // Process the current line along with the previous and next lines
                sum += processLines(previousLine, currentLine, nextLine);

                // Shift lines for the next iteration
                previousLine = currentLine;
                currentLine = nextLine;
                nextLine = reader.readLine();
            } while (nextLine != null);

            sum += processLines(previousLine, currentLine, null);
            return sum ;

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }

    /**
     * this method gets 3 lines and checks only for the current line what numbers are valid and sum them up.
     * @param prev prev line so that current line can use it to check adjacency
     * @param curr curr line to check valid numbers in
     * @param next next line so that current line can use it to check adjacency
     * @return the sum of valid numbers in the current row
     */
    private static int processLines(String prev, String curr, String next){
        int i = 0 ;
        int sum = 0 ;
        int curNum ;
        while(i < curr.length()){
            if(!Character.isDigit(curr.charAt(i))){
                i ++ ;
            }
            else{
                curNum = isValid(prev,curr,next,i) ;
                if(curNum >=0){
                  sum += curNum ;
                  i += Integer.toString(curNum).length();
                }
                else {
                    i++ ; //because of -1 well get into that function num.length times
                }

            }
        }
        return sum ;
    }


    /**
     * this function gets 3 lines and a starting index that we know it's a digit. it will check
     * what's the number and if one of the digits is near a sign.
     * if so it'll return the num. else -1
     * @param prev prev line so that current line can use it to check adjacency
     * @param curr current line to check if the digit starting in the ith place is valid
     * @param next next line so that current line can use it to check adjacency
     * @param i start index of a number
     * @return number is its valid, -1 is not.
     */
    private static int isValid(String prev, String curr, String next, int i){
        int counter = i ;
        String num = "" ;
        boolean flag = true ;
        while (flag && counter< curr.length()){
            if(!Character.isDigit(curr.charAt(counter))){
                flag = false;
            }
            else {
                num += curr.charAt(counter) ;
                counter ++ ;
            }
        }
        counter = i ;
        flag= false ;
        while (counter < num.length()+i){
            flag = flag || validDigit(prev,curr,next,counter) ;
            counter ++ ;
        }
        if (flag){
            return Integer.parseInt(num) ;
        }
        return -1 ;
    }


    /**
     * this function check validation for a specific number in that index.
     * the function always gets an index that for sure there is number at
     * @param prev prev line so that current line can use it to check adjacency
     * @param curr current line to check if the digit in the ith place is valid
     * @param next next line so that current line can use it to check adjacency
     * @param index index of a digit
     * @return true is valid, else false
     */
    private static boolean validDigit(String prev, String curr, String next, int index){
        boolean flag;
        if(prev == null && next == null){ //only cur (text.length == 1)
            if(index >0 && index< curr.length()-1) {
                flag = (check(curr.charAt(index-1)) || check(curr.charAt(index+1)));
            }
            else if(index == 0){
                flag = check(curr.charAt(index+1)) ;
            }
            else {
                flag = check(curr.charAt(index-1)) ;
            }
        }
        else if (prev == null) { //beginning of a text at lest length 2
            if(index >0 && index< curr.length()-1) {
                flag = (check(curr.charAt(index-1)) || check(curr.charAt(index+1))
                        || check(next.charAt(index-1)) || check(next.charAt(index)) || check(next.charAt(index+1))) ;
            }
            else if(index == 0){
                flag = (check(curr.charAt(index+1)) || check(next.charAt(index)) || check(next.charAt(index+1))) ;
            }
            else {
                flag = (check(curr.charAt(index-1)) || check(next.charAt(index-1)) || check(next.charAt(index))) ;
            }
        }
        else if (next == null) { //end of text
            if(index >0 && index< curr.length()-1) {
                flag = (check(curr.charAt(index-1)) || check(curr.charAt(index+1)) || check(prev.charAt(index-1))
                        || check(prev.charAt(index)) || check(prev.charAt(index+1))) ;
            }
            else if(index == 0){
                flag = (check(curr.charAt(index+1)) || check(prev.charAt(index)) || check(prev.charAt(index+1))) ;
            }
            else {
                flag = (check(curr.charAt(index-1)) || check(prev.charAt(index-1)) || check(prev.charAt(index))) ;
            }
        }
        else { //middle of a text
            if(index >0 && index< curr.length()-1) {
                flag = (check(curr.charAt(index-1)) || check(curr.charAt(index+1))
                        || check(prev.charAt(index-1)) || check(prev.charAt(index))
                        || check(prev.charAt(index+1)) || check(next.charAt(index-1)) || check(next.charAt(index)) || check(next.charAt(index+1))) ;
            }
            else if(index == 0){
                flag = (check(curr.charAt(index+1)) || check(prev.charAt(index))
                        || check(prev.charAt(index+1)) || check(next.charAt(index)) || check(next.charAt(index+1))) ;
            }
            else {
                flag = (check(curr.charAt(index-1)) || check(prev.charAt(index-1)) || check(prev.charAt(index))
                        || check(next.charAt(index-1)) || check(next.charAt(index))) ;
            }
        }
        return flag ;
    }

    /**
     * this function check if a char is a symbol but not a dot. used for
     * checking adjacent is outer function
     * @param c char to check
     * @return true is valid symbol (all except '.'), else false
     */
    private static boolean check(char c){
        return ((c> 20 && c < 65) && (c != 46) && !Character.isDigit(c)) ;
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        int thirdDayAnswerP1 = engineSum(args[0]) ;
        System.out.println("first part answer is: " + thirdDayAnswerP1);

        int thirdDayAnswerP2 = gears((args[0])) ;
        System.out.println("sec part answer is: "+ thirdDayAnswerP2);
    }
}



