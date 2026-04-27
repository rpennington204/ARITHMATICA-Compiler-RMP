Toy Language ARITHMATIC Compiler

This is a compiler program that takes in a string of a toy language, ARITHMATICA. 
- This language only has the types Int and Double
- It supports printing variables or numbers (PRINT), taking user input for a variable (INPUT), and an if-then conditional (IF X==Y THEN X = 10)
- It supports the basic arithmatic operations of addition, subtraction, multiplication, and divition
- If a minus preceeds a number without whitespace, it makes that value negative (ex: -10). If there's a space, it is a subtraction (ex: 10 - 5)
- Every statement ends with a ";"
- There is no mixing int and double types in operations
- The input string that represents the source language is in the "TestScan" class.

The output is a list of string JVM Bytecode instructions. 
