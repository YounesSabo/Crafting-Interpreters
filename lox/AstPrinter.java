package com.craftinginterpreters.lox;

// R will now be of type String when iomplementing the funcitons from the Expr.java file
// Implement the visitor interface
class AstPrinter implements Expr.Visitor<String>{
	String print(Expr expr) {
		return expr.accept(this);
	}
	
	// Overwriting the visit functions for each type so taht each type will print their tree when visiting
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
      return parenthesize(expr.operator.lexeme,
                          expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
      return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
    	// Easy cause we just have to print the exact value here
      if (expr.value == null) return "nil";
      return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
      return parenthesize(expr.operator.lexeme, expr.right);
    }
	
	private String parenthesize(String name, Expr... exprs) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("(").append(name);
		for (Expr expr : exprs) {
		  builder.append(" ");
		  // Loop through the expressions until we find the literal value
		  builder.append(expr.accept(this));
		}
		builder.append(")");
		
		    return builder.toString();
	}
	
	// Parser for the moment
	public static void main(String[] args) {
	    Expr expression = new Expr.Binary(
	        new Expr.Unary(
	            new Token(TokenType.MINUS, "-", null, 1),
	            new Expr.Literal(123)),
	        new Token(TokenType.STAR, "*", null, 1),
	        new Expr.Grouping(
	            new Expr.Literal(45.67)));

	    System.out.println(new AstPrinter().print(expression));
	  }	
	
}
