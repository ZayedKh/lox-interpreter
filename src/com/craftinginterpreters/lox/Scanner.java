package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    // start and current are offsets that index into the string
    // start points to the first char in the lexeme being scanned, while current
    // points to the char being considered.
    // line tracks what source line current is on, so we can produce tokens that know
    // their locations.
    // this is also useful for debugging.
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    // we store the raw source code as a string - we have a list ready to fill with
    // generated tokens.


    // scanner works its way through the code, adding tokens until it runs out of characters
    // it then appends a final 'end of file' token to indicate the end.
    // we depend on a few fields to keep track of the scanners location in the source code.
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    // in each loop we scan a single token - we can start with the single char lexemes
    // adding a token for each single char.
    private void scanToken() {
        char c = advance();

        // switch statement to tokenize lexemes.
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
        }
    }

    // helper function to consumer next character in source and return it to
    // scanToken()
    private char advance(){
        return source.charAt(current++);
    }

    // helper method for adding token.
    // this method grabs the current lexeme and creates a new token for it.
    private void addToken(TokenType type){
        addToken(type, null);
    }


    // overloaded method of addToken() to deal with literal tokens
    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));

    }
}








