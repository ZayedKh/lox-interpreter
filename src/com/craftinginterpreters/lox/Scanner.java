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

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

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
                    // while comments are lexemes, we don't want to deal with them, so we don't call
                    // addToken(), after we move to a new line, start gets reset and the lexeme is lost.
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                line++;
                break;
            // case to handle string - we know that they always start with "
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    // in case of chars that aren't represented in lox throw this error.
                    // program keeps scanning to find any other errors
                    // since hadError is set to true, we never actually execute the code
                    // we just notify the user of all the errors they find in the code
                    Lox.error(line, "Unexpected character.");
                }
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

    // method to lookahead, doesn't actually consumer char.
    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private void string() {
        // while we don't find the ending quotation of the string
        while (peek() != '"' && !isAtEnd()) {
            // if there is a new line char
            if (peek() == '\n') {
                line++;
            }
            // keep consuming chars
            advance();
        }

        // throw error if we reach end of program without terminating the string
        if (isAtEnd()) {
            Lox.error(line, "unterminated string.");
            return;
        }

        // consume the closing "
        advance();

        // trim the beginning " and ending "
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        // while the next char is also a digit, advance
        while (isDigit(peek())) {
            advance();
        }

        // if there is a decimal, and the digit after the decimal is a number as well
        if (peek() == '.' && isDigit(peekNext())) {
            // consume the '.'
            advance();
            // while the next char is also a digit, advance
            while (isDigit(peek())) {
                advance();
            }
        }

        // add a number token, use double built in method to convert string to double.
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    // while peek looks ahead to one char, peek ahead looks forward to two chars
    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }

        return source.charAt(current + 1);
    }

    private void identifier(){
        while(isAlphaNumeric(peek())) {
            advance();
        }

        // get the string between start and current
        // it is either a reserved keyword, or it is an identified
        String text = source.substring(start, current);

        // locate if the string exists in our keyword map
        TokenType type = keywords.get(text);

        // if it doesn't exist we know it is an identifier.
        if(type == null){
            type = IDENTIFIER;
        }


        addToken(type);
    }

    private boolean isAlphaNumeric(char c){
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAlpha(char c) {
        return ((c >= 'a' && c <= 'z') ||  (c >= 'A' && c <= 'Z') || (c == '_'));
    }
}
