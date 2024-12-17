import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Node<T> {
    int id;
    T someData;
    Node<T> leftChild;
    Node<T> rightChild;

    public Node(int id, T someData) {
        this.id = id;
        this.someData = someData;
    }

    public void displayNode() {
        System.out.println("Node [id=" + id + ", data=" + someData + "]");
    }
}

class Tree<T extends Comparable<T>> {
    private Node<T> root;

    public void insert(int id, T someData) {
        Node<T> newNode = new Node<>(id, someData);
        if (root == null) {
            root = newNode;
        } else {
            Node<T> current = root;
            Node<T> parent;
            while (true) {
                parent = current;
                if (someData.compareTo(current.someData) < 0) { // Двигаться налево
                    current = current.leftChild;
                    if (current == null) { // Если нет левого потомка
                        parent.leftChild = newNode;
                        return;
                    }
                } else { // Двигаться направо
                    current = current.rightChild;
                    if (current == null) { // Если нет правого потомка
                        parent.rightChild = newNode;
                        return;
                    }
                }
            }
        }
    }

    public Node<T> find(T key) {
        Node<T> current = root; // Начать с корневого узла
        while (current != null) {
            if (key.compareTo(current.someData) < 0) { // Двигаться налево
                current = current.leftChild;
            } else if (key.compareTo(current.someData) > 0) { // Двигаться направо
                current = current.rightChild;
            } else {
                return current; // Найдено совпадение
            }
        }
        return null; // Не найдено
    }

    public void inOrderTraversal(Node<T> node, List<T> result) {
        if (node != null) {
            inOrderTraversal(node.leftChild, result);
            result.add(node.someData);
            inOrderTraversal(node.rightChild, result);
        }
    }

    public Node<T> getRoot() {
        return root;
    }

    public boolean delete(T key) {
        Node<T> current = root;
        Node<T> parent = root;
        boolean isLeftChild = true;

        // Поиск узла для удаления
        while (current.someData.compareTo(key) != 0) {
            parent = current;
            if (key.compareTo(current.someData) < 0) {
                isLeftChild = true;
                current = current.leftChild;
            } else {
                isLeftChild = false;
                current = current.rightChild;
            }
            if (current == null) {
                return false; // Узел не найден
            }
        }

        // Узел для удаления найден
        // Если узел не имеет потомков
        if (current.leftChild == null && current.rightChild == null) {
            if (current == root) {
                root = null;
            } else if (isLeftChild) {
                parent.leftChild = null;
            } else {
                parent.rightChild = null;
            }
        }
        // Если узел имеет одного потомка
        else if (current.rightChild == null) {
            if (current == root) {
                root = current.leftChild;
            } else if (isLeftChild) {
                parent.leftChild = current.leftChild;
            } else {
                parent.rightChild = current.leftChild;
            }
        }
        else if (current.leftChild == null) {
            if (current == root) {
                root = current.rightChild;
            } else if (isLeftChild) {
                parent.leftChild = current.rightChild;
            } else {
                parent.rightChild = current.rightChild;
            }
        }
        // Если узел имеет двух потомков
        else {
            Node<T> successor = getSuccessor(current);
            if (current == root) {
                root = successor;
            } else if (isLeftChild) {
                parent.leftChild = successor;
            } else {
                parent.rightChild = successor;
            }
            successor.leftChild = current.leftChild;
        }
        return true;
    }

    private Node<T> getSuccessor(Node<T> delNode) {
        Node<T> successorParent = delNode;
        Node<T> successor = delNode;
        Node<T> current = delNode.rightChild;

        while (current != null) {
            successorParent = successor;
            successor = current;
            current = current.leftChild;
        }

        if (successor != delNode.rightChild) {
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }

    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(Node<T> node, String prefix, boolean isTail) {
        if (node != null) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + node.someData);
            printTree(node.leftChild, prefix + (isTail ? "    " : "│   "), false);
            printTree(node.rightChild, prefix + (isTail ? "    " : "│   "), true);
        }
    }

}

public class Main {
    public static <T extends Comparable<T>> List<T> sortUsingTree(Tree tree) {
        List<T> sortedList = new ArrayList<>();
        tree.inOrderTraversal(tree.getRoot(), sortedList);
        return sortedList;
    }
    public static <T extends Comparable<T>> List<T> sortUsingTree(T[] array) {

        Tree<T> tree = new Tree<>();
        for (int i = 0; i < array.length; i++) {
            tree.insert(i, array[i]);
        }
        List<T> sortedList = new ArrayList<>();
        tree.inOrderTraversal(tree.getRoot(), sortedList);
        return sortedList;
    }

    public static void main(String[] args) {

        Integer[] array = {5, 2, 9, 1, 2, 6};
        Tree<Integer> tree = new Tree<>();
        for (int i = 0; i < array.length; i++) {
            tree.insert(i, array[i]);
        }
        List<Integer> sorted = sortUsingTree(tree);
        System.out.println("Sorted array: " + sorted);

        String[] stringArray = {"b", "a", "c", "z", "ch", "vladimir", "aa"};
        List<String> sortedStrings = sortUsingTree(stringArray);
        System.out.println("Sorted strings: " + sortedStrings);

        int length = 1000000;
        Integer[] bigArray = new Integer[length];
        Random random = new Random(); // создаем объект класса Random
        for (int i = 0; i < length; i++) { bigArray[i] = random.nextInt(10000);}
        long startTime = System.currentTimeMillis();
        List<Integer> sortedBigArray = sortUsingTree(bigArray);
        long endTime = System.currentTimeMillis();
        System.out.printf("Sorting an array of %s elements took: %s ms%n", length, (endTime - startTime));
    }

}