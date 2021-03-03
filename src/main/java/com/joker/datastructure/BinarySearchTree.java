package com.joker.datastructure;

public class BinarySearchTree {

    private static Node root;

    public static void main(String[] args) {
        int[] nums = {15, 18, 17, 20, 6, 3, 2, 4, 7, 13, 9};
        initBinarySearchTree(nums);
        anotherTreeWalk(root);
        Node node = treeSearch(root, 13);
        Node successorNode = treeSuccessor(node);
        System.out.println(successorNode.value);
        node = treeSearch(root, 7);
        Node predecessorNode = treePredecessor(node);
        System.out.println(predecessorNode.value);
    }
    
    static class Node {
        Node pre;
        Node left;
        Node right;

        int value;

        Node(Node pre, Node left, Node right, int value) {
            this.pre = pre;
            this.left = left;
            this.right = right;
            this.value = value;
        }

        public final int getValue() { return value; }
    }

    static void initBinarySearchTree(int[] nums) {
        Node node = null;
        root = new Node(null, null, null, nums[0]);
        for (int i = 1; i < nums.length; i++) {
            node = new Node(null, null, null, nums[i]);
            treeInsert(root, node);
        }
    }

    /**
     * 二叉搜索树的插入
     * @param root
     * @param insertNode
     */
    static void treeInsert(Node root, Node insertNode) {
        Node pre = null;
        Node x = root;
        while (x != null) {
            pre = x;
            if (insertNode.value < x.value) {
                x = x.left;
            } else {
                x = x.right;
            }
        }
        insertNode.pre = pre;
        if (pre == null) {
            root = insertNode;
        } else if (insertNode.value < pre.value) {
            pre.left = insertNode;
        } else {
            pre.right = insertNode;
        }
    }

    /**
     * 二叉搜索树：中序遍历整个树
     */
    static void inorderTreeWalk(Node node) {
        if (node != null) {
            inorderTreeWalk(node.left);
            System.out.print(" " + node.value + " ");
            inorderTreeWalk(node.right);
        }
    }

    /**
     * 二叉搜索树：非递归实现中序遍历
     * 
     * 先获取该树的最小关键结点，然后循环查找后继结点
     * @param node
     */
    static void anotherTreeWalk(Node node) {
        System.out.println();
        System.out.println("--------------");
        Node minimumNode = treeMinimum(node);
        System.out.print(" " + minimumNode.value + " ");
        while((minimumNode = treeSuccessor(minimumNode)) != null) {
            System.out.print(" " + minimumNode.value + " ");
        }
        
    }

    /**
     * 二叉搜索树：通过递归查找指定的value
     * @param root
     * @param value
     * @return
     */
    static Node treeSearch(Node node, int value) {
        if (node == null || value == node.value) {
            return node;
        }
        if (value < node.value) {
            return treeSearch(node.left, value);
        } else {
            return treeSearch(node.right, value);
        }
    }

    /**
     * 二叉搜索树：通过while循环查找指定的value
     * @param node
     * @param value
     * @return
     */
    static Node iterativeTreeSearch(Node node, int value) {
        while (node != null && node.value != value) {
            if (value < node.value) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node;
    }

    /**
     * 二叉搜索树：获取最小关键字
     * @param node
     * @return
     */
    static Node treeMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * 二叉搜索树：获取最大关键字
     * @param node
     * @return
     */
    static Node treeMaximum(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    /**
     * 二叉搜索树：获取结点的后继节点，就是大于该结点的关键字中最小的结点
     * 
     * 如果该结点node存在右子树，那么，该node的后继结点就是该右子树的最小关键字
     * 否则，就从该node起往上查找一个结点，该结点为其双亲的左孩子
     * @param node
     * @return
     */
    static Node treeSuccessor(Node node) {
        if (node.right != null) {
            return treeMinimum(node.right);
        }
        Node successorNode = node.pre;
        while(successorNode != null && node == successorNode.right) {
            node = successorNode;
            successorNode = node.pre;
        }
        return successorNode;
    }

    /**
     * 二叉搜索树：获取结点的前驱结点
     * @param node
     * @return
     */
    static Node treePredecessor(Node node) {
        if (node.left != null) {
            return treeMaximum(node.left);
        }
        Node predecessorNode = node.pre;
        while (predecessorNode != null && node == predecessorNode.left) {
            node = predecessorNode;
            predecessorNode = node.pre;
        }
        return predecessorNode;
    }

    /**
     * 二叉搜索树：删除一个结点
     * @param root
     * @param deleteNode
     */
    static void treeDelete(Node root, Node deleteNode) {
        if (deleteNode.left == null) { // 删除的结点没有左孩子，则直接用它的右孩子结点替换
            treeSplant(root, deleteNode, deleteNode.right);
        } else if (deleteNode.right == null) { // 如果删除的结点没有右孩子，则直接用它的左孩子结点替换
            treeSplant(root, deleteNode, deleteNode.left);
        } else { // 要删除的结点有两个孩子
            // 找到它的后继结点，该后继结点因为是右子树中的最小关键结点，所以它没有左子树
            Node successorNode = treeMinimum(deleteNode.right);
            if (successorNode.pre != deleteNode) { // 如果后继结点不跟要删除的结点相邻
                // 让这个后继结点的右子树替换该后继结点
                treeSplant(root, successorNode, successorNode.right);
                successorNode.right = deleteNode.right;
                successorNode.right.pre = successorNode;
            }
            // 让这个后继结点替换掉要删除的结点
            treeSplant(root, deleteNode, successorNode);
            successorNode.left = deleteNode.left;
            successorNode.left.pre = successorNode;
        }
    }

    /**
     * 二叉搜索树：结点替换操作
     * 该函数不处理newNode的left,right更新操作，需要调用方自己额外处理
     * @param root
     * @param oldNode
     * @param newNode
     */
    static void treeSplant(Node root, Node oldNode, Node newNode) {
        if (oldNode.pre == null) { // 替换的结点正好是根节点
            root = newNode;
        } else if (oldNode == oldNode.pre.left) { // 要替换的结点是一个左结点
            oldNode.pre.left = newNode;
        } else { // 要替换的结点是一个右结点
            oldNode.pre.right = newNode;
        }
        if (newNode != null) {
            newNode.pre = oldNode.pre;
        }
    }
}


