package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*; 

class Scanner {
  // Source of the code we want to scan
  private final String source;
  
  // tokens is of type list that can hold elements of type Token
  // it is an empty arraylist that can hold elements of type Token
  private final List<Token> tokens = new ArrayList<>();
  
  // Init variables to generate token data and token allocation
  private int start = 0; // start index of a lexeme
  private int current = 0; // current char the scanner is on
  private int line = 1; // index of the current char 
  
  // REGEX for Identifier KEYWORDS
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

  
  /* OBJECT GENERATOR */
  Scanner(String source) {
    this.source = source;
  }
  
  /* TOKEN ALLOCATION 
   * returns list of tokens
   * */
  List<Token> scanTokens() {
	// keeps running while we are not at the end of the source code
    while (!isAtEnd()) {
      // We are at the beginning of the next lexeme.
      start = current;
      scanToken();
    }
    // when at the end of the file we append our list of tokens with EOF token to indicate the end of the source code
   
    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }
  
  /* TOKEN SCANNER 
   * Recignizes current token and appends token list
   * Works only for lexemes that are 1 char long !
   * */
  private void scanToken() {
	    char c = advance();
	    switch (c) {
	      case '(': addToken(LEFT_PAREN); break;
	      case ')': addToken(RIGHT_PAREN); break;
	      case '{': addToken(LEFT_BRACE); break;
	      case '}': addToken(RIGHT_BRACE); break;
	      case ',': addToken(COMMA); break;
	      case '.': addToken(DOT); break;
	      case '-': addToken(MINUS); break;
	      case '+': addToken(PLUS); break;
	      case ';': addToken(SEMICOLON); break;
	      case '*': addToken(STAR); break; 
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
        	// Might be a comment
        	// TODO ADD /*...*/ possibility
            if (match('/')) {
              // A comment goes until the end of the line.
              while (peek() != '\n' && !isAtEnd()) advance();
              
            }  else if (match('*')) {
            	// TODO FINISH THIS PART
            	boolean run = true;
            	while(run && !isAtEnd() ) {
            		
            		char next = peek();
            		// We are at the end of the comments
            		if(next == '*' && match('/')){
            			run = false;
            		// New Line
            		}else if(next == '\n'){
            			line++;
            		}else {
            			advance();
            		}
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
          
        // HANDLING LITERALS
          // STRINGS
        case '"': string(); break;
        
          // Numbers
        
        
        default: 
        	if (isDigit(c)) {
                number();
        	}else if (isAlpha(c)){
        		identifier();
            } else {
                Lox.error(line, "Unexpected character.");
             }
	    	  break;
	    }
  }
   
  /* INDENTIFIERS AND KEYWORDS */
  private void identifier() {
	    while (isAlphaNumeric(peek())) advance();

	    String text = source.substring(start, current);
	    TokenType type = keywords.get(text);
	    if (type == null) type = IDENTIFIER;
	    addToken(type);
  }
  /* READING LITERAL NUMBERS
   * We consume as many digits as we find for the integer part of the literal. 
   * Then we look for a fractional part, which is a decimal point (.) followed by 
   * at least one digit. If we do have a fractional part, again, we 
   * consume as many digits as we can find.
   * */
  private void number() {
	    while (isDigit(peek())) advance();

	    // Look for a fractional part.
	    if (peek() == '.' && isDigit(peekNext())) {
	      // Consume the "."
	      advance();

	      while (isDigit(peek())) advance();
	    }

	    addToken(NUMBER,
	        Double.parseDouble(source.substring(start, current)));
  }
  /* READING LITERAL STRINGS */
  private void string() {
	  // Keep adding the char to the string
	    while (peek() != '"' && !isAtEnd()) {
	    	// Multi-line strings!
	      if (peek() == '\n') line++;
	      advance();
	    }

	    if (isAtEnd()) {
	      Lox.error(line, "Unterminated string.");
	      return;
	    }

	    // The closing ".
	    advance();

	    // Trim the surrounding quotes. We don't want " in our string value
	    // If Lox supported escape sequences like \n, we’d unescape those here.
	    String value = source.substring(start + 1, current - 1);
	    addToken(STRING, value);
  }
  
  /* HELPER FUNCTIONS FOR scanToken() */
  private boolean match(char expected) {
	// EOF?
    if (isAtEnd()) return false;
    
    if (source.charAt(current) != expected) return false;
    // next char
    current++;
    return true;
  }
  
  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }
  
  /* HELPER FUNCITONS FOR LITERAL NUMBERS*/
  private char peekNext() {
	    if (current + 1 >= source.length()) return '\0';
	    return source.charAt(current + 1);
  } 
  
  private boolean isDigit(char c) {
	    return c >= '0' && c <= '9';
  } 
  
  private boolean isAlpha(char c) {
	    return (c >= 'a' && c <= 'z') ||
	           (c >= 'A' && c <= 'Z') ||
	            c == '_';
  }

  private boolean isAlphaNumeric(char c) {
	    return isAlpha(c) || isDigit(c);
  }
  /* EOF CHECKER 
   * returns boolean if EOF is reached
   * */
  private boolean isAtEnd() {
    return current >= source.length();
  }
  
  /* HELPER FUNCTIONS FOR scanToken() */
  private char advance() {
	  // next char in the source
    return source.charAt(current++);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
	// takens lexeme from source code
    String text = source.substring(start, current);
    // adds new token to list of type Token ( is of class Token ) 
    tokens.add(new Token(type, text, literal, line));
  }
  
}

