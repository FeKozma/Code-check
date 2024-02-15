package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteObjectToFile {

    File file;
    public WriteObjectToFile() {
        try {
            for (int i = 0; i < 100; i++) {
                file = new File("result_"+i+".txt");
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                    i = 100;
                } else {
                    System.out.println("File already exists, try to create next file.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void write(String obj) {
        try {
            FileWriter myWriter = new FileWriter(file.getName(), true);
            myWriter.write(obj.replace("\n", "\\n") + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
