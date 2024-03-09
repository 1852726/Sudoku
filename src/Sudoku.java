import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Sudoku {
    private static int SIZE = 9;
    private int[][] board = new int[SIZE][SIZE];
    private static String FILE_PATH = "Board.txt";
    private int[] extractNums(String line) {
        int[] ansArr = new int[SIZE];
        int ix = 0, i = 0;
        while(ix < SIZE) {
            if(line.charAt(i)>='0' && line.charAt(i)<='9') {
                ansArr[ix++] = Integer.parseInt(String.valueOf(line.charAt(i)));
            }
            i++;
        }
        return ansArr;
    }
    public void input() throws IOException {
        Scanner scanner = new Scanner(new FileReader(FILE_PATH));
        int ix = 0;
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.contains("-")) {
                continue;
            }
            board[ix++] = extractNums(line);
        }
        scanner.close();
    }
    public void print() {
        System.out.println("the ans board is:");
        for(int i=0;i<SIZE;i++) {
            if(i==3 || i==6) {
                System.out.println("----------------------------------");
            }
            for(int j=0;j<SIZE;j++) {
                System.out.print(board[i][j]);
                if(j==2 || j==5) {
                    System.out.print(" |");
                }
                System.out.print('\t');
            }
            System.out.println();
        }
    }
    class Info {
        int x,y;
        int val;
        public Info(int x,int y,int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }
    }
    private List<Info> solveList = new ArrayList<>();
    private List<Integer>[][] toSolveBoard = new List[SIZE][SIZE];
    private static List<Integer> INIT_LIST = Arrays.asList(1,2,3,4,5,6,7,8,9);
    private void processPos(int x,int y,int val) {
        if(toSolveBoard[x][y].size() <= 1) {
            return;
        }
        toSolveBoard[x][y].remove(Integer.valueOf(val));
        if(toSolveBoard[x][y].size() == 1) {
            board[x][y] = toSolveBoard[x][y].get(0);
            solveList.add(new Info(x,y,toSolveBoard[x][y].get(0)));
        }
    }
    private void processRowAndCol(Info info) {
        for(int i=0;i<SIZE;i++) {
            processPos(info.x,i,info.val);
            processPos(i,info.y,info.val);
        }
    }
    private void processBlock(Info info) {
        int from_x = (info.x/3)*3;
        int from_y = (info.y/3)*3;
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                processPos(i+from_x,j+from_y,info.val);
            }
        }
    }
    public void solve() {
        for(int i=0;i<SIZE;i++) {
            for(int j=0;j<SIZE;j++) {
                if(board[i][j]<1 || board[i][j]>9) {
                    toSolveBoard[i][j] = new ArrayList<>(INIT_LIST);
                    continue;
                }
                toSolveBoard[i][j] = new ArrayList<>();
                solveList.add(new Info(i,j,board[i][j]));
            }
        }
        int ix = 0;
        while(ix < solveList.size()) {
            Info curInfo = solveList.get(ix);
            processRowAndCol(curInfo);
            processBlock(curInfo);
            ix++;
        }
    }
}
