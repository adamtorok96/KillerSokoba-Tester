package hu.saturnalmighty.tester;

import java.io.*;
import java.util.LinkedList;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
	    if( args.length != 3 ) {
	        usage();

	        exit(1);
        }

        System.out.println("Program: " + args[0]);
	    System.out.println("Test command file: " + args[1]);
	    System.out.println("Test validation file: " + args[2]);

        LinkedList<String> tests = null;

        try {
            tests = getTests(args[2]);
        } catch (IOException e) {
            System.out.println("Failed to read test file!");

            exit(1);
        }

        try {
            Process process = Runtime.getRuntime().exec(String.format("java -jar %s %s", args[0], args[1]));

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            int n = 0;

            while( (line = br.readLine()) != null ) {
                if( tests.size() <= n ) {
                    System.out.println("F: Missing tests or too many output!");
                    exit(1);
                }

                System.out.print('\t');

                if( !line.equals(tests.get(n)) ) {
                    System.out.println(String.format("F %s != %s", line, tests.get(n)));

                    exit(1);
                }

                System.out.println(String.format("S %s = %s", line, tests.get(n)));

                n++;
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void usage()
    {
        System.out.println("Usage: /program <program> <test.cmds> <test.vlds>");
    }

    private static LinkedList<String> getTests(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));

        LinkedList<String> tests = new LinkedList<>();
        String line;

        while( (line = br.readLine()) != null ) {
            tests.add(line);
        }

        br.close();

        return tests;
    }
}
