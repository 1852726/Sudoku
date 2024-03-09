import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Sudoku sudoku = new Sudoku();
        sudoku.input();
        sudoku.solve();
        sudoku.print();
    }
}
