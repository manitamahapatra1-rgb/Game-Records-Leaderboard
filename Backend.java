/*
* Author: Manita Mahapatra
* Email: mmahapatra@wisc.edu
* Assignment: Program 3
* Course: COMP SCI 400
*
*/


import java.io.IOException;
import java.util.List;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Backend implements BackendInterface {

    private IterableSortedCollection<GameRecord> tree;
    private String timeFilter;
    private Integer lowRange;
    private Integer highRange;

    public Backend(IterableSortedCollection<GameRecord> tree) {
        this.tree = tree;
    }

    /** Add and stores the specified record to the tree. Don't forget that the GameRecord
     *  must have the Comparator set. This will be used to store these records in order within your
     *  tree, and to retrieve them by level range in the getRange method.
     * @param record the game record to add
     */
    public void addRecord(GameRecord record){
        tree.insert(record);
    }

    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided records.csv, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other record qualities.
     * After reading records from the file, the records are inserted into
     * the tree passed to this backend's constructor. This will be used to store these records in order within your
     * tree, and to retrieve them by level range in the getRange method.
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    public void readData(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists() || !file.canRead()) {
            throw new IOException("trouble finding/reading file");
        }
        Scanner fileScanner = new Scanner(file);

        if (!fileScanner.hasNextLine()) {
            fileScanner.close();
            return;
        }

        //takes the first line of the csv which has all categories
        String firstLine = fileScanner.nextLine();

        try{
        //takes the categories of each column (split by ,)
        String[] categories = firstLine.split(",");


        //create vars to store the index of each category taken from line one
        int nameIndex = -1;
        int continentIndex = -1;
        int scoreIndex = -1;
        int collectablesIndex = -1;
        int levelIndex = -1;
        int completionTimeIndex = -1;

        for (int i = 0; i < categories.length; i++) {
            String category = categories[i].trim();

            if (category.equals("name")) {
                nameIndex = i; //stores this column as name category's index
            }

            if (category.equals("continent")) {
                continentIndex = i; //stores this column as continent category's index
            }

            if (category.equals("score")) {
                scoreIndex = i; //stores this column as score category's index
            }

            if (category.equals("collectables")) {
                collectablesIndex = i; //stores this column as collectables category's index
            }

            if (category.equals("level")) {
                levelIndex = i; //stores this column as level category's index
            }

            if (category.equals("completion_time")) {
                completionTimeIndex = i; //stores this completion time as name category's index
            }
        }

    if (nameIndex == -1 || continentIndex == -1 || scoreIndex == -1 ||
        collectablesIndex == -1 || levelIndex == -1 || completionTimeIndex == -1) {
        //fileScanner.close();
        throw new IOException("CSV file is missing one or more required columns");
    }

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();

            String[] parts = line.split(",");

            //gets each line's categories and converts them to the correct
            try {
            String name = parts[nameIndex];
            GameRecord.Continent continent = GameRecord.Continent.valueOf(parts[continentIndex]);
            int score = Integer.parseInt(parts[scoreIndex]);
            int collectables = Integer.parseInt(parts[collectablesIndex]);
            int level = Integer.parseInt(parts[levelIndex]);
            String completionTime = parts[completionTimeIndex];

            GameRecord record = new GameRecord(name, continent, score, collectables, level, completionTime);

            addRecord(record);
            } catch (Exception e) {
                System.out.println("Skipping the incorrectly formatted row.");
            }
        }
        } finally {
                fileScanner.close();
        }

        }



    /**
     * Retrieves a list of names from the tree passed to the constructor.
     * The records should be ordered by the record's level, and fall within
     * the specified range of level values.  This level range will
     * also be used by future calls to filterRecords and getTopTen.
     *
     * If a completion time filter has been set using the filterRecords method
     * below, then only records that pass that filter should be included in the
     * list of names returned by this method.
     *
     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a
     * null argument for the high parameter means that there is no maximum
     * level to include in the returned list.
     *
     * @param low is the minimum level of records in the returned list
     * @param high is the maximum level of records in the returned list
     * @return List of names for all records from low to high that pass any
     *     set filter, or an empty list when no such records can be found
     */
    public List<String> getAndSetRange(Integer low, Integer high){
        List<String> names = new ArrayList<String>();

        this.lowRange = low;
        this.highRange = high;

        for (GameRecord record : tree) {
            //check if in range
            boolean insideRange = true;

            if (low != null && record.getLevel() < low) {
                insideRange = false;
            }

            if (high != null && record.getLevel() > high) {
                insideRange = false;
            }

            if (insideRange == true) {
                if (timeFilter == null) {
                    names.add(record.getName());
                } else {
                    if (record.getCompletionTime().compareTo(timeFilter) < 0) {
                        names.add(record.getName());
                    }
                }

            }

        }

        return names;

    }


    /**
     * Retrieves a list of record names that have a completion time that is smaller than the specified
     * completion time.
     * Similar to the getRange method: this list of record names should be ordered by the records'
     * level, and should only include records that fall within the specified
     * range of level values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high level bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and getTopTen methods.
     *
     * When null is passed as the completion time to this method, then no
     * completion time filter should be used.  This clears the filter.
     *
     * @param completion time filters returned record names to only include records that
     *     have a completion time that are smaller than the specified value. Formatted hhh:mm:ss
     * @return List of names for records that meet this filter requirement and
     *     are within any previously set level range, or an empty list
     *     when no such records can be found
     */
    public List<String> applyAndSetFilter(String time){

        if (time == null) {
            timeFilter = null;
        } else {
            timeFilter = time;
        }

        List<String> names1 = new ArrayList<String>();

        for (GameRecord record : tree) {
        //reset insideRange
        boolean insideRange = true;

            if (lowRange != null && record.getLevel() < lowRange) {
                insideRange = false;
            }

            if (highRange != null && record.getLevel() > highRange) {
                insideRange = false;
            }

            if (insideRange == true) {
                if (time == null || record.getCompletionTime().compareTo(time) < 0){
                    names1.add(record.getName());
                }

            }

        }
        return names1;
    }


    /**
     * This method returns a list of record names representing the top
     * ten records with the most collectables that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to filteredRecords.  The order of the record names
     * in this returned list is up to you.
     *
     * If fewer than ten such records exist, return all of them.  And return an
     * empty list when there are no such records.
     *
     * @return List of ten record names with the most collectables
     */
    public List<String> getTopTen(){

        // List<String> top10 = new ArrayList<String>();
        List<GameRecord> top10Games = new ArrayList<>();
        List<String> top10Names = new ArrayList<>();


        for (GameRecord record : tree) {
            boolean insideRange = true;

            /// check if inside range
            if (lowRange != null && record.getLevel() < lowRange) {
                insideRange = false;
            }

            if (highRange != null && record.getLevel() > highRange) {
                insideRange = false;
            }

            //check if inside range and passes time filter
            if (insideRange == true) {
                if (timeFilter == null || record.getCompletionTime().compareTo(timeFilter) < 0) {

                    //adds 10 into the arraylist first
                    if (top10Games.size() < 10){
                        top10Games.add(record);
                    } else {
                        int minIndex = 0;

                        for (int i = 1; i < top10Games.size(); i++) {
                            if (top10Games.get(i).getCollectables() < top10Games.get(minIndex).getCollectables()) {
                                minIndex = i;
                            }

                        }

                        if (record.getCollectables() > top10Games.get(minIndex).getCollectables()) {
                                top10Games.set(minIndex, record);
                            }
                    }

                }
            }
        }

        //adds the names to an array list
        for (int i = 0; i < top10Games.size(); i++){
            top10Names.add(top10Games.get(i).getName());
        }

        return top10Names;

    }


    }