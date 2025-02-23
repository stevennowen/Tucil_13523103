## Overview
IQ Puzzler Pro is a program designed to solve puzzles by strategically placing pieces onto a board using a recursive backtracking algorithm. It reads an input file that specifies the board dimensions and the individual puzzle pieces, then systematically explores all possible placements by generating various rotations and mirror images of each piece. The program places a piece at a candidate location and recursively attempts to fill the board; if a dead-end is reached, it backtracks to try alternative configurations. 

## Requirements and Instalation

The workspace contains two folders by default, where:

- `Requirements`:
  - JDK version 21 or Higher
  - Latest javaFx 
- `Installation`:
  - Download or Clone this Repository
  - SetUp your JDK and javafx environment

## How to Run Program
- Make sure your path is on the Tucil-13523103 folder.
- `Compiling`:
  - javac --module-path "Your Absolute Path to javafx-sdk-lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -d bin src/GUI/*.java src/solver/*.java
  - Example : javac --module-path "C:\Users\Owen\OneDrive\Documents\Owen\STIMA\javafx-sdk-23.0.2\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -d bin src/GUI/*.java src/solver/*.java
- `Running`:
  - java --module-path "Your Absolute Path to javafx-sdk-lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -cp bin GUI.MainApp
  - Example : java --module-path "Your Absolute Path to javafx-sdk-lib" --add-modules javafx.controls,javafx.fxml,javafx.swing -cp bin GUI.MainApp



