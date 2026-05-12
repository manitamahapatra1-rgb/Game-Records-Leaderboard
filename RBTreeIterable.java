import java.beans.Transient;
import java.util.Iterator;
import java.util.Stack;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 */
public class RBTreeIterable<T extends Comparable<T>>
        extends RedBlackTree<T> implements IterableSortedCollection<T> {
            private Comparable<T> maximum = null; // stores the maximum for the iterator, or null if no maximum is set.
            private Comparable<T> minimum = null; //stores the minimum for the iterator, or null if no minimum is set.

    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     *
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    public void setIteratorMin(Comparable<T> min) {
        this.minimum = min;
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     *
     * @param max the maximum for iterators created for this tree, or null for no maximum
     */
    public void setIteratorMax(Comparable<T> max) {
        this.maximum = max;
    }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax. If setIteratorMin has not been called
     * before, or if it was called with a null argument, the iterator uses no minimum value
     * and starts with the lowest value that exists in the tree. If setIteratorMax has not been
     * called before, or if it was called with a null argument, the iterator uses no maximum
     * value and finishes with the highest value that exists in the tree.
     */
    public Iterator<T> iterator() {
       return new TreeIterator<T>(root, minimum, maximum);
    }


    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class TreeIterator<R extends Comparable<R>> implements Iterator<R> {

        // stores the start point (minimum) for the iterator
        Comparable<R> min = null;
        // stores the stop point (maximum) for the iterator
        Comparable<R> max = null;
        // stores the stack that keeps track of the inorder traversal
        Stack<BinaryNode<R>> stack = null;

        /**
         * Constructor for a new iterator if the tree with root as its root node, and
         * min as the start (minimum) value (or null if no start value) and max as the
         * stop (maximum) value (or null if no stop value) of the new iterator.
         * Time complexity should be O(log n).
         *
         * @param root root node of the tree to traverse
         * @param min  the minimum value that the iterator will return
         * @param max  the maximum value that the iterator will return
         */
        public TreeIterator(BinaryNode<R> root, Comparable<R> min, Comparable<R> max) {
            this.min = min;
            this.max = max;
            this.stack = new Stack<>();

            updateStack(root);

        }

        /**
         * Helper method for initializing and updating the stack. This method both
         * - finds the next data value stored in the tree (or subtree) that is between
         * start(minimum) and stop(maximum) point (including start and stop points
         * themselves), and
         * - builds up the stack of ancestor nodes that contain values between
         * start(minimum) and stop(maximum) values (including start and stop values
         * themselves) so that those nodes can be visited in the future.
         *
         * @param node the root node of the subtree to process
         */
        private void updateStack(BinaryNode<R> node) {

            ///base case
            if (node == null) {
                return;
            }

            /// recursive case1
            if (this.min != null && this.min.compareTo(node.getData()) > 0) {
                updateStack(node.getRight());
            } else {//if (this.min != null && this.min.compareTo(node.getData())  <= 0) {
                stack.push(node);
                updateStack(node.getLeft());
            }
        }

        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         */
        public boolean hasNext() {
            if (stack.isEmpty()){
                return false;
            }
            if (max != null && this.max.compareTo(stack.peek().getData()) < 0) {
                return false;
            }
            return true;
        }

        /**
         * Returns the next value of the iterator.
         * Amortized time complexity should be O(1).
         * Worst case time complexity should be O(log n).
         * Do not implement this method by linearly walking through the
         * entire tree from the smallest element until the start bound is reached.
         * That process should occur only once during construction of the
         * iterator object.
         *
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        public R next() {
            BinaryNode<R> popped;

             if (stack.isEmpty()) {
                throw new NoSuchElementException("stack is empty");
             }

             popped = stack.pop();

             if (max != null && this.max.compareTo(popped.getData()) < 0) {
                throw new NoSuchElementException("popped node's value is greater than the max");
             }

             updateStack(popped.getRight());

            return popped.getData();
        }
    }

    @Test ///test with integers and no start or stop
    public void testRBTIterableInteger(){
    RBTreeIterable<Integer> tree1 = new RBTreeIterable<>();
    tree1.insert(3);
    tree1.insert(16);
    tree1.insert(4);
    tree1.insert(17);
    tree1.insert(1);
    tree1.insert(5);
    tree1.insert(18);

    Iterator<Integer> iterator = tree1.iterator();

    assertEquals(1, iterator.next());
    assertEquals(3, iterator.next());
    assertEquals(4, iterator.next());
    assertEquals(5, iterator.next());
    assertEquals(16, iterator.next());
    assertEquals(17, iterator.next());
    assertEquals(18, iterator.next());

    assertFalse(iterator.hasNext());
    }

    @Test // test with integers and start and stop
    public void testRBTIterableStringAndStartAndStop(){
    RBTreeIterable<String> tree2 = new RBTreeIterable<>();
    tree2.insert("apple");
    tree2.insert("orange");
    tree2.insert("banana");
    tree2.insert("pear");
    tree2.insert("avocado");
    tree2.insert("grape");
    tree2.insert("blueberry");

    tree2.setIteratorMin("apple");
    tree2.setIteratorMax("banana");
    Iterator<String> iterator = tree2.iterator();

    assertEquals("apple", iterator.next());
    assertEquals("avocado", iterator.next());
    assertEquals("banana", iterator.next());


    assertFalse(iterator.hasNext());
    }

    @Test //test with duplicates and just a start point
    public void testRBTIterableDuplicateAndJustStart(){
    RBTreeIterable<Integer> tree3 = new RBTreeIterable<>();
    tree3.insert(9);
    tree3.insert(1);
    tree3.insert(7);
    tree3.insert(6);
    tree3.insert(6);
    tree3.insert(0);

    tree3.setIteratorMin(1);
    Iterator<Integer> iterator = tree3.iterator();

    assertEquals(1, iterator.next());
    assertEquals(6, iterator.next());
    assertEquals(6, iterator.next());
    assertEquals(7, iterator.next());
    assertEquals(9, iterator.next());

    assertFalse(iterator.hasNext());

    }


    @Test //test with just a end point
    public void testRBTIterableJustEnd(){
    RBTreeIterable<Integer> tree4 = new RBTreeIterable<>();
    tree4.insert(2);
    tree4.insert(4);
    tree4.insert(6);
    tree4.insert(8);
    tree4.insert(10);
    tree4.insert(12);

    tree4.setIteratorMax(10);
    Iterator<Integer> iterator = tree4.iterator();

    assertEquals(2, iterator.next());
    assertEquals(4, iterator.next());
    assertEquals(6, iterator.next());
    assertEquals(8, iterator.next());
    assertEquals(10, iterator.next());

    assertFalse(iterator.hasNext());
    }
}