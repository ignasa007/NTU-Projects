package Q4;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Q4 {

    public static void main(String[] args) throws IOException {

        try {

            Scanner sc = (new Scanner(new File("./Assignment1-GeneFeature/Inframe_3_Gram.arff")));
            String write_fn = "./Q4/count_3_gram.fasta";
            FileWriter writer = createFile(write_fn);

            String line = sc.nextLine();
            line = sc.nextLine();

            ArrayList<String> attributes = new ArrayList<String>();
            line = sc.nextLine().trim();
            while (!line.equals("")) {
                line = line.split(" ")[1];
                if (line.equals("Class")) {
                    attributes.add(line);
                } else {
                    attributes.add(line.split("_", 2)[1].replace("_", " "));
                }
                line = sc.nextLine().trim();
            }

            line = sc.nextLine();
            line = sc.nextLine();

            line = sc.nextLine().trim();
            String[] values = line.split(",");
            for (int i=0; i<values.length; i++) {
                writer.write(String.format("%s - %s\n", attributes.get(i), values[i]));
            }

            writer.close();
   
        } catch(FileNotFoundException e) {
            System.out.println(e);
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