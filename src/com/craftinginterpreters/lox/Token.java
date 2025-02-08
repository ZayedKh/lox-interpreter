package com.craftinginterpreters.lox; // Defines the package where this class belongs

// The Token class represents a lexical token in the Lox language.
class Token {
    // Fields that store information about the token
    final TokenType type;  // The type of token (e.g., keyword, identifier, number, etc.)
    final String lexeme;   // The actual text representation of the token
    final Object literal;  // The literal value (if applicable, e.g., number or string)
    final int line;        // The line number where the token appears in the source code

    // Constructor: Initializes the token with provided values
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    // Overrides the default toString() method to provide a readable string representation of the token
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
