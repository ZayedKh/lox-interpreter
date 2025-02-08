package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


public class Lox {

    public static void main(String[] args) {
        if(args.length > 1){
            System.out.println("Usage: jlox [script");
            System.exit(64);
        } else if(args.length == 1){
            runFile(args[0]);
        } else{
            runPrompt();
        }
    }

    // Lox is a scripting file so executes directly from source.
    // Can support two ways of running. Starting jlox from CLI, or interactive running.


    // In this method we give the method the path to the file,
    // it then reads the file and runs it
    private static void runFile(String path) throws IOException{
        byte [] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }


    // Prompt which allows you to enter code one line at a time.
    // REPL, read, evaluate, print, loop.
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){ // Infinite for loop lacking break condition
            System.out.println("> ");
            String line = reader.readLine(); // Reads a line of input from user on CLI
            /*
                Typing ctrl+d kills an interactive command-line app, this signals an end of file
                condition to the program. When ctrl+d is pressed, readLine returns null. Thus we check
                for that to exit the loop.
             */


            if(line == null) {
                break; // break if line is null
            }
            run (line);
        }
    }


    // Core function to actually run source code.
    private static void run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // For now we just print out tokens to debug.
        for(Token token : tokens){
            System.out.println(token);
        }
    }
}