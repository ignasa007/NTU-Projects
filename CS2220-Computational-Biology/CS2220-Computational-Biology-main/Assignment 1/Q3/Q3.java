package Q3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Q3 {

    public static void main(String[] args) throws IOException {

        try {

            String[] base_pairs = new String[] {"A", "C", "G", "T"};
            String seq = "NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNCCGTCAGAGCGCCGACACTCTTCTCTGTGCGAGCGAGCCGCCGACCGCCAAGCAAAATGGGAAATGAGGCAAGTTATCCTTTGGAAATGTGCTCACACTTTGATGCAGATGAAATTAAAAGGCTAGGAAAGAGATTTAAGAAGCTCGATTTGGACAAT";
            Boolean pos = true;

            String write_fn = "./Q3/count_3_gram.fasta";
            FileWriter writer = createFile(write_fn);

            String upstream = seq.substring(0, 99);
            features(upstream, base_pairs, writer);

            String downstream = seq.substring(102, 201);
            features(downstream, base_pairs, writer);

            writer.write(pos ? "pos" : "neg");
            writer.close();
   
        } catch(FileNotFoundException e) {
            System.out.println(e);
        }

    }

    public static void features(String stream, String[] base_pairs, FileWriter writer) throws IOException {

        for (int i=0; i<base_pairs.length; i++) {
            for (int j=0; j<base_pairs.length; j++) {
                for (int k=0; k<base_pairs.length; k++) {
                    String gram_3 = base_pairs[i] + base_pairs[j] + base_pairs[k];
                    if (gram_3.equals("ATG")) {
                    }
                    int count = count(stream, gram_3);
                    writer.write(String.format("%d,", count));
                }
            }
        }

    }

    public static int count(String stream, String gram_3) {

        int count = 0;
        
        for (int pos=0; pos<stream.length(); pos+=3) {
            if (stream.substring(pos, pos+3).equals(gram_3)) {
                count += 1;
            } 
        }

        return count;

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