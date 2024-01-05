package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
	
	static boolean hadError = false;
	// check if code has an error
	if(hadError) System.exit(65);
	
	public static void main(String[] args) throws IOException {
		
		if (args.length > 1) {
			// Had to many commands when running the file
	      System.out.println("Usage: jlox [script]");
	      System.exit(64); 
	      
	    } else if (args.length == 1) {
	    	
	    	// Run file
	      runFile(args[0]);
	      
	    } else {
	    	// Keep in prompt when running nothing
	      runPrompt();
	    }

	}

}

/* RUNNING CODE USING CMD PROMPT 
 * run .jlox file with path in command prompt to run file with our interpreter
 */
private static void runFile(String path) throws IOException {
	// Stores path char in array > forming a string
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    // Run file 
    run(new String(bytes, Charset.defaultCharset()));
  }

/* RUNNING CODE INTERACTIVELY 
 * Will open a prompt where you put code to execute 
 */
private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);
    
    // while true we run the prompt | for(;;) == while (true)
    for (;;) { 
      System.out.print("> ");
      // Read input from the user
      String line = reader.readLine();
      // When user type <CTRL-D> readline() returns NULL and we break the prompt
      if (line == null) break;
      // run the new input
      run(line);
      // Reset error flag > dont kill session when user makes a mistake!
      hadError = false;
    }
  }

/* INPUT HANDELING */
private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    // For now, just print the tokens.
    for (Token token : tokens) {
      System.out.println(token);
    }
  }

/* ERROR HANDELING */
static void error(int line, String message) {
    report(line, "", message);
  }

/* ERROR PRINTING */
private static void report(int line, String where,
                             String message) {
	// where will be most of the times empty
	// example output: Error: Unexpected "," somewhere in your code. Good luck finding it!
    System.err.println(
        "[line " + line + "] Error" + where + ": " + message);
    
    // Primary reason for the error handler is to alter ahdError!
    hadError = true;
    }

































