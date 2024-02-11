package org.example;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        //String driverName = "com.mysql.jdbc.Driver";
        //Class.forName(driverName); // here is the ClassNotFoundException
        //String serverName = "localhost";
        //String mydatabase = "suptodo";
        //String url = "jdbc:mysql://" + serverName + "/" + mydatabase;


        File baseDir = new File(Util.PATH);
        if (!baseDir.isDirectory()) {
            System.out.println(Util.ANSI_RED + "PATH is not a directory, please change the value in Util.java");
            throw new Exception("PATH is not a directory, please change the value in Util.java");
        }


        ManyFunctions manyFunctions = new CheckDirectory().checkDirectory(baseDir);

        for (int i = 0; i < manyFunctions.oneFunctions.size(); i++) {
            OneFunction oneFunction = manyFunctions.oneFunctions.get(i);
            manyFunctions.oneFunctions.subList(i+1, manyFunctions.oneFunctions.size()).stream()
                    .filter(oneFunction2 -> oneFunction2.equals(oneFunction)).findFirst().ifPresent(of -> Util.log(of.name, true));
        }

    }
}