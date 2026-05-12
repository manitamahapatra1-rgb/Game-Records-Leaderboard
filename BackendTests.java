/*
* Author: Manita Mahapatra
* Email: mmahapatra@wisc.edu
* Assignment: Program 3
* Course: COMP SCI 400
*
*/
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.List;

/**
 * BackendTests - JUnit 5 Test Suite for Backend Class
 * Test Backend class like  adding records, loading from the CSV,
 * filtering by level range, filtering by completion time, and getting top 10 records.
  */
public class BackendTests {

    /**
     * Test addRecord() and getAndSetRange()
     * Checks that records are added properly and can be found in a level range
     */
    @Test
    public void roleTest1() {
        IterableSortedCollection<GameRecord> tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

    //records with different levels
    GameRecord rec1 = new GameRecord("Player1", GameRecord.Continent.NORTH_AMERICA, 40000, 150, 250, "100:30:45");
    GameRecord rec2 = new GameRecord("Player2", GameRecord.Continent.EUROPE, 45000, 180, 350, "120:15:30");
    GameRecord rec3 = new GameRecord("Player3", GameRecord.Continent.ASIA, 50000, 200, 450, "150:45:00");

        // Add records
        backend.addRecord(rec1);
        backend.addRecord(rec2);
        backend.addRecord(rec3);

        // Get records in level range [200, 400]
        // rec2 with level 350 should be in this range
        List<String> result = backend.getAndSetRange(200, 400);
            assertTrue(result != null);
            assertTrue(result.size() > 0);
        }

    /**
     * tests readData(), getAndSetRange() with null bounds, and applyAndSetFilter()
     * and checks file loading, unbounded ranges, and time-based filtering
     */
    @Test
    public void roleTest2() {
        IterableSortedCollection<GameRecord> tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        // Test readData - should handle file loading
        try {
            backend.readData("records.csv");
        } catch (IOException e) {
            // Expected if file doesn't exist
        }

            // null high test
            List<String> results1 = backend.getAndSetRange(100, null);
            assertTrue(results1 != null);

        // null low test
        List<String> results2 = backend.getAndSetRange(null, 500);
        assertTrue(results2 != null);

        // completion time filter test
        List<String> filtered = backend.applyAndSetFilter("100:00:00");
        assertTrue(filtered != null);

        // clear filter test
        List<String> unfiltered = backend.applyAndSetFilter(null);
        assertTrue(unfiltered != null);
    }

    /**
     * Tests getTopTen() with level range and time filters
     * Makes sure top 10 works with both filters and does not return more than 10 records
     */
    @Test
    public void roleTest3() {
        IterableSortedCollection<GameRecord> tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        // test records with various collectables
        GameRecord record1 = new GameRecord("person1", GameRecord.Continent.NORTH_AMERICA, 60000, 250, 300, "180:00:00");
        GameRecord record2 = new GameRecord("person2", GameRecord.Continent.EUROPE, 65000, 280, 350, "200:30:00");
        GameRecord record3 = new GameRecord("person3", GameRecord.Continent.ASIA, 70000, 310, 400, "220:15:00");
        GameRecord record4 = new GameRecord("person4", GameRecord.Continent.AFRICA, 75000, 340, 450, "240:45:00");
        GameRecord record5 = new GameRecord("person5", GameRecord.Continent.AUSTRALIA, 80000, 370, 500, "260:30:00");

        backend.addRecord(record1);
        backend.addRecord(record2);
        backend.addRecord(record3);
        backend.addRecord(record4);
        backend.addRecord(record5);

        // Set range an filter, get top 10
        backend.getAndSetRange(250, 500);
        backend.applyAndSetFilter("250:00:00");
        List<String> topTen = backend.getTopTen();

        assertTrue(topTen != null);
        assertTrue(topTen.size() <= 10);

        // Test without range constrictsions
        backend.getAndSetRange(null, null);
        List<String> allTopTen = backend.getTopTen();

        assertTrue(allTopTen != null);
        assertTrue(allTopTen.size() <= 10);
    }
    /**
    * Tests getAndSetRange method and makes sure that the correct records were loaded and returned.
    */
    @Test
    public void integrationTest1() throws IOException {
        //create RBTreeIterable
        RBTreeIterable<GameRecord> tree = new RBTreeIterable<>();
        Backend backend = new Backend(tree);

        //load the records csv file into backend
        backend.readData("records.csv");

        //set a level range and get results
        List<String> results = backend.getAndSetRange(1,100);

        //make sure records were loaded and returned within
        assertTrue(results != null);
        assertTrue(results.size() > 0);
    }

    /**
    * Tests applyAndSetFilter method, checks for non-null value when filter is set and checks for size
    */
    @Test
    public void integrationTest2() throws IOException{
        RBTreeIterable<GameRecord> tree = new RBTreeIterable<>();
        Backend backend = new Backend(tree);

        //load records from csv
        backend.readData("records.csv");

        //set a level range first
        backend.getAndSetRange(1,100);

        //save set filter into list
        List<String> filtered = backend.applyAndSetFilter("010:00:00");

        //check that list is non-null
        assertTrue(filtered != null);

        //save set filter with null value
        List<String> unfiltered = backend.applyAndSetFilter(null);
        assertTrue(unfiltered.size() >= filtered.size());
    }



    /**
    * Tests getTopTen method
    */
    @Test
    public void integrationTest3() throws IOException{
        //Create RBTIterable
        RBTreeIterable<GameRecord> tree = new RBTreeIterable<>();
        Backend backend = new Backend(tree);

        //load records from csv
        backend.readData("records.csv");

        //set level and range
        backend.getAndSetRange(1,100);
        //get topTen values and store into list
        List<String> topTen = backend.getTopTen();

        //check that the list in non-null
        assertTrue(topTen != null);
        //make sure that topTen list is at or below ten records
        assertTrue(topTen.size() <= 10);
    }

    /**
    * Checks getAndSetRange with null parameter and checks that size is correct
    */
    @Test
    public void integrationTest4() throws IOException{
        //create RBTIterable
        RBTreeIterable<GameRecord> tree = new RBTreeIterable<>();
        Backend backend = new Backend(tree);

        //load records from csv
        backend.readData("records.csv");

        //get all records by creating null end for range
        List<String> allRecords = backend.getAndSetRange(1, null);

        //get only ten records by maxing at 10 records for the range
        List<String> smallRange = backend.getAndSetRange(1, 10);

        ////check that the list with allrecords is greater in size than the one with just 10
        assertTrue(smallRange.size() <= allRecords.size());
    }



}