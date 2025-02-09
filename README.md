
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


