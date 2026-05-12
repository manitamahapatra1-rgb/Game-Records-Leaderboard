/**
* Author:Manita Mahapatra
* Email:mmahapatra@wisc.edu
* Course: CS 400, Spring 2026
* Assignment: P101
* Citations:
*/

/**
* This class implements a binary search tree that sorts values in BST order. It implements
* the methods insert, contains, size, isEmpty, and clear recursively
*/
public class BinarySearchTree<T extends Comparable <T>> implements SortedCollection<T> {

    protected BinaryNode<T> root;

    public BinarySearchTree() {
        root = null;
    }

    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree. When the provided subtree
     * is null, this method does nothing.
     * @param newNode is the node being inserted
     * @param subtree is the subtree where the newNode is being inserted
     */
    protected void insertHelper(BinaryNode<T> newNode, BinaryNode<T> subtree) {
        if (subtree != null){
            if (newNode.data.compareTo(subtree.data) <= 0) {
                if (subtree.left != null) {
                    insertHelper(newNode, subtree.left);
                } else {
                    subtree.left = newNode;
                    newNode.up = subtree;
                }
            } else {
                if (subtree.right != null) {
                    insertHelper(newNode, subtree.right);
                }
                else {
                    subtree.right = newNode;
                    newNode.up = subtree;
                }
            }
        }
    }

    /**
     * Inserts a new data value into the sorted collection.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null, we do not allow
     * null values to be stored within a SortedCollection
     */
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("Cannot insert null data");
        }
        BinaryNode<T> newNode = new BinaryNode<> (data);
        if (this.root == null) {
            this.root = newNode;
        } else {
            insertHelper(newNode, this.root);
        }
    }

    /**
     * Performs the search for the contains algorithm to recursively
     * search for the provided find node. Method returns the boolean value of true or false
     * depending on whether the node is contained within the BST.
     * @param find is the node value being searched
     * @param currentNode is the current node being compared
     * @return boolean value of if node is within BST or not
     */
    public boolean containsHelper(Comparable<T> find, BinaryNode<T> currentNode) {
        if (currentNode == null) {
            return false;
        }
        //left subtree
        if (find.compareTo(currentNode.data) == 0) {
            return true;
        }
        if (find.compareTo(currentNode.data) < 0) {
            return containsHelper(find, currentNode.left);
        } else { //right subtree
            return containsHelper(find, currentNode.right);
        }
    }

    /**
     * Returns true if find node is within BST, false if not.
     * @param find the node being searched for
     * @return boolean true or false if find node is contained within BST
     */
    public boolean contains(Comparable<T> find) {
        return containsHelper(find, this.root);
    }

    /**
     * Performs the operation of counting the number of nodes in the BST for the size algrothim
     * @param root the root of the BST
     * @return int the size of the BST
     */
    public int sizeHelper(BinaryNode<T> root) {
        if (root == null) {
            return 0;
        }
        return 1 + sizeHelper(root.left) + sizeHelper(root.right);
    }

    /**
     * Counts the number of values in the collection, with each duplicate value
     * being counted separately within the value returned.
     * @return the number of values in the collection, including duplicates
     */
    public int size() {
        return sizeHelper(root);
    }

    /**
     * Checks if the collection is empty.
     * @return true if the collection contains 0 values, false otherwise
     */
    public boolean isEmpty() {
        if (root == null) {
            return true;
        }
        return false;
    }

    /**
     * Removes all values and duplicates from the collection.
     */
    public void clear() {
        this.root = null;
    }

    ///test1 checks size, clear, isEmpty, contains in an unbalanced Integer BST
    public boolean test1(){
        boolean allPass = true;
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(10);
        bst.insert(12);
        bst.insert(15);
        bst.insert(8);
        bst.insert(14);
        bst.insert(18);
        bst.insert(18);

        //size check
        if (bst.size() == 7) {
            //pass
        } else {
            allPass = false;
            System.out.println("fail at size check");
        }

        //contains check
        if (bst.contains(10) && bst.contains(12) && bst.contains(15)
            && bst.contains(8) && bst.contains(14) && bst.contains(18)){
            //pass
        } else {
            allPass = false;
            System.out.println("fail at contents check");
        }


        //order check
        BinaryNode<Integer> rootNode = bst.root;
        BinaryNode<Integer> a_10 = rootNode; //10
        BinaryNode<Integer> b_8 = a_10.getLeft(); //8
        BinaryNode<Integer> c_12 = a_10.getRight(); //12
        BinaryNode<Integer> d_15 = c_12.getRight(); //15
        BinaryNode<Integer> e_14 = d_15.getLeft();//14
        BinaryNode<Integer> f_18 = d_15.getRight(); //18
        BinaryNode<Integer> g_18 = f_18.getLeft(); //18

        if (a_10!= null && a_10.getData().equals(10)
            && b_8!= null && b_8.getData().equals(8) && b_8.getLeft() == null
            && c_12!= null && c_12.getData().equals(12) && c_12.getLeft() == null
            && d_15!= null && d_15.getData().equals(15)
            && e_14!= null && e_14.getData().equals(14)
            && f_18!= null && f_18.getData().equals(18) && f_18.getRight() == null
            && g_18!= null && g_18.getData().equals(18) && g_18.getRight() == null && g_18.getRight() == null) {
            //pass
        } else {
            allPass = false;
            System.out.println("fail, layout of bst is incorrect");
        }

        //clear and isEmpty check
        bst.clear();
        if (bst.size() == 0 && bst.isEmpty()) {
            //pass
        } else {
            allPass = false;
            System.out.println("fail at clear and isEmpty check");
        }


        return allPass;
    }

    //test2 checks size after insertion of balanced BST before and after clear
    public boolean test2(){
        boolean allPass = true;
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(16);
        bst.insert(5);
        bst.insert(20);

        //size and isEmpty check
        if (bst.size() == 3 && bst.isEmpty() == false) {
            //pass
        } else {
            allPass = false;
            System.out.println("fail at size check");
        }
        bst.clear();

        //size and isEmpty check after clear
        if (bst.size() == 0 && bst.isEmpty()) {
            //pass
        } else {
            allPass = false;
            System.out.println("clear failed");
        }

        bst.insert(18);
        bst.insert(17);
        bst.insert(22);

        //Size and isEmpty check after clear and new insertion
        if (bst.size() == 3 && bst.isEmpty() == false) {
            //pass
        } else {
            allPass = false;
            System.out.println("clear failed");
        }

        if (bst.contains(18) && bst.contains(17) && bst.contains(22)) {
            //pass
        } else {
            allPass = false;
            System.out.println("contains failed");
        }
       return allPass;
    }

    //test3 checks size, clear, isEmpty, contains in an unbalanced String BST
    public boolean test3(){
        boolean allPass = true;
        BinarySearchTree<String> bst = new BinarySearchTree<>();
        bst.insert("mango");
        bst.insert("apple");
        bst.insert("orange");
        bst.insert("passionfruit");
        bst.insert("banana");
        bst.insert("pineapple");
        bst.insert("coconut");

        //size check
        if (bst.size() == 7) {
            //pass
        } else {
            allPass = false;
            System.out.println("fail at size check");
        }

        //contains check
        if (bst.contains("mango") && bst.contains("apple") && bst.contains("orange")
            && bst.contains("passionfruit") && bst.contains("banana")
            && bst.contains("pineapple") && bst.contains("coconut") ){
            //pass
        } else {
            allPass = false;
            System.out.println("fail at contents check");
        }


        //order check
        BinaryNode<String> rootNode = bst.root;
        BinaryNode<String> mango = rootNode;
        BinaryNode<String> apple = mango.getLeft();
        BinaryNode<String> orange = mango.getRight();
        BinaryNode<String> passionfruit = orange.getRight();
        BinaryNode<String> banana = apple.getRight();
        BinaryNode<String> pineapple = passionfruit.getRight();
        BinaryNode<String> coconut = banana.getRight();

        if (mango!= null && mango.getData().equals("mango")
            && apple!= null && apple.getData().equals("apple") && apple.getLeft() == null
            && orange!= null && orange.getData().equals("orange") && orange.getLeft() == null
            && passionfruit!= null && passionfruit.getData().equals("passionfruit") && passionfruit.getLeft() == null
            && banana!= null && banana.getData().equals("banana") && banana.getLeft() == null
            && pineapple!= null && pineapple.getData().equals("pineapple") && pineapple.getLeft() == null && pineapple.getRight() == null
            && coconut!= null && coconut.getData().equals("coconut") && coconut.getLeft() == null && coconut.getRight() == null) {
            //pass
        } else {
            allPass = false;
            System.out.println("fail, layout of bst is incorrect");
        }

        //clear and isEmpty check
        bst.clear();
        if (bst.size() == 0 && bst.isEmpty()) {
            //pass
        } else {
            allPass = false;
            System.out.println("fail at clear and isEmpty check");
        }

        return allPass;
    }


    public static void main(String[] args) {
        BinarySearchTree<Integer> testers = new BinarySearchTree<>();
        boolean test1Result = testers.test1();
        Boolean test2Result = testers.test2();
        boolean test3Result = testers.test3();

        if(test1Result){
            System.out.println("test1: PASS");
        } else {
            System.out.println("test1: FAIL");
        }

        if(test2Result){
            System.out.println("test2: PASS");
        } else {
            System.out.println("test2: FAIL");
        }

        if(test3Result){
            System.out.println("test3: PASS");
        } else {
            System.out.println("test3: FAIL");
        }

    }

}