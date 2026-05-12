import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T>{
    /**
     * Empty Red Black Tree Constructor
     */
    public RedBlackTree(){
        super();
    }

    /**
     * Inserts a new data value into the sorted collection.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null, we do not allow
     * null values to be stored within a SortedCollection
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null data");
        }
        RedBlackNode<T> newNode = new RedBlackNode<T>(data);

        if (this.root == null) {
            this.root = newNode;
        } else {
            insertHelper(newNode, this.root);
            ensureRedProperty(newNode);
        }

        //makes sure root is black
        RedBlackNode<T> rootNode = (RedBlackNode<T>) root;
        if (rootNode.isBlackNode() == false) {
            rootNode.flipColor();
        }

    }

    /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If this is not the case, the method terminates without
     * making any changes to the tree. If a red property violation is detected, then
     * the method repairs this violation and any additional red property violations
     * that are generated as a result of the applied repair operation.
     * Using this method might cause nodes with a value equal to the value of one of
     * their ancestors to appear within the left and the right subtree of that ancestor,
     * even if the original insertion procedure consistently inserts such nodes into only
     * the left or the right subtree. But it will preserve the ordering of nodes within
     * the tree.
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    protected void ensureRedProperty(RedBlackNode<T> newNode) {
        //red property violations

        boolean auntIsLeftChild;

        //newnode is null or is the root
        if (newNode == null || newNode.getUp() == null) {
            return;
        }

        RedBlackNode<T> parent = newNode.getUp();
        if (parent.isBlackNode()) { // get parent node (if black, do nothing)
            return;
        }

        RedBlackNode<T> grandparent = parent.getUp();

         // violation: parent node is red, meaning there is red child and parent
            RedBlackNode<T> aunt; ///
            if (grandparent.getLeft() == parent){
                //parent is left child, aunt is right child
                aunt = grandparent.getRight();
                auntIsLeftChild = false;
            } else { // aunt is on the left
                aunt = grandparent.getLeft();
                auntIsLeftChild = true;
            }

            //check aunt's color
            if (aunt!= null && aunt.isBlackNode() == false){ //aunt is red
                parent.flipColor(); //make black parent > red
                aunt.flipColor(); //make black aunt > red
                grandparent.flipColor(); // make black grandparent > red

                ensureRedProperty(grandparent); //check if there is violation up
                return;
            }
                //aunt is black
                //if aunt is right child, then parent is left,
                // ensure parent also has left child or left rotate
            if (!auntIsLeftChild) {
                if (parent.getLeft() == newNode) { //no need to rotate in straight line
                        //do nothing
                } else {
                    rotate(newNode, parent);
                    RedBlackNode<T> temp = newNode;
                    newNode = parent;
                    parent = temp;
                }
            } else { /// aunt is left child, parent is right child
                    if (parent.getRight() == newNode) { //no need to rotate in straight line
                    //do nothing
                } else {
                    rotate(newNode, parent);
                    RedBlackNode<T> temp = newNode;
                    newNode = parent;
                    parent = temp;
                }
            }

            ///rotate and color swap
            rotate(parent, grandparent);
            parent.flipColor();
            grandparent.flipColor();

    }

///Testers

/**
* Test1: models Q03 RBT Insert Question 3
* Includes testing rebalancing logic with the insertion of red parent and red aunt,
* insertion with null aunt, and a leaf node insertion
* with no RBT violations
*/
@Test
public void testRBT1(){
RedBlackTree<Integer> testRBT1 = new RedBlackTree<>();
testRBT1.insert(3);
testRBT1.insert(16);
testRBT1.insert(4);
testRBT1.insert(17);
testRBT1.insert(1);
testRBT1.insert(5);
testRBT1.insert(18);

RedBlackNode<Integer> root = (RedBlackNode<Integer>) testRBT1.root;

//check root value and if it is black
assertEquals(4, root.getData());
assertTrue(root.isBlackNode());


//check order
assertEquals(3, root.getLeft().getData()); // 3 is left child of root 4 and black
assertTrue(root.getLeft().isBlackNode());
assertEquals(16, root.getRight().getData()); //16 is right child of root 4 and red
assertFalse(root.getRight().isBlackNode());

assertEquals(1, root.getLeft().getLeft().getData()); // 1 is left child of 3 and red
assertFalse(root.getLeft().getLeft().isBlackNode());
assertEquals(17, root.getRight().getRight().getData()); //17 is right child of 16 and black
assertTrue(root.getRight().getRight().isBlackNode());
assertEquals(5, root.getRight().getLeft().getData()); //5 is left child of 16 and black
assertTrue(root.getRight().getLeft().isBlackNode());

assertEquals(18, root.getRight().getRight().getRight().getData()); //18 is right child of 17 and red
assertFalse(root.getRight().getRight().getRight().isBlackNode());

}


/**
* Test2: models Q03 RBT Insert Question 2
* testing rebalancing logic with both a color flip and right rotation
*
*/
@Test
public void testRBT2(){
RedBlackTree<String> testRBT2 = new RedBlackTree<>();
testRBT2.insert("L");
testRBT2.insert("E");
testRBT2.insert("T");
testRBT2.insert("C");
testRBT2.insert("I");
testRBT2.insert("P");
testRBT2.insert("V");
testRBT2.insert("B");
testRBT2.insert("D");
testRBT2.insert("A");

RedBlackNode<String> root = (RedBlackNode<String>) testRBT2.root;

//check root value and if it is black
assertEquals("E", root.getData());
assertTrue(root.isBlackNode());

//check order
assertEquals("C", root.getLeft().getData()); // C is left child of root E and red
assertFalse(root.getLeft().isBlackNode());
assertEquals("L", root.getRight().getData()); //L is right child of root E and red
assertFalse(root.getRight().isBlackNode());

assertEquals("B", root.getLeft().getLeft().getData()); // B is left child of C and black
assertTrue(root.getLeft().getLeft().isBlackNode());
assertEquals("D", root.getLeft().getRight().getData()); // D is right child of C and black
assertTrue(root.getLeft().getRight().isBlackNode());

assertEquals("I", root.getRight().getLeft().getData()); // I is left child of L and black
assertTrue(root.getRight().getLeft().isBlackNode());
assertEquals("T", root.getRight().getRight().getData()); // T is right child of L and black
assertTrue(root.getRight().getRight().isBlackNode());

assertEquals("A", root.getLeft().getLeft().getLeft().getData()); //A is left child of B and red
assertFalse(root.getLeft().getLeft().getLeft().isBlackNode());

assertEquals("P", root.getRight().getRight().getLeft().getData()); // P is left child of T and red
assertFalse(root.getRight().getRight().getLeft().isBlackNode());

assertEquals("V", root.getRight().getRight().getRight().getData()); // V is right child of T and red
assertFalse(root.getRight().getRight().getRight().isBlackNode());

}


/**
* Test3: models Q03 RBT Insert Question 4
* testing rebalancing logic with both a color flip and left rotation
*
*/
@Test
public void testRBT3(){
RedBlackTree<Integer> testRBT3 = new RedBlackTree<>();
testRBT3.insert(3);
testRBT3.insert(4);
testRBT3.insert(10);
testRBT3.insert(2);
testRBT3.insert(8);

RedBlackNode<Integer> root = (RedBlackNode<Integer>) testRBT3.root;

//check root value and if it is black
assertEquals(4, root.getData());
assertTrue(root.isBlackNode());

//check order
assertEquals(3, root.getLeft().getData()); // 3 is left child of root 4 and black
assertTrue(root.getLeft().isBlackNode());
assertEquals(10, root.getRight().getData()); //10 is right child of root 4 and black
assertTrue(root.getRight().isBlackNode());

assertEquals(2, root.getLeft().getLeft().getData()); // 2 is left child of 3 and red
assertFalse(root.getLeft().getLeft().isBlackNode());


assertEquals(8, root.getRight().getLeft().getData()); //  is left child of 10 and red
assertFalse(root.getRight().getLeft().isBlackNode());


}


}