package huffmancodingtext;

/**
 *
 * @author piker
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import static java.util.Collections.sort;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

public class HuffmanCodingText extends Application {

    /**
     * @param args the command line arguments
     */
    static ArrayList<Node> codingArray;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Input Text File");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open input File");
                try {
                    runCoding(fileChooser.showOpenDialog(primaryStage),primaryStage);
                } catch (IOException ex) {
                    Logger.getLogger(HuffmanCodingText.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Huffman Coding | by Piker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void runCoding(File inputFile,Stage primaryStage) throws IOException {

        Scanner input = new Scanner(System.in);
        String inputText = readFile(inputFile);
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
        String outString = codeThisString(inputText);
//        System.out.println(outString);
        byte[] bytesToWriteInFile = fromBinary(codeThisString(inputText));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Coded File");
        File file = fileChooser.showSaveDialog(primaryStage);
        FileOutputStream fos = new FileOutputStream(file.getPath()+file.getName());
        fos.write(bytesToWriteInFile);
        fos.close();
        Path path = Paths.get(file.getPath()+file.getName());
        byte[] data = Files.readAllBytes(path);
//        System.out.println(toBinary(data));
//        System.out.println(decode(outString, head));

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
        String out = "";
        for (int i = 0; i < inputString.length(); i++) {
            for (Node a : codingArray) {
                if (a.value == inputString.charAt(i)) {
                    out += a.biString();
                }
            }
        }
        return out;
    }

    static String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    static byte[] fromBinary(String s) {
        int sLen = s.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++) {
            if ((c = s.charAt(i)) == '1') {
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            } else if (c != '0') {
                throw new IllegalArgumentException();
            }
        }
        return toReturn;
    }

    static String decode(String input, Node head) {
        String out = "";
        for (int i = 0; i < input.length();) {
            Node temp = head;
            try {
                while (temp.left != null || temp.right != null) {
                    if (input.charAt(i) == '1') {
                        temp = temp.right;
                        i++;
                    } else {
                        temp = temp.left;
                        i++;
                    }
                }
            } catch (Exception a) {
                System.out.println(" error ");
            }
            out += temp.value;
        }
        return out;
    }

    static String readFile(File inputFile) {
        String out = "";
        try {
            File file = inputFile;
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();
            out = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

}
