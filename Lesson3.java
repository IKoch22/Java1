package lesson3p1;

import java.util.Random;
import java.util.Scanner;

public class Lesson3 {

    final int SIZE = 5;             // Размер Игрового поля SIZExSIZE
    final int SIZE_LINE_WIN = 4;    // Размер выигрышной линии
    final char DOT_X = 'x';
    final char DOT_O = 'o';
    final char DOT_EMPTY = '.';
    char[][] map;
    int winX, winY;                 // Для нахождения потенциально выигрышного хода
    Random random;
    Scanner scanner;

    public static void main(String[] args) {
        new Lesson3().game();
    }


    void game() {
        initMap();
        random = new Random();
        scanner = new Scanner(System.in);

        while (true) {

            humanTurn();
            if (checkWinNew(DOT_X)) {
                System.out.println("YOU WON!");
                break;
            }


            if (isMapFull()) {
                System.out.println("Sorry, DRAW!");
                break;
            }

            aiTurn();
            printMap();
            if (checkWinNew(DOT_O)) {
                System.out.println("AI WON!");
                break;
            }

            if (isMapFull()) {
                System.out.println("Sorry, DRAW!");
                break;
            }
        }
        printMap();
        System.out.println("GAME OVER");

    }


    void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                map[i][j] = DOT_EMPTY;
    }

    void printMap() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)
                System.out.print(map[i][j] + " ");
            System.out.println();
        }
    }

    void humanTurn() {
        int x, y;
        do {
            System.out.println("Enter X and Y (1.." + SIZE + "):");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y));
        map[y][x] = DOT_X;
    }

    void aiTurn() {

        int x, y;

        // Проверка "искусственного интеллекта" на один ход до победы

        if (checkWinTurn(DOT_X) && isCellValid(winX, winY)) {
            map[winY][winX] = DOT_O;
        }
        else {
            do {
                x = random.nextInt(SIZE);
                y = random.nextInt(SIZE);
            } while (!isCellValid(x, y));
            map[y][x] = DOT_O;

        }

    }

    boolean checkWinNew(char dt) {
        // Переменные-счетчики для горизонтальных и вертикальных линий
        int checkHorizontalCounter, checkVerticalCounter;
        // Переменные-счетчики для диагоналей, которые выше или совпадают с основной и побочной диагоналями
        int checkMainDiagonalCounter, checkSideDiagonalCounter;
        // Переменные-счетчики для диагоналей, которые ниже основной и побочной диагоналей
        int checkBelowMainDiagonalCounter, checkBelowSideDiagonalCounter;

        for (int i = 0; i < SIZE; i++) {

            checkHorizontalCounter = 0;
            checkVerticalCounter   = 0;
            checkMainDiagonalCounter   = 0;
            checkSideDiagonalCounter   = 0;
            checkBelowMainDiagonalCounter   = 0;
            checkBelowSideDiagonalCounter   = 0;
            for (int j = 0; j < SIZE; j++) {

                // Проверка для диагоналей, которые выше или совпадают с основной и побочной диагоналями
                if (j+i < SIZE) {
                    if (map[j][j+i] == dt)                  { checkMainDiagonalCounter++;   }
                    else if (checkMainDiagonalCounter >0)   { checkMainDiagonalCounter = 0; }

                    if (map[SIZE-j-1][j+i] == dt)           { checkSideDiagonalCounter++;   }
                    else if (checkSideDiagonalCounter >0)   { checkSideDiagonalCounter = 0; }

                    if (checkMainDiagonalCounter == SIZE_LINE_WIN || checkSideDiagonalCounter == SIZE_LINE_WIN) return true;
                }
                else {
                    checkMainDiagonalCounter   = 0;
                    checkSideDiagonalCounter   = 0;
                }

                // Проверка для диагоналей, которые ниже основной и побочной диагоналей
                if (j-i >=0) {
                    if (map[j][j-i] == dt)                      { checkBelowMainDiagonalCounter++;   }
                    else if (checkBelowMainDiagonalCounter >0)  { checkBelowMainDiagonalCounter = 0; }

                    if (map[SIZE-j-1][j-i] == dt)               { checkBelowSideDiagonalCounter++;   }
                    else if (checkBelowSideDiagonalCounter >0)  { checkBelowSideDiagonalCounter = 0; }

                    if (checkBelowMainDiagonalCounter == SIZE_LINE_WIN || checkBelowSideDiagonalCounter == SIZE_LINE_WIN) return true;
                }
                else {
                    checkBelowMainDiagonalCounter   = 0;
                    checkBelowSideDiagonalCounter   = 0;
                }

                // Проверка для горизонтальных и вертикальных линий
                if (map[j][i] == dt)                { checkHorizontalCounter++;   }
                else if (checkHorizontalCounter >0) { checkHorizontalCounter = 0; }

                if (map[i][j] == dt)                { checkVerticalCounter++;     }
                else if (checkVerticalCounter >0)   { checkVerticalCounter = 0;   }

                if (checkHorizontalCounter == SIZE_LINE_WIN || checkVerticalCounter == SIZE_LINE_WIN)   return true;
            }

        }


        return false;
    }


    boolean checkWin(char dt) {
        boolean checkHorizontal;
        boolean checkVertical;
        boolean checkMainDiagonal, checkSideDiagonal;

        checkMainDiagonal = true;
        checkSideDiagonal = true;
        for (int i = 0; i < SIZE; i++) {
            if (map[i][i] != dt)         { checkMainDiagonal = false; }
            if (map[SIZE-i-1][i] != dt)  { checkSideDiagonal = false; }

            checkHorizontal = true;
            checkVertical = true;
            for (int j = 0; j < SIZE; j++) {
                if (map[j][i] != dt)     { checkHorizontal   = false; }
                if (map[i][j] != dt)     { checkVertical     = false; }
            }
            if (checkHorizontal || checkVertical)   return true;

        }
        if (checkMainDiagonal || checkSideDiagonal) return true;


        return false;
    }

    boolean isMapFull() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (map[i][j] == DOT_EMPTY)
                    return false;
        return true;
    }

    boolean isCellValid(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE)
            return false;
        return map[y][x] == DOT_EMPTY;
    }


    //Доработать искусственный интеллект, чтобы он мог блокировать ходы игрока.
    boolean checkWinTurn(char dt) {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isCellValid(i, j)) {
                    map[j][i]=dt;        // Временно отметим следующий ход и проверим победит ли он в этом случае
                    if (checkWinNew(dt)) {
                        // Нашли потенциально выигрышный ход
                        winX = i;
                        winY = j;
                        map[j][i]=DOT_EMPTY;    // Обязательно отменяем временный ход
                        return true;
                    }
                    map[j][i]=DOT_EMPTY;    // Обязательно отменяем временный ход
                }
            }
        }


        return false;
    }

}