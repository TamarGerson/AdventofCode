import java.io.*;
import java.util.*;

public class DayTen {

    private static final Set<Character> chars = new HashSet<>();
    private static final Map<Character, String> before = new HashMap<>();
    private static final Map<Character, String> after = new HashMap<>();
    private static final Map<Character, String> below = new HashMap<>();
    private static final Map<Character, String> above = new HashMap<>();

    private static int colOfs ;

    private static int rowOfS ;

    private static int totalCols ;

    /**
     * this function checks where is the s coordinates and update the static field
     * @param filePath filepath
     */
    private static void wheresTheS(String filePath){
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            int rows = 0;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.indexOf('S') != -1){
                    colOfs = line.indexOf('S') ;
                    rowOfS = rows ;
                }
                rows ++ ;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * this function init all the maps and sets
     */
    private static void initSet(){
        chars.clear();

        chars.add('|') ;
        chars.add('-' );
        chars.add('J');
        chars.add('7');
        chars.add('F') ;
        chars.add('L') ;

        before.clear();
        after.clear();
        below.clear();
        above.clear();

        before.put('|', " ") ;
        before.put('-', "-LF") ;
        before.put('L', " ") ;
        before.put('7', "-LF") ;
        before.put('F', " ") ;
        before.put('J', "-LF") ;
        before.put('.', " ");

        after.put('|', " ") ;
        after.put('-', "-7J") ;
        after.put('L', "-J7") ;
        after.put('7', " ") ;
        after.put('F', "-7J" ) ;
        after.put('J', " ") ;
        after.put('.', " ");

        below.put('|', "|JL") ;
        below.put('-', " ") ;
        below.put('L', " ") ;
        below.put('7', "|JL") ;
        below.put('F', "|JL" ) ;
        below.put('J', " ") ;
        below.put('.', " ");

        above.put('|', "|F7") ;
        above.put('-', " ") ;
        above.put('L', "|F7") ;
        above.put('7', " ") ;
        above.put('F', " " ) ;
        above.put('J', "|F7") ;
        above.put('.', " ");
    }

    /**
     * this function checks how many chars are in line
     * @param filePath filepath
     * @return num of chars in line
     */
    private static int charsInLine(String filePath){
        int length = 0;
        try (Scanner scanner = new Scanner(new File(filePath))) {
            String data = scanner.nextLine() ;
            char[] chars =  data.toCharArray() ;
            scanner.close();
            return chars.length ;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
        return Integer.MIN_VALUE ;
    }


    /**
     * this function gets three lines and checks the connection of the current
     * line to prev, cur itself, and next line.
     * @param g graph
     * @param previousLine prev line
     * @param currentLine cur line
     * @param nextLine next line
     * @param index index to check neighbors in cr line
     * @param cols how many cols are in the original file
     * @param indexInLine index from 0 to cols
     */
    private static void neighbours(Graph g,char[] previousLine, char[] currentLine, char[] nextLine, int index, int cols, int indexInLine){
        /**
         * if connection in i-1 is good -> add vertex(index, index-1)
         * if connection above good -> add vertex(index,index -cols)
         */
        if (indexInLine >0 ){
            String optionVals = before.get(currentLine[indexInLine]);
            String beforeChar = String.valueOf(currentLine[indexInLine-1]);
            if(optionVals.contains(beforeChar)){
                g.addEdge(index, index-1);
            }
        }
        if (indexInLine< cols -1){
            String optionVals = after.get(currentLine[indexInLine]) ;
            String afterChar = String.valueOf(currentLine[indexInLine+1]);
            if(optionVals.contains(afterChar)){
                g.addEdge(index, index+1);
            }
        }
        if(previousLine != null)
        {
            String optionVals = above.get(currentLine[indexInLine]) ;
            String aboveChar = String.valueOf(previousLine[indexInLine]);
            if(optionVals.contains(aboveChar)){
                g.addEdge(index, index-cols);
            }
        }
        if (nextLine != null){
            String optionVals = below.get(currentLine[indexInLine]) ;
            String belowChar = String.valueOf(nextLine[indexInLine]);
            if(optionVals.contains(belowChar)){
                g.addEdge(index, index+cols);
            }
        }
    }

    /**
     * this function gets 3 lines, pass through all the indexes in the current line and checks the edges of each vertex
     * @param g graph
     * @param previousLine prev line
     * @param currentLine cur line
     * @param nextLine next line
     * @param curRow current row
     * @param col total cols in file
     * @param c char instead s point
     */
    private static void addEdges(Graph g,String previousLine, String currentLine, String nextLine, int curRow, int col, char c){
        currentLine = currentLine.replace('S', c) ;
        char[] cur = currentLine.trim().toCharArray() ;
        if (nextLine == null && previousLine == null){
            for (int i = 0 ; i < col ; i++){
                neighbours(g,null,cur,null,curRow*col+i, col,i) ;
            }
        }
        else if (previousLine == null){
            nextLine = nextLine.replace('S', c) ;
            char[] next = nextLine.trim().toCharArray();
            for (int i = 0 ; i < col ; i++){
                neighbours(g,null,cur,next,curRow*col+i, col,i) ;
            }
        }
        else if (nextLine == null){
            previousLine = previousLine.replace('S', c) ;
            char[] prev = previousLine.trim().toCharArray();
            for (int i = 0 ; i < col ; i++){
                neighbours(g,prev,cur,null,curRow*col+i, col,i) ;
            }
        }
        else {
            nextLine = nextLine.replace('S', c) ;
            previousLine = previousLine.replace('S', c) ;
            char[] next = nextLine.trim().toCharArray();
            char[] prev = previousLine.trim().toCharArray();
            for (int i = 0 ; i < col ; i++){
                neighbours(g,prev,cur,next,curRow*col+i, col,i) ;
            }
        }
    }

    /**
     * this function creates a graph from data filepath
     * @param c char to replace s in
     * @param rows rows in filepath
     * @param cols cols in filepath
     * @param filePath filepath to data
     * @return graph g vis col*row num of V
     */
    private static Graph createGraph(char c, int rows, int cols, String filePath){
        Graph g = new Graph(rows*cols) ;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            String previousLine = null;
            String nextLine;

            // Read the first line
            currentLine = reader.readLine();
            nextLine = reader.readLine();
            int curRow = 0 ;
            do {
                // Process the current line along with the previous and next lines
                addEdges(g,previousLine, currentLine, nextLine, curRow, cols, c);

                // Shift lines for the next iteration
                previousLine = currentLine;
                currentLine = nextLine;
                nextLine = reader.readLine();
                curRow ++ ;
            } while (nextLine != null);
            addEdges(g,previousLine, currentLine, null, curRow ,cols ,c);
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        return  g ;
    }

    /**
     * this function returns an arr of graphs that in each graph S replaced by char
     * @param filePath file data
     * @return an arr of graphs
     */
    public static ArrayList<Graph> graphs(String filePath){
        initSet();
        int rows = (int) DaySeven.fileLength(filePath);
        int cols = charsInLine(filePath) ;
        totalCols = cols ;
        ArrayList<Graph> graphs = new ArrayList<>() ; //6 versions instead s
        for (char c: chars){
            graphs.add(createGraph(c, rows, cols, filePath));
        }
        return graphs ;
    }

    /**
     * this function checks if there's a circle in the graph and what's the size of it
     * @param g graph
     * @return number of vertexes in cycle. if no cycle return 0
     */
    private static int vInCycle(Graph g){
        return g.findCycleSize(rowOfS*totalCols+ colOfs) ;
    }

    public static void main(String[] args) {

        if(args.length != 1){
            System.out.println("Usage: please add a file path");
        }
        else {
            int size = 0 ;
            ArrayList<Graph> graphs = graphs(args[0]) ;
            wheresTheS(args[0]);
            for (Graph graph : graphs){
                int cycle = vInCycle(graph) ;
                if (cycle>0){
                    size= cycle ;
                    break;
                }
            }
            if (size % 2 == 0){
                System.out.println("first part answer is: "+ size/2);
            }
            else {
                System.out.println("first part answer is: " + ((size/2)+1));
            }
        }
    }
}
