package solver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Scanner;
import java.io.IOException;

class InputPiece {
    List<char[]> matrix;
    String color;

    public InputPiece() {
        this.matrix = new ArrayList<>();
    }

    public void setMatrix(char[] matrixs) {
        this.matrix.add(matrixs);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    public List<char[]> getMatrix() {
        return this.matrix;
    }
}

public class Puzzle {

    public static boolean hasCommonElement(char[] arr1, char[] arr2) {
        for (char c : arr1) {
            for (char d : arr2) {
                if (c == d && c != ' ' && d != ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    public static int lenPieceRow(String filepath) {
        int row = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String[] dimensions = reader.readLine().split(" ");
            String S = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return row;
    }

    public static int longestPiece(String filepath) {
        int maxlen = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String[] dimensions = reader.readLine().split(" ");
            String S = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > maxlen) {
                    maxlen = line.length();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return maxlen;
    }

    public static List<char[]> padMatrix(List<char[]> matrix) {
        int maxLength = matrix.stream()
                .mapToInt(row -> row.length)
                .max()
                .orElse(0);

        List<char[]> paddedMatrix = new ArrayList<>();
        for (char[] row : matrix) {
            char[] paddedRow = new char[maxLength];
            System.arraycopy(row, 0, paddedRow, 0, row.length);
            for (int i = row.length; i < maxLength; i++) {
                paddedRow[i] = ' ';
            }
            paddedMatrix.add(paddedRow);
        }
        return paddedMatrix;
    }

    public static List<char[]> rotate90(List<char[]> matrix) {
        List<char[]> padded = padMatrix(matrix);
        int rows = padded.size();
        int cols = padded.get(0).length;

        List<char[]> rotated = new ArrayList<>();
        for (int j = 0; j < cols; j++) {
            char[] newRow = new char[rows];
            for (int i = 0; i < rows; i++) {
                newRow[i] = padded.get(rows - 1 - i)[j];
            }
            rotated.add(newRow);
        }
        return rotated;
    }

    public static List<char[]> rotate180(List<char[]> matrix) {
        List<char[]> padded = padMatrix(matrix);
        List<char[]> rotated = new ArrayList<>();
        for (int i = padded.size() - 1; i >= 0; i--) {
            char[] reversedRow = new char[padded.get(i).length];
            for (int j = 0; j < reversedRow.length; j++) {
                reversedRow[j] = padded.get(i)[reversedRow.length - 1 - j];
            }
            rotated.add(reversedRow);
        }
        return rotated;
    }

    public static List<char[]> rotate270(List<char[]> matrix) {
        List<char[]> padded = padMatrix(matrix);
        int rows = padded.size();
        int cols = padded.get(0).length;

        List<char[]> rotated = new ArrayList<>();
        for (int j = cols - 1; j >= 0; j--) {
            char[] newRow = new char[rows];
            for (int i = 0; i < rows; i++) {
                newRow[i] = padded.get(i)[j];
            }
            rotated.add(newRow);
        }
        return rotated;
    }

    public static List<char[]> mirrorHorizontal(List<char[]> matrix) {
        List<char[]> mirrored = new ArrayList<>();
        for (char[] row : matrix) {
            char[] newRow = new char[row.length];
            for (int i = 0; i < row.length; i++) {
                newRow[i] = row[row.length - 1 - i];
            }
            mirrored.add(newRow);
        }
        return mirrored;
    }

    // mengisi data
    public static Map<String, Object> readInput(String filepath) {
        Map<String, Object> result = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            // baca baris pertama: N M P S
            String[] dimensions = reader.readLine().split(" ");

            int N = Integer.parseInt(dimensions[0]);
            int M = Integer.parseInt(dimensions[1]);
            int P = Integer.parseInt(dimensions[2]);
            String S = reader.readLine();

            int row = lenPieceRow(filepath);
            List<InputPiece> allPieces = new ArrayList<>(); // LIST of Object

            int currentColor = 0;
            char[] firstPieceData = reader.readLine().replaceAll("\\s+$", "").toCharArray();
            InputPiece currentPiece = new InputPiece();
            currentPiece.setMatrix(firstPieceData);
            currentPiece.setColor(Colors.COLORS[currentColor]);
            allPieces.add(currentPiece);

            char[] temp = firstPieceData;
            for (int i = 1; i < row; i++) {
                char[] piece = reader.readLine().replaceAll("\\s+$", "").toCharArray();
                if (hasCommonElement(temp, piece)) {
                    currentPiece.setMatrix(piece);
                } else {
                    currentColor++;
                    currentPiece = new InputPiece();
                    currentPiece.setMatrix(piece);
                    currentPiece.setColor(Colors.COLORS[currentColor]);
                    allPieces.add(currentPiece);
                    temp = piece;
                }
            }

            result.put("N", N);
            result.put("M", M);
            result.put("P", P);
            result.put("S", S);
            result.put("totalPiece", row);
            result.put("allPieces", allPieces);

            return result;
        } catch (

        IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidPiece(char[] piece) {
        if (piece.length == 0) {
            return false;
        }

        int firstCharIdx = 0;
        for (int i = 0; i < piece.length; i++) {
            if (piece[i] != ' ') {
                firstCharIdx = i;
                break;
            }
        }

        char firstChar = piece[firstCharIdx];
        for (char c : piece) {
            if ((c != firstChar && c != ' ') || (c != ' ' && !String.valueOf(c).matches("[A-Z]"))) {
                System.out.println(c);
                return false;
            }
        }
        return true;
    }

    public static String returnThrowMessage(String filepath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            // baca baris pertama: N M P S
            String[] dimensions = reader.readLine().split(" ");

            if (dimensions.length < 3 ||
                    dimensions[0].trim().isEmpty() ||
                    dimensions[1].trim().isEmpty() ||
                    dimensions[2].trim().isEmpty()) {
                return "Error : Nilai N, M, dan P tidak boleh kosong.";
            }

            if (dimensions[0].trim().matches("\\d+") == false || dimensions[1].trim().matches("\\d+") == false
                    || dimensions[2].trim().matches("\\d+") == false) {
                return "Error : Nilai N, M, dan P harus berupa angka POSITIF.";
            }

            String S = reader.readLine();

            if (S.trim().isEmpty() || !S.trim().equals("DEFAULT") || S == null) {
                return "Error : String S tidak boleh kosong atau selain DEFAULT.";
            }

            int row = lenPieceRow(filepath);
            List<InputPiece> allPieces = new ArrayList<>();

            char[] firstPieceData = reader.readLine().toCharArray();
            if (firstPieceData == null || firstPieceData.length == 0) {
                return "Error : Data piece tidak boleh kosong.";
            }
            if (!isValidPiece(firstPieceData)) {
                return "Error: Piece tidak valid. Terdapat karakter yang berbeda.";
            }
            InputPiece currentPiece = new InputPiece();
            currentPiece.setMatrix(firstPieceData);
            allPieces.add(currentPiece);

            char[] temp = firstPieceData;
            for (int i = 1; i < row; i++) {
                char[] piece = reader.readLine().toCharArray();
                if (piece == null || piece.length == 0) {
                    return "Error : Data piece tidak lengkap.";
                }
                if (!isValidPiece(piece)) {
                    return "Error: Piece tidak valid. Terdapat karakter yang berbeda.";
                }
                if (hasCommonElement(temp, piece)) {
                    currentPiece.setMatrix(piece);
                } else {
                    currentPiece = new InputPiece();
                    currentPiece.setMatrix(piece);
                    allPieces.add(currentPiece);
                    temp = piece;
                }
            }

            for (int i = 0; i < allPieces.size(); i++) {
                for (int j = i + 1; j < allPieces.size(); j++) {

                    if (Arrays.equals(allPieces.get(i).getMatrix().get(0), allPieces.get(j).getMatrix().get(0))) {
                        return "Error: Terdapat piece yang sama.";
                    }
                }
            }

            return null;
        } catch (IOException e) {
            return "Error : File tidak ditemukan.";
        }
    }

    public static void printOutput(Map<String, Object> result) {
        int N = (int) result.get("N");
        int M = (int) result.get("M");
        int P = (int) result.get("P");
        String S = (String) result.get("S");

        @SuppressWarnings("unchecked")
        List<InputPiece> allPieces = (List<InputPiece>) result.get("allPieces");
        List<InputPiece> paddedPieces = padAllPieces(allPieces);

        System.out.println("N: " + N);
        System.out.println("M: " + M);
        System.out.println("P: " + P);
        System.out.println("S: " + S);

        System.out.println("\nAll Pieces:");
        int count = 0;
        for (InputPiece piece : paddedPieces) {
            System.out.println("Piece " + (count) + ":");
            List<char[]> matrix = piece.getMatrix(); // list matriks dari instance piece

            String output = matrix.stream()
                    .map(Arrays::toString)
                    .collect(Collectors.joining(",\n  ", "[\n  ", "\n]"));

            System.out.println(output);
            System.out.println();
            count++;
        }
    }

    public static List<InputPiece> padAllPieces(List<InputPiece> allPieces) {
        List<InputPiece> paddedPieces = new ArrayList<>();
        for (InputPiece piece : allPieces) {
            List<char[]> paddedMatrix = padMatrix(piece.getMatrix());
            InputPiece paddedPiece = new InputPiece();
            paddedPiece.matrix = paddedMatrix;
            paddedPieces.add(paddedPiece);
        }
        return paddedPieces;
    }

    public static InputPiece checkObject(List<InputPiece> allPieces, char Element) {
        for (InputPiece pieces : allPieces) {
            int length = pieces.getMatrix().size();
            for (int i = 0; i < length; i++) {
                char[] matrix = pieces.getMatrix().get(i);
                for (char c : matrix) {
                    if (c == Element) {
                        return pieces;
                    }
                }
            }
        }
        return null;
    }

    // bagian Board
    public static char[][] initializeBoard(int N, int M) {
        char[][] board = new char[N][M];

        for (int i = 0; i < N; i++) {
            Arrays.fill(board[i], '.');
        }

        return board;
    }

    public static boolean placePiece(char[][] board, List<char[]> piece, int x, int y) {
        int rows = piece.size();
        int cols = piece.get(0).length;

        // Cek potongan muat ga di board
        if (x + rows > board.length || y + cols > board[0].length) {
            return false;
        }

        // Cek ada konflik ?
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (piece.get(i)[j] != ' ' && board[x + i][y + j] != '.') {
                    return false;
                }
            }
        }

        // Tempatkan potongan di board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (piece.get(i)[j] != ' ') {
                    board[x + i][y + j] = piece.get(i)[j];
                }
            }
        }

        return true;
    }

    public static boolean fit(char[][] board) {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void removePiece(char[][] board, List<char[]> piece, int x, int y) {
        int r = piece.size();
        int c = piece.get(0).length;

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (piece.get(i)[j] != ' ') {
                    board[x + i][y + j] = '.';
                }
            }
        }
    }

    public static BufferedImage saveBoardAsImage(char[][] board, List<InputPiece> allPieces) {
        int rows = board.length;
        int cols = board[0].length;
        int cellSize = 50;
        int width = cols * cellSize;
        int height = rows * cellSize;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));

        for (int i = 0; i <= rows; i++) {
            g.drawLine(0, i * cellSize, width, i * cellSize);
        }
        for (int j = 0; j <= cols; j++) {
            g.drawLine(j * cellSize, 0, j * cellSize, height);
        }

        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char value = board[i][j];
                String text = String.valueOf(value);
                int textWidth = g.getFontMetrics().stringWidth(text);
                int textHeight = g.getFontMetrics().getAscent();
                int x = (j * cellSize) + (cellSize - textWidth) / 2;
                int y = (i * cellSize) + (cellSize + textHeight) / 2 - 5;

                InputPiece piece = checkObject(allPieces, value);

                g.setColor(AnsiConverter.colorFromANSI(piece.getColor()));
                g.drawString(text, x, y);
            }
        }

        g.dispose();
        return image;
    }

    public static File saveBoardAsTXT(char[][] board) throws IOException {
        File file = new File("output.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (char[] row : board) {
                writer.write(row);
                writer.newLine();
            }
        }

        return file;
    }
    // bagian solusi

    public static long attempts = 0;

    public static boolean solve(char[][] board, List<InputPiece> pieces, int currentPiece) {

        if (currentPiece == pieces.size()) {
            return fit(board); // penuhh
        }

        InputPiece piece = pieces.get(currentPiece);
        List<char[]> matrix = piece.getMatrix();
        int rows = board.length;
        int cols = board[0].length;

        // Cobain kordinat
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                @SuppressWarnings("unchecked")
                List<char[]>[] rotations = new List[] { matrix, mirrorHorizontal(matrix), rotate90(matrix),
                        mirrorHorizontal(rotate90(matrix)),
                        rotate180(matrix),
                        mirrorHorizontal(rotate180(matrix)),
                        rotate270(matrix),
                        mirrorHorizontal(rotate270(matrix)) };

                for (List<char[]> rotatedMatrix : rotations) {
                    attempts++;
                    if (placePiece(board, rotatedMatrix, i, j)) {
                        if (solve(board, pieces, currentPiece + 1)) {
                            return true;
                        }
                        removePiece(board, rotatedMatrix, i, j);
                    }
                }
            }
        }

        return false;
    }

    // bagian implementasi

    public static Map<String, Object> MainGUI(String filepath) throws IOException, InterruptedException {

        Map<String, Object> result = readInput(filepath);
        if (result != null) {
            int N = (int) result.get("N");
            int M = (int) result.get("M");
            @SuppressWarnings("unchecked")
            List<InputPiece> allPieces = (List<InputPiece>) result.get("allPieces");
            List<InputPiece> paddedPieces = padAllPieces(allPieces);

            char[][] board = initializeBoard(N, M);

            long startTime = System.currentTimeMillis();

            boolean isSolved = solve(board, paddedPieces, 0);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println(isSolved);

            if (!isSolved) {
                result.put("duration", duration);
                result.put("attempts", attempts);
                result.put("image", null);
                attempts = 0;
                return result;
            } else {

                BufferedImage image = saveBoardAsImage(board, allPieces); // Gambar + warnanya
                File file = saveBoardAsTXT(board);

                result.put("file", file);
                result.put("duration", duration);
                result.put("attempts", attempts);
                result.put("image", image);

                return result;
            }
        }
        return null;

    }

}

// keterangan :
// - InputPiece adalah class berisi List<char[]> matrix dari setiap piece
// - Piece adalah instance dari class InputPiece
// - allPieces adalah list<InputPiece> dari instance Piece
// - char[] harus di ubah ke List<char[]> agar compatible di class