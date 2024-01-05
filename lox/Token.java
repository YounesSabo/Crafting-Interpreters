package com.craftinginterpreters.lox;
/* Data class containing all the info of a token
	 * Datatype of the token
	 * Lexeme : the token itself
	 * Object the token represents
	 * line where it occurs
 * EXTRA INFO
 	* 'final' keyword indicates that the variable's value cannot be changed 
 	* once it has been assigned. It essentially makes the variable a constant. 
 	* 
 	* TokenType enumeration of allowed tokens defined in the TokenType.java file
 	* 
 	* Object type is to most general type of variable, it can assign any type of variable
 * */
class Token {
  final TokenType type;
  final String lexeme;
  final Object literal;
  final int line; 
  
  // function to generate the class for a token 
  Token(TokenType type, String lexeme, Object literal, int line) {
    this.type = type;
    this.lexeme = lexeme;
    this.literal = literal;
    this.line = line;
  }

  public String toString() {
    return type + " " + lexeme + " " + literal;
  }
}
