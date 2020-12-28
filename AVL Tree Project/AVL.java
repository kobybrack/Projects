public class AVL {

  public Node root;

  private int size;

  public int getSize() {
    return size;
  }

  /** find w in the tree. return the node containing w or
  * null if not found */
  public Node search(String w) {
    return search(root, w);
  }
  private Node search(Node n, String w) {
    if (n == null) {
      return null;
    }
    if (w.equals(n.word)) {
      return n;
    } else if (w.compareTo(n.word) < 0) {
      return search(n.left, w);
    } else {
      return search(n.right, w);
    }
  }

  /** insert w into the tree as a standard BST, ignoring balance */
  public void bstInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    bstInsert(root, w);
  }

  /* insert w into the tree rooted at n, ignoring balance
   * pre: n is not null */
  private void bstInsert(Node n, String w) {
    if (n.word == w) {
      return;
    }
    if (w.compareTo(n.word) < 0) {
      if (n.left != null) {
        bstInsert(n.left, w);
      } else {
        n.left = new Node(w, n, null, null);
        this.size++;
      }
    } 
    if (w.compareTo(n.word) > 0) {
      if (n.right != null) {
        bstInsert(n.right, w);
      } else{ 
        n.right = new Node(w, n, null, null);
        this.size++;
      }
    }
  }

  /** insert w into the tree, maintaining AVL balance
  *  precondition: the tree is AVL balanced */
  public void avlInsert(String w) {
    if (root == null) {
      root = new Node(w);
      size = 1;
      return;
    }
    avlInsert(root, w);
  }

  /* insert w into the tree, maintaining AVL balance
   *  precondition: the tree is AVL balanced and n is not null */
  private void avlInsert(Node n, String w) {
    if (n.word == w) {
      return;
    }
    if (w.compareTo(n.word) < 0) {
      if (n.left != null) {
        avlInsert(n.left, w);
      } else {
        n.left = new Node(w, n, null, null);
        this.size++;
      }
    } 
    if (w.compareTo(n.word) > 0) {
      if (n.right != null) {
        avlInsert(n.right, w);
      } else{ 
        n.right = new Node(w, n, null, null);
        this.size++;
      }
    }
    updateHeight(n);
    rebalance(n);
  }

  private void updateHeight(Node n) {
    if (n.left != null && n.right != null) {
      if (n.left.height > n.right.height) {
          n.height = n.left.height + 1;
        } else {
          n.height = n.right.height + 1;
        }
    } else if (n.left != null && n.right == null) {
        n.height = n.left.height + 1;
    } else if (n.left == null && n.right != null) {
        n.height = n.right.height + 1;
    } 
  }

  /** do a left rotation: rotate on the edge from x to its right child.
  *  precondition: x has a non-null right child */
  public void leftRotate(Node x) {
    Node temp = x.right;
    if (temp.left != null) {
      x.right = temp.left;
      temp.left.parent = x;
      if (x.left == null) {
        x.height = x.right.height + 1;
      } else if(x.left != null && x.right.height > x.left.height) {
        x.height = x.right.height + 1;
      } else if (x.left != null && x.left.height < x.left.height) {
        x.height = x.left.height + 1;
      } else {
        x.height = x.left.height + 1;
      }
    } else {
      x.right = null;
      if (x.left != null) {
        x.height = x.left.height + 1; 
      } else {
        x.height = 0;
      }
    }
    if (x.parent == null) {
      temp.parent = null;
      this.root = temp;
    } else {
      temp.parent = x.parent;
      if (x.parent.left == x) {
        x.parent.left = temp;
      }
      if (x.parent.right == x) {
        x.parent.right = temp;
      }
    }
    temp.left = x;
    x.parent = temp;
    updateHeight(temp);
  }

  /** do a right rotation: rotate on the edge from x to its left child.
  *  precondition: y has a non-null left child */
  public void rightRotate(Node y) {
    Node temp = y.left;
    if (temp.right != null) {
      y.left = temp.right;
      temp.right.parent = y;
      if (y.right == null) {
        y.height = y.left.height + 1;
      } else if(y.right != null && y.left.height > y.right.height) {
        y.height = y.left.height + 1;
      } else if (y.right != null && y.left.height < y.right.height) {
        y.height = y.right.height + 1;
      } else {
        y.height = y.right.height + 1;
      }
    } else {
      y.left = null;
      if (y.right != null) {
        y.height = y.right.height + 1; 
      } else {
        y.height = 0;
      }
    }
    if (y.parent == null) {
      temp.parent = null;
      this.root = temp;
    } else {
      temp.parent = y.parent;
      if (y.parent.left == y) {
        y.parent.left = temp;
      }
      if (y.parent.right == y) {
        y.parent.right = temp;
      }
    }
    temp.right = y;
    y.parent = temp;
    updateHeight(temp);
  }

  /** rebalance a node N after a potentially AVL-violoting insertion.
  *  precondition: none of n's descendants violates the AVL property */
  public void rebalance(Node n) {
    if (balanceFactor(n) < -1) {
      if (balanceFactor(n.left) < 0) {
        this.rightRotate(n);
      } else {
        this.leftRotate(n.left);      
        this.rightRotate(n);      
      }
    }
    if (balanceFactor(n) > 1) {
        if (balanceFactor(n.right) < 0) {
          this.rightRotate(n.right);
          this.leftRotate(n);
        } else {
          this.leftRotate(n);
        }
    }
  }

  private int balanceFactor(Node n) {
    if (n.right == null && n.left == null) {
      return 0;
    }
    if (n.right == null && n.left != null) {
      return (-1 - n.left.height);
    }
    if (n.right != null && n.left == null) {
      return (n.right.height + 1);
    }
    return (n.right.height - n.left.height);
  }

  /** remove the word w from the tree */
  public void remove(String w) {
    remove(root, w);
  }

  /* remove w from the tree rooted at n */
  private void remove(Node n, String w) {
    return; // (enhancement TODO - do the base assignment first)
  }

  /** print a sideways representation of the tree - root at left,
  * right is up, left is down. */
  public void printTree() {
    printSubtree(root, 0);
  }
  private void printSubtree(Node n, int level) {
    if (n == null) {
      return;
    }
    printSubtree(n.right, level + 1);
    for (int i = 0; i < level; i++) {
      System.out.print("        ");
    }
    System.out.println(n);
    printSubtree(n.left, level + 1);
  }

  /** inner class representing a node in the tree. */
  public class Node {
    public String word;
    public Node parent;
    public Node left;
    public Node right;
    public int height;

    public String toString() {
      return word + "(" + height + ")";
    }

    /** constructor: gives default values to all fields */
    public Node() { }

    /** constructor: sets only word */
    public Node(String w) {
      word = w;
    }

    /** constructor: sets word and parent fields */
    public Node(String w, Node p) {
      word = w;
      parent = p;
    }

    /** constructor: sets all fields */
    public Node(String w, Node p, Node l, Node r) {
      word = w;
      parent = p;
      left = l;
      right = r;
    }
  }
}
