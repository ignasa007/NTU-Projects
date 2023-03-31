package Q2;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import static java.lang.Math.min;
import static java.lang.Math.max;

public class Q2 {

    public static void main(String[] args) throws IOException {

        try {

            Scanner sc = (new Scanner(new File("./Assignment1-GeneFeature/vertebrates.txt")));
            String write_fn = "./Q2/neg.fasta";
            FileWriter writer = createFile(write_fn);
            
            String seq = "", sub_seq = "", line = "";
            int index = 0, idx = 0, id = -1, new_id = -1;
            Boolean seq_complete = false, found_index = false;
            
            while (sc.hasNextLine()) {

                line = sc.nextLine().trim();

                try {
                    sub_seq = line.split(" ", 2)[0];
                    new_id = Integer.parseInt(sub_seq);
                    if (seq != "") {
                        writeEntries(seq, id, index, writer);
                        seq = "";
                        index = 0;
                        seq_complete = false;
                        found_index = false;
                    }
                    id = new_id;

                } catch(NumberFormatException e) {
                    if (sub_seq.charAt(0) == '.' || sub_seq.charAt(0) == 'M') {
                        seq_complete = true;
                    }
                    if (!seq_complete) {
                        seq = seq + sub_seq;
                    } else if (!found_index){
                        idx = sub_seq.indexOf("i");
                        if (idx == -1) {
                            index += sub_seq.length();
                        } else {
                            index += idx;
                            found_index = true;
                        }
                        
                    }
                }

            }

            writeEntries(seq, id, index, writer);            
            writer.close();

        } catch(FileNotFoundException e) {
            System.out.println(e);
        }

    }

    public static void writeEntries(String seq, int id, int index, FileWriter writer) throws IOException {

        String write_seq = "";
        int i, j = 1;
        i = seq.indexOf("ATG", 0);
        while (i != -1) {
            if (i != index) {
                write_seq = "N".repeat(max(0, 99-i)) + seq.substring(max(0, i-99), min(i+102, seq.length())) + "N".repeat(max(0, 102-(seq.length()-i)));
                writer.write(String.format("> %d +1_Index(%d) %d\n%s\n", id, index, j, write_seq));
                j += 1;                    
            }
            i = seq.indexOf("ATG", i+3);
        } 

    }

    public static FileWriter createFile(String fn) {

        File write_file = new File(fn);

        try {
            if (write_file.createNewFile()) {
                System.out.println("File created: " + write_file.getName());
            } else {
                System.out.println("File already exists.");
            }
            return new FileWriter(fn);

        } catch (IOException e) {
            e.printStackTrace();
            FileWriter fw = null; 
            return fw;
        }

    }

}