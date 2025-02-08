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

            // here we have operators, but since operators are mostly used in tandem
            // (!=, ==, >=) we can't exactly use single char scanning
            // therefore we need to check the second char and see if it creates a valid lexeme/token
            // ternary operators are used for the implementation.
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                // special handling due to // comment possibility
                if (match('/')) {
                    // if it is a comment, we just consume all chars until the next line
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;
            default:
                // in case of chars that aren't represented in lox throw this error.
                Lox.error(line, "Unexpected character.");
                // program keeps scanning to find any other errors
                // since hadError is set to true, we never actually execute the code
                // we just notify the user of all the errors they find in the code
                break;
        }
    }

    // helper function to consumer next character in source and return it to
    // scanToken()
    private char advance() {
        return source.charAt(current++);
        // we return the CURRENT char, THEN increment.
    }

    // helper method for adding token.
    // this method grabs the current lexeme and creates a new token for it.
    private void addToken(TokenType type) {
        addToken(type, null);
    }


    // overloaded method of addToken() to deal with literal tokens
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));

    }

    private boolean match(char expected) {
        // if we are at the end we know there are no more chars - return false
        if (isAtEnd()) {
            return false;
        }
        // if the
        if (source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }
    private char peek(){
        if(isAtEnd()){
            return '\0';
        }
        return source.charAt(current);
    }
}








