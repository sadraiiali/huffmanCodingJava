package huffmancodingtext;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Objects;
import jdk.nashorn.internal.ir.BinaryNode;

/**
 *
 * @author piker
 */
public class Node implements Comparable {

    char value;
    int repeat;
    ArrayDeque<Integer> code;
    Node right;
    Node left;

    public Node(char value, int repeat, Node right, Node left) {
        this.value = value;
        this.repeat = repeat;
        this.right = right;
        this.left = left;
    }

    public Node(int repeat, Node right, Node left) {
        this.repeat = repeat;
        this.right = right;
        this.left = left;
    }

    Node merge(Node other) {
        Node out;
        out = new Node(this.repeat + other.repeat, this, other);
        return out;
    }

    @Override
    public String toString() {
        if (code == null) {
            return "Node{" + "value=" + value + ", repeat=" + repeat + '}';
        } else {

            return "Node{" + "value=" + value + ", repeat=" + repeat + '}' + biString();

        }
    }

    String biString() {
        String biString = "";
        for (Iterator descItr = code.descendingIterator(); descItr.hasNext();) {
            biString += descItr.next();
        }
        return biString;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.value;
        hash = 73 * hash + this.repeat;
        hash = 73 * hash + Objects.hashCode(this.right);
        hash = 73 * hash + Objects.hashCode(this.left);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.repeat != other.repeat) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object t) {
        Node other = (Node) t;
        return other.repeat - this.repeat;
    }

}
