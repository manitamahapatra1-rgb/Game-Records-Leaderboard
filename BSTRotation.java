public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {
    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     *
     * @param child is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     */
    protected void rotate(BinaryNode<T> child, BinaryNode<T> parent) {

        BinaryNode<T> newParent = child;
        BinaryNode<T> oldParent = parent;
        BinaryNode<T> newParentRightChild = child.getRight(); //will become oldParent's left child
        BinaryNode<T> newParentLeftChild = child.getLeft(); //will become oldParent's right child
        BinaryNode<T> oldParentParent = parent.getUp();

        if (oldParentParent != null){ // if parent node has a parent
            if (oldParentParent.getLeft() == oldParent){
                oldParentParent.setLeft(newParent);
            } else if (oldParentParent.getRight() == (oldParent)){
                oldParentParent.setRight(newParent);
            }
        } else {
            this.root = newParent; //if parent node is the root, switch child node to be the root
        }

        // if left child, right rotate
        if (child == parent.getLeft()) {
            newParent.setRight(oldParent);
            oldParent.setLeft(newParentRightChild);
            if (newParentRightChild != null) {
                newParentRightChild.setUp(oldParent);
            }

        } else { //if right child, left rotate
            newParent.setLeft(oldParent);
            oldParent.setRight(newParentLeftChild);
            if (newParentLeftChild != null) {
                newParentLeftChild.setUp(oldParent);
            }
        }

        oldParent.setUp(newParent); // set original parents parent to its child
        newParent.setUp(oldParentParent); //set original child to its parent

    }

    public boolean test1(){
        //Testing left Rotatation with 3 shared children and root node included in rotation
        boolean allPass = true;

        BinaryNode<T> parentNode = new BinaryNode("elephant");
        BinaryNode<T> childNode = new BinaryNode("giraffe");
        BinaryNode<T> antNode = new BinaryNode("ant");
        BinaryNode<T> fishNode = new BinaryNode("fish");
        BinaryNode<T> oxNode = new BinaryNode("ox");

        //setting up tree
        this.root = parentNode;
        parentNode.setRight(childNode);
        childNode.setUp(parentNode);
        parentNode.setLeft(antNode);
        antNode.setUp(parentNode);
        childNode.setLeft(fishNode);
        fishNode.setUp(childNode);
        childNode.setRight(oxNode);
        oxNode.setUp(childNode);

        //left rotate childNode and parentNode(parentNode is the root)
        this.rotate(childNode, parentNode);

        //checking for correct location of node
        if (this.root.equals(childNode)
           && childNode.getRight().equals(oxNode)
           && childNode.getLeft().equals(parentNode)
           && parentNode.getLeft().equals(antNode)
           && parentNode.getRight().equals(fishNode)) {
        //pass
        } else {
            allPass = false;
            System.out.println("fail1");
        }

        //checking for correct values of each node
        if (this.root.getData().equals("giraffe")
           && childNode.getRight().getData().equals("ox")
           && childNode.getLeft().getData().equals("elephant")
           && parentNode.getLeft().getData().equals("ant")
           && parentNode.getRight().getData().equals("fish")) {
        //pass
        } else {
            allPass = false;
            System.out.println("fail2");
        }
        return allPass;
    }

    public boolean test2(){
        //Testing right rotation with 2 shared children and root node included in rotation
        boolean allPass = true;

        BinaryNode<T> parent8 = new BinaryNode(8);
        BinaryNode<T> child3 =  new BinaryNode(3);
        BinaryNode<T> otherNode1 = new BinaryNode(1);
        BinaryNode<T> otherNode6 = new BinaryNode(6);

        //setting up tree
        this.root = parent8;
        parent8.setLeft(child3);
        child3.setUp(parent8);
        child3.setLeft(otherNode1);
        otherNode1.setUp(child3);
        child3.setRight(otherNode6);
        otherNode6.setUp(child3);

        //rotating child3 and parent8 (parent8 is the root)
        this.rotate(child3, parent8);

        //ceching for the correct location of each node after rotation
        if (this.root.equals(child3)
           && child3.getRight().equals(parent8)
           && child3.getLeft().equals(otherNode1)
           && parent8.getLeft().equals(otherNode6)) {
        //pass
        } else {
            allPass = false;
            System.out.println("fail3");
        }

        //checking for correct values of each node after rotation
        if (this.root.getData().equals(3)
           && child3.getRight().getData().equals(8)
           && child3.getLeft().getData().equals(1)
           && parent8.getLeft().getData().equals(6)) {
        //pass
        } else {
            allPass = false;
            System.out.println("fail4");
        }

        return allPass;
    }

    public boolean test3(){
        boolean allPass = true;

        //testing a tree with 0 shared children and root node not included in rotation
        BinaryNode<T> grandparent = new BinaryNode(8);
        BinaryNode<T> parent = new BinaryNode(3);
        BinaryNode<T> child = new BinaryNode(1);
        this.root = grandparent;
        grandparent.setLeft(parent);
        parent.setUp(grandparent);
        parent.setLeft(child);
        child.setUp(parent);

        //rotating child and parent (grandparent is the root)
        this.rotate(child, parent);

        //checking for correct location of each node after rotation
        if (this.root.equals(grandparent)
            && grandparent.getLeft().equals(child)
            && child.getRight().equals(parent)) {
            //pass
        } else {
            allPass = false;
        }

        //checking for correct values of each node after roatation
        if (this.root.getData().equals(8)
            && grandparent.getLeft().getData().equals(1)
            && child.getRight().getData().equals(3)) {
            //pass
        } else {
            allPass = false;
        }

        return allPass;
    }

    public boolean test4(){
        boolean allPass = true;
        //testing tree with 1 shared child and a root node not included in rotation
        BinaryNode<T> grandparentChair = new BinaryNode("chair");
        BinaryNode<T> parentTable = new BinaryNode("table");
        BinaryNode<T> childDesk = new BinaryNode("desk");
        BinaryNode<T> childLamp = new BinaryNode("lamp");

        //setting up tree
        this.root = grandparentChair;
        grandparentChair.setRight(parentTable);
        parentTable.setUp(grandparentChair);
        parentTable.setLeft(childDesk);
        childDesk.setUp(parentTable);
        childDesk.setRight(childLamp);
        childLamp.setUp(childDesk);

        //rotating childDesk and parentTable (grandparentChair is the root)
        rotate(childDesk, parentTable);

        //checking for correct locations of each node after rotation
        if(this.root.equals(grandparentChair)
           && grandparentChair.getRight().equals(childDesk)
           && childDesk.getRight().equals(parentTable)
           && parentTable.getLeft().equals(childLamp)) {
            //pass
        } else {
            allPass = false;
        }

        //checking for correct values of each node after rotation
        if(this.root.getData().equals("chair")
           && grandparentChair.getRight().getData().equals("desk")
           && childDesk.getRight().getData().equals("table")
           && parentTable.getLeft().getData().equals("lamp")) {
            //pass
        } else {
            allPass = false;
        }
        return allPass;
    }

    public static void main(String[] args) {
        BSTRotation<String> stringTester = new BSTRotation<>();
        BSTRotation<Integer> intTester = new BSTRotation<>();
        boolean test1Result = stringTester.test1();
        Boolean test2Result = intTester.test2();
        boolean test3Result = intTester.test3();
        boolean test4Result = stringTester.test4();

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

        if(test4Result){
            System.out.println("test4: PASS");
        } else {
            System.out.println("test4: FAIL");
        }
    }


}