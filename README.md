## Overview
IQ Puzzler Pro is a program designed to solve puzzles by strategically placing pieces onto a board using a recursive backtracking algorithm. It reads an input file that specifies the board dimensions and the individual puzzle pieces, then systematically explores all possible placements by generating various rotations and mirror images of each piece. The program places a piece at a candidate location and recursively attempts to fill the board; if a dead-end is reached, it backtracks to try alternative configurations. 

## Requirements and Instalation

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
