package huffmancodingtext;

/**
 *
 * @author piker
 */
import java.util.*;
import static java.util.Collections.sort;
import javafx.util.Pair;

public class HuffmanCodingText {

    /**
     * @param args the command line arguments
     */
    static ArrayList<Node> codingArray;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("string ra vared konid \n");
        String inputText = input.nextLine();
        ArrayList<Node> chars = countChars(inputText);
        Collections.sort(chars);
        int numberOfChars = chars.size();
        for (Node a : chars) {
            System.out.println(a);
        }
        while (chars.size() > 1) {
            Node first = chars.remove(chars.size() - 1);
            Node second = chars.remove(chars.size() - 1);
            chars.add(second.merge(first));
        }
        Node head = chars.get(0);
        codingArray = new ArrayList<Node>();
        readTree(head, new ArrayDeque<Integer>());
        Collections.sort(codingArray);
        for (Node a : codingArray) {
            System.out.println(a);
        }
        System.out.println(codeThisString(inputText));
    }

    static ArrayList<Node> countChars(String input) {
        ArrayList<Node> out = new ArrayList<Node>();
        for (int i = 0; input.length() > 0; i++) {
            char selectedChar = input.charAt(0);
            int count = 1;
            for (int j = 1; j < input.length(); j++) {
                if (input.charAt(j) == selectedChar) {
                    count++;
                }
            }
            input = input.replaceAll(selectedChar + "", "");
            out.add(new Node(selectedChar, count, null, null));
        }
        return out;
    }

    static void readTree(Node top, ArrayDeque<Integer> temp) {
        if (top.right == null & top.left == null) {
            top.code = temp.clone();
            codingArray.add(top);
            return;
        }
        temp.push(0);
        readTree(top.left, temp);
        temp.pop();
        temp.push(1);
        readTree(top.right, temp);
        temp.pop();
    }

    static String codeThisString(String inputString) {
        String out="";
        for (int i = 0; i < inputString.length(); i++) {
            for (Node a : codingArray) {
                if (a.value == inputString.charAt(i)) {
                    out += a.biString();
                }
            }
        }
        return out;
    }
}
