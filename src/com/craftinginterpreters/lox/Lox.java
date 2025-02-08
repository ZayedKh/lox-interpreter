package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.craftinginterpreters.lox.Scanner;


public class Lox {

    /*
      Line of lox code:
      var language = "lox";

      5 lexemes in this expression. Bundling lexemes together results in a token
     */

    // Used so that we don't execute code that has a known error.
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
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

        if(hadError){
            System.exit(65); // exit with non-zero exit code
        }
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
                condition to the program. When ctrl+d is pressed, readLine returns null, thus we check
                for that to exit the loop.
             */


            if(line == null) {
                break; // break if line is null
            }
            run (line);
            hadError = false; // If user makes mistake, shouldn't kill their entire session.
        }
    }


    // Core function to actually run source code.
    private static void run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // For now, we just print out tokens to debug.
        for(Token token : tokens){
            System.out.println(token);
        }
    }


    // Error handling

    static void error(int line, String message){
        report(line, "", message);
    }

    private static void report(int line, String where, String message){
        System.err.println("[line " + line +  "] Error" + where + ": " + message);
        hadError = true;
    }
}