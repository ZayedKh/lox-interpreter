
# Lox Interpreter

Resources: Crafting Interpreters by Robert Nystrom.

Creating an Interpreter for the Lox language in Java.

Lox itself is a high-level compact langauge:

```javascript
// A print statement in Lox
print "Hello, world!";
```

The syntax is based off of C, considering that Java is used to implement Lox, it makes sense as Java is also based off of C.

Lox is most similar to JavaScript:

```javascript
// Variable declaration and intialization
var x = 15;
```

Lox is dynamically typed, type checking is done at runtime, operation errors are also detected at runtime. 

## Lox data types

- Booleans
```javascript
true;
false;
```
- Numbers: double-prec floats
```javascript
var num1 = 15;
var num2 = 15.15;
```
- Strings
```javascript
"This is a string"
""
"225"
```
- Nil


## Process

### Lexing/Scanning

The first step in interpreting a language is lexing, we use a scanner to take in a stream of characters and categorize them into lexemes, after which each lexeme is tokenized.

```javascript
var num = 15;
```
Present lexemes:

"var", "num", "=" , "15" and ";"

Tokenize in format:  
Token type | Lexeme | Object literal (if applicable):

VAR var null

IDENTIFIER num null

EQUAL = null

NUMBER 15 15.0

SEMICOLON ; null


We can create our own Scanner class which takes in a stream of chars, and uses a switch statement to covert each lexeme into its respective token:

```java
class Scanner{
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
                ...
                default:
                break;
        }
    }
}

```




