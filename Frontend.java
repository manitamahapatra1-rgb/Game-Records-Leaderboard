// Sam Pallan CS 400: P103.RoleCode

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * CS400 Project 1: P103.RoleCode
 *
 * reads commands rom the user via scanner and then
 * delegates work to the backend before printing the results.
 */
public class Frontend implements FrontendInterface {

    private final Scanner in;
    private final BackendInterface backend;

    /**
     * the constructor, i dunno what else to say tbh.
     *
     * @param in      scanner to read user input from
     * @param backend backend implementation to delegate to
     */
    public Frontend(Scanner in, BackendInterface backend) {
        if (in == null || backend == null) {
            throw new NullPointerException("Scanner and BackendInterface cannot be null.");
        }
        this.in = in;
        this.backend = backend;
    }

    @Override
    public void runCommandLoop() {
        showCommandInstructions();

        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) {
                // End of input stream ==> quiting here
                break;
            }

            String line = in.nextLine().trim();
            if (line.equalsIgnoreCase("quit")) {
                // handled not by this program => not quitting here
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            try {
                processSingleCommand(line);
            } catch (Exception e) {
                System.out.println("An unexpected error has occured.");
            }
        }
    }

    @Override
    public void showCommandInstructions() {
        System.out.println(
            "Game Records Leaderboard - Commands:\n" +
            "  submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME\n" +
            "    - example: submit Sam NORTH_AMERICA 1200 15 42 001:23:45\n" +
            "  submit multiple FILEPATH\n" +
            "    - example: submit multiple records.csv\n" +
            "  level MAX\n" +
            "    - example: level 100\n" +
            "  level MIN to MAX\n" +
            "    - example: level 10 to 100\n" +
            "  time TIME\n" +
            "    - example: time 010:00:00\n" +
            "  show MAX_COUNT\n" +
            "    - example: show 5\n" +
            "  show most collectables\n" +
            "  help\n" +
            "  quit"
        );
    }

    @Override
    public void processSingleCommand(String command) {
        if (command == null) {
            System.out.println("Invalid input (null)");
            return;
        }

        String trimmed = command.trim();
        if (trimmed.isEmpty()) {
            System.out.println("Invalid input (empty)");
            return;
        }

        // split on whitespace cuz oversized lines
        String[] parts = trimmed.split("\\s+");
        String first = parts[0].toLowerCase();

        switch (first) {
            case "help":
                showCommandInstructions();
                return;

            case "submit":
                handleSubmit(trimmed, parts);
                return;

            case "level":
                handleLevel(parts);
                return;

            case "time":
                handleTime(parts);
                return;

            case "show":
                handleShow(parts);
                return;

            default:
                System.out.println("Invalid command. Type 'help' for options.");
        }
    }


    // Command handlers

    private void handleSubmit(String fullLine, String[] parts) {
        // submit multiple FILEPATH  (filepath could contain spaces)
        if (parts.length >= 2 && parts[1].equalsIgnoreCase("multiple")) {
            String prefix = "submit multiple";
            String filepath = fullLine.substring(prefix.length()).trim();
            if (filepath.isEmpty()) {
                System.out.println("Invalid input. Example: submit multiple records.csv");
                return;
            }
            try {
                backend.readData(filepath);
                System.out.println("Loaded records from: " + filepath);
            } catch (IOException e) {
                System.out.println("Could not read file.");
            }
            return;
        }

        // submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME
        if (parts.length != 7) {
            System.out.println("Invalid input.");
            System.out.println("  submit NAME CONTINENT SCORE COLLECTABLES LEVEL COMPLETION_TIME");
            return;
        }

        String name = parts[1];

        GameRecord.Continent continent;
        try {
            // accounting for case sensitivity
            String continentToken = parts[2].toUpperCase();
            continent = GameRecord.Continent.valueOf(continentToken);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: invalid CONTINENT");
            System.out.println("  Try one of: " + java.util.Arrays.toString(GameRecord.Continent.values()));
            return;
        }

        Integer score = parseInt(parts[3], "SCORE");
        Integer collectables = parseInt(parts[4], "COLLECTABLES");
        Integer level = parseInt(parts[5], "LEVEL");
        if (score == null || collectables == null || level == null) return;

        String time = parts[6];
        if (!isValidTime(time)) {
            System.out.println("Error: COMPLETION_TIME must look like hhh:mm:ss (example 001:23:45). Got: " + time);
            return;
        }

        GameRecord record = new GameRecord(name, continent, score, collectables, level, time);
        backend.addRecord(record);
        System.out.println("Submitted record for " + name + ".");
    }

    private void handleLevel(String[] parts) {
        // level MAX
        // level MIN to MAX
        if (parts.length == 2) {
            Integer max = parseInt(parts[1], "MAX");
            if (max == null) return;
            backend.getAndSetRange(null, max);
            System.out.println("Level range set: <= " + max);
            return;
        }

        if (parts.length == 4 && parts[2].equalsIgnoreCase("to")) {
            Integer min = parseInt(parts[1], "MIN");
            Integer max = parseInt(parts[3], "MAX");
            if (min == null || max == null) return;
            backend.getAndSetRange(min, max);
            System.out.println("Level range set: " + min + " to " + max);
            return;
        }

        System.out.println("Error: invalid level command.");
        System.out.println("  level MAX");
        System.out.println("  level MIN to MAX");
    }

    private void handleTime(String[] parts) {
        // time TIME
        if (parts.length != 2) {
            System.out.println("Error: time requires exactly 1 argument.");
            System.out.println("  time hhh:mm:ss");
            return;
        }
        String time = parts[1];
        if (!isValidTime(time)) {
            System.out.println("Error: TIME must look like hhh:mm:ss (example 010:00:00). Got: " + time);
            return;
        }
        backend.applyAndSetFilter(time);
        System.out.println("Time filter set: < " + time);
    }

    private void handleShow(String[] parts) {
        // show MAX_COUNT
        // show most collectables
        if (parts.length == 2) {
            Integer maxCount = parseInt(parts[1], "MAX_COUNT");
            if (maxCount == null) return;
            if (maxCount < 0) {
                System.out.println("Error: MAX_COUNT must be non-negative.");
                return;
            }

            List<String> names = backend.getTopTen();
            printNames(names, maxCount);
            return;
        }

        if (parts.length == 3 && parts[1].equalsIgnoreCase("most") && parts[2].equalsIgnoreCase("collectables")) {
            List<String> names = backend.getTopTen();
            printNames(names, 10);
            return;
        }

        System.out.println("Error: invalid show command.");
        System.out.println("  show MAX_COUNT");
        System.out.println("  show most collectables");
    }

    //  other helpers

    private Integer parseInt(String token, String label) {
        try {
            return Integer.valueOf(token);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + label + " must be an integer. Got: " + token);
            return null;
        }
    }

    private boolean isValidTime(String time) {
        // validation (1-3 digits : 2 digits : 2 digits)
        if (time == null || !time.matches("\\d{1,3}:\\d{2}:\\d{2}")) {
            return false;
        }

        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        return minutes >= 0 && minutes <= 59 && seconds >= 0 && seconds <= 59;
    }

    private void printNames(List<String> names, int limit) {
        System.out.println("Records:");
        if (names == null || names.isEmpty()) {
            System.out.println("  (none)");
            return;
        }
        int count = Math.min(limit, names.size());
        for (int i = 0; i < count; i++) {
            System.out.println("  " + names.get(i));
        }
    }
}