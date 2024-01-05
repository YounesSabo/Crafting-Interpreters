// This file is not part of the lox package, NOT PART OF THE INTERPRETER
package com.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
  public static void main(String[] args) throws IOException {
    if (args.length != 0) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }
    // TODO CHANGE THIS TO ARGC[0] AND LEARN HOW TO RUN IN CMD WITH DESIRED PATH
    String outputDir = "C:\\Users\\saboy\\Documents\\Crafting Interpreter\\Chapters\\H4\\Scanner\\src\\com\\craftinginterpreters\\tool";
    // Description of each type and fields to generate the expressions 
    // We pass 'Expr' as an argument instead of hardcoding the name because 
    // we’ll add a separate family of classes later for statements.
    defineAst(outputDir, "Expr", Arrays.asList(
    	      "Binary   : Expr left, Token operator, Expr right",
    	      "Grouping : Expr expression",
    	      "Literal  : Object value",
    	      "Unary    : Token operator, Expr right"
    	    ));
  }
  
  /* declares each field in the class body. It defines a constructor 
   * for the class with parameters for each field and initializes 
   * them in the body.
   * */
  private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
	    // Generates string outputdir/Expr.java  
	    String path = outputDir + "/" + baseName + ".java";
	    // Init writing to file 
	    PrintWriter writer = new PrintWriter(path, "UTF-8");

	    writer.println("package com.craftinginterpreters.lox;");
	    writer.println();
	    writer.println("import java.util.List;");
	    writer.println();
	    writer.println("abstract class " + baseName + " {");
	    // The AST classes.
	    for (String type : types) {
	      String className = type.split(":")[0].trim();
	      String fields = type.split(":")[1].trim(); 
	      defineType(writer, baseName, className, fields);
	    }
	    writer.println("}");
	    writer.close();
  }
  
  private static void defineType(PrintWriter writer, String baseName,String className, String fieldList) {
	    writer.println("  static class " + className + " extends " + baseName + " {");

	    // Constructor.
	    writer.println("    " + className + "(" + fieldList + ") {");

	    // Store parameters in fields.
	    String[] fields = fieldList.split(", ");
	    for (String field : fields) {
	      String name = field.split(" ")[1];
	      writer.println("      this." + name + " = " + name + ";");
	    }

	    writer.println("    }");

	    // Fields.
	    writer.println();
	    for (String field : fields) {
	      writer.println("    final " + field + ";");
	    }

	    writer.println("  }");
  }
}
