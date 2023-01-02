# Compiler Construction Project

## Objective
This is my compiler construction project which is implemented for only lexical and syntax analyzer phase. For syntax analyzer the provided rules are: 
E -> E + T ,
E -> T ,
T -> T * F ,
T -> F ,
F -> ( E ) ,
F -> id 

The code is written in java. Along with that for GUI Java Swing is used.

## Requirements
Recognize following tokens for the lexemes in the input source program:
– Keywords: int, char, string, if, else, do, while.
– Identifiers (ID): letter followed by zero or more letters or digits.
– String Literal: (SL): anything that is surrounded by double quotes, for example “Hello”
– Integer value (IV): example: 155 (unsigned only)
– Relational Operators (ROP): <, <=, ==, <>, >= and >
– Arithmetic Operators (AOP): +, -, *, / 
– Other Operators (OOP): assignment: =, parenthesis: (, ), braces: {, }, line terminator: ;

• Upon recognizing a token, lexical analyzer should output the lexeme, the token name and the 
attribute value based on the above table.
• Store tokens in symbol table based on the above table.
• Should properly handle the comments enclosed in /* and */ or following // .
• Should properly handle the white spaces (blanks, tabs or newlines).
• Also output all the data stored in the symbol table.
• If the input to your compiler contains something other than the lexemes mentioned in the 
table above, then it should generate and display the error with:
– The line number
– The unrecognized lexeme in the source code and 
– A meaningful error message
• For Syntax Analysis, if the input contains a legal expression involving operators + and * and 
parenthesis ( and ), your compiler should accept it and output “Compiled Successfully”
message, otherwise give out error message with correct line number and a meaningful error 
message:
Use the CFG and the Parsing Table given below for Syntax Analysis of the input.

![image](https://user-images.githubusercontent.com/74453775/210228733-5bbe830a-2785-4672-9e06-f91a40077e62.png)


## Here are the screen shots of GUI:
![image](https://user-images.githubusercontent.com/74453775/210228054-7347dda8-b5a9-4074-b7d7-536374587d95.png)

### Lexical Analyzer Working:
![image](https://user-images.githubusercontent.com/74453775/210228217-6d20ed3b-161f-47eb-a0c1-87dc44111b81.png)
![image](https://user-images.githubusercontent.com/74453775/210228272-0ba2e943-7df8-46d9-82c2-26a1262cb010.png)

### Incase of Lexical Error:
![image](https://user-images.githubusercontent.com/74453775/210228383-7e859544-a42c-45b2-8b71-8bcd1c50bfcb.png)
![image](https://user-images.githubusercontent.com/74453775/210228408-7e70298a-5ae0-4cfc-9118-6a1181ef5ba4.png)

### Syntax Analyzer:
![image](https://user-images.githubusercontent.com/74453775/210228462-f2b978ef-cbed-42a6-8d1c-4952ab7df184.png)

### Incase of Syntax Error:
![image](https://user-images.githubusercontent.com/74453775/210228564-5752a5fd-895a-4baf-a83e-5f4edda74fec.png)
![image](https://user-images.githubusercontent.com/74453775/210228596-d7589fcb-29d3-4e4c-8686-ae2f68d2ed20.png)
