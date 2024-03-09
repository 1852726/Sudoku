import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Sudoku {
    public static int SIZE = 9;
    private int[][] board = new int[SIZE][SIZE];
    private static String FILE_PATH = "Board.txt";
    private int[] extractNums(String line) {
        int[] ansArr = new int[SIZE];
        int ix = 0, i = 0;
        while(ix < SIZE) {
            if(line.charAt(i)>='0' && line.charAt(i)<='9') {
                ansArr[ix++] = line.charAt(i)-'0';
            }
            i++;
        }
        return ansArr;
    }
    public Sudoku(){}
    public Sudoku(int[][] board,List<Integer>[][] toSolveBoard,List<Info> solveList) {
        this.board = board;
        this.toSolveBoard = toSolveBoard;
        this.solveList = solveList;
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
    public int[][] getBoard() {
        return board;
    }
    public List<Integer>[][] getToSolveBoard() {
        return toSolveBoard;
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
    static class Info {
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
    private boolean processPos(int x,int y,int val) {
        if(board[x][y] == val) {
            return false;
        }
        if(toSolveBoard[x][y] == null) {
            return true;
        }
        toSolveBoard[x][y].remove(Integer.valueOf(val));
        if(toSolveBoard[x][y].size() == 1) {
            board[x][y] = toSolveBoard[x][y].get(0);
            solveList.add(new Info(x,y,toSolveBoard[x][y].get(0)));
            toSolveBoard[x][y].remove(0);
        }
        return true;
    }
    private boolean processRowAndCol(Info info) {
        boolean ans = true;
        for(int i=0;i<SIZE;i++) {
            if(i != info.x) {
                ans = ans && processPos(i,info.y,info.val);
            }
            if(i != info.y) {
                ans = ans && processPos(info.x,i,info.val);
            }
        }
        return ans;
    }
    private boolean processBlock(Info info) {
        boolean ans = true;
        int from_x = (info.x/3)*3;
        int from_y = (info.y/3)*3;
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                if(i+from_x == info.x && j+from_y==info.y) {
                    continue;
                }
                ans = ans && processPos(i+from_x,j+from_y,info.val);
            }
        }
        return ans;
    }
    private boolean isSolved() {
        for(int i=0;i<board.length;i++) {
            for(int j=0;j<board[i].length;j++) {
                if(board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @return  1 for further dfs
     *          0 for success
     *          -1 for error
     * @throws IOException
     */
    public int solve() throws IOException {
        if(solveList.isEmpty()) {
            input();
            for(int i=0;i<SIZE;i++) {
                for(int j=0;j<SIZE;j++) {
                    if(board[i][j] == 0) {
                        toSolveBoard[i][j] = new ArrayList<>(INIT_LIST);
                        continue;
                    }
                    solveList.add(new Info(i,j,board[i][j]));
                }
            }
        }
        int ix = 0;
        boolean ans;
        while(ix < solveList.size()) {
            Info curInfo = solveList.get(ix);
            ans = processRowAndCol(curInfo);
            ans = ans && processBlock(curInfo);
            if(!ans) {
                return -1;
            }
            ix++;
        }
        if(isSolved()) {
            return 0;
        }
        return 1;
    }
}
