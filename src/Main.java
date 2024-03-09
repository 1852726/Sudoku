import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Solution {
    private List<Integer>[][] copyToSolveBoard(List<Integer>[][] toSolveBoard) {
        List<Integer>[][] copyBoard = new List[Sudoku.SIZE][Sudoku.SIZE];
        for(int i=0;i<Sudoku.SIZE;i++) {
            for(int j=0;j<Sudoku.SIZE;j++) {
                if(toSolveBoard[i][j] == null) {
                    continue;
                }
                copyBoard[i][j] = new ArrayList<>(toSolveBoard[i][j]);
            }
        }
        return copyBoard;
    }
    private int[][] copyBoard(int[][] board) {
        int[][] copyBoard = new int[Sudoku.SIZE][];
        for(int i=0;i<Sudoku.SIZE;i++) {
            copyBoard[i] = board[i].clone();
        }
        return copyBoard;
    }
    private boolean dfs(Sudoku sudoku) throws IOException {
        int result = sudoku.solve();
        if(result == -1) {
            return false;
        }
        if(result == 0) {
            sudoku.print();
            return true;
        }
        int[][] board = sudoku.getBoard();
        List<Integer>[][] toSolveBoard = sudoku.getToSolveBoard();
        for(int i=0;i<board.length;i++) {
            for(int j=0;j<board.length;j++) {
                if(board[i][j] != 0) {
                    continue;
                }
                for (int val : toSolveBoard[i][j]) {
                    int[][] nextBoard = copyBoard(board);
                    nextBoard[i][j] = val;
                    List<Sudoku.Info> infoList = new ArrayList<>();
                    infoList.add(new Sudoku.Info(i,j,val));
                    Sudoku nextKu = new Sudoku(nextBoard,copyToSolveBoard(toSolveBoard),infoList);
                    if(i == 0) {
                        System.out.println("尝试第0行，第"+j+"列为："+val+"的数据");
                    }
                    if(dfs(nextKu)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
    public void solve() throws IOException {
        Sudoku sudoku = new Sudoku();
        dfs(sudoku);
    }
}
public class Main {
    public static void main(String[] args) throws IOException {
        long before_time = System.currentTimeMillis();
        Solution solution = new Solution();
        solution.solve();
        long end_time = System.currentTimeMillis();
        System.out.println("程序总共耗时："+(end_time-before_time)/1000+"秒");
    }
}
