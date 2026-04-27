/**
 * This is the parser class. This reads the input tokens, and follows a top-down grammar to build parse trees. 
 */

import java.util.*;

public class Parser {
	private ArrayList<Token> tokenList;
	private int curToken = 0;
	SymbolTable symTable = new SymbolTable();
	
	Parser (ArrayList<Token> tokenList) {this.tokenList = tokenList;}
	
	private Token peek() {
		if (curToken >= tokenList.size()) return new Token("EOF", "");
		return tokenList.get(curToken);} //Peeks the current token
	
	private Token advance() {return tokenList.get(curToken++);} //Advances to next token
	
	private Token consume(String type, String message) { //Advances to the next token if the type is correct
        if (peek().getType().equals(type)) return advance();
        throw new RuntimeException(message);
    }
	
	
	Program parse() throws Exception{ //program -> statement*
		Program program = new Program();
		
		while (curToken < tokenList.size()) {
			program.statements.add(statement());
		}
		return program;
	}
	
	private Stmt statement() throws Exception{ //statement -> assignment ";" | keyword ";"
		Stmt stmt;
		if (peek().getType().equals("keyword")){stmt = keyword();}
		
		else if (peek().getType().equals("id")) {
			stmt = assignment();
			consume("eos", "Semicolon expected to end statement");
			}
		
		else throw new Exception ("Invalid Statment, must start with keyword or identifier");
		return stmt;
	}
	
	private Stmt assignment() throws Exception{ //assignment -> id Assign expression
		Token name = consume("id", "Expected variable name");
		symTable.lookup(name.getValue()); //confirms variable exists
		consume("Assign", "Expected Assignment");
		Expr expr = expression();
		
		return new AssignmentStmt(name.getValue(), expr);
	}
	
	private Stmt keyword() throws Exception { //keyword -> varDec | Input | Print | IfThen
		Stmt key;
		if (peek().getValue().equals("INT") || peek().getValue().equals("DOUBLE")) {key = varDec();}
		
		else if (peek().getValue().equals("PRINT")) {key = print();}
		
		else if (peek().getValue().equals("IF")) {key = ifThen();}
		
		else if (peek().getValue().equals("INPUT")) {key = input();}
		
		else throw new Exception ("Invalid keyword");
		return key;
	}
	
	private Stmt input() throws Exception { //Input -> "INPUT" id
		consume("keyword", "Expected INPUT statement");
		Token var = consume("id","Identifier for Input expected");
		
		symTable.lookup(var.getValue());
		
		consume("eos", "Expected semicolon after INPUT statement");
		return new InputStmt(var.getValue());
	}
	
	private Stmt varDec() throws Exception { //varDec -> type id | type id Assign assignment
		Token type = consume("keyword", "Keyword expected");
		Token varName = consume("id", "Expected identifier");
		symTable.declare(varName.getValue(), type.getValue());
		
		Expr expr = null;
		if (peek().getType().equals("Assign")) {
			consume ("Assign", "Assignment token expected");
			expr = expression();
			}
		consume ("eos","Semicolon expected after variable declaration");
		return new VarDeclStmt(type.getValue(),varName.getValue(),expr);
	}
	
	private Stmt print() throws Exception { //Print -> "PRINT" (id | int | double)
		consume("keyword", "Print statement expected");
		Token output = new Token();
		if (peek().getType().equals("id")) {output = consume("id", "Identifier expected");}
		
		else if (peek().getType().equals("int")) {output = consume("int","Integer expected");}
		
		else if (peek().getType().equals("double")) {output = consume("double","Double expected");}
		
		else throw new Exception ("Invalid Print objet");
		
		consume ("eos", "Semicolon expected after print statement");
		return new PrintStmt(output.getValue());
	}
	
	private Stmt ifThen() throws Exception { //IfThen -> "IF" condition "THEN" statement
		Expr left;
		Token comp;
		Expr right;
		Stmt then;
		if (peek().getValue().equals("IF")) {
			consume("keyword", "Keyword expected");
			left = expression();
			comp = consume("comp", "Comparision operator expected");
			right = expression();
			if (peek().getValue().equals("THEN")) {
				consume("keyword", "Keyword expected");
				then = statement();
			}
			else throw new Exception("Invalid keyword: THEN expected");
			
		}
		else throw new Exception("Invalid Keyword in IF THEN statement");
		return new IfStmt (left, comp.getValue(), right, then);
	}
	
	private Expr expression() throws Exception{ // expression -> term ((+|-) term)*
		Expr node = term();
		
		while (peek().getValue().equals("+") || peek().getValue().equals("-")) {
			Token operator = consume("aop", "Arithmetic Operator expected");
			Expr right = term();
			node = new BinaryExpr(node, operator.getValue(), right);
		}
		return node;
	}
	
	private Expr term() throws Exception { //term -> factor ((/|*)factor)*
		Expr node = factor();
		
		while (peek().getValue().equals("/") || peek().getValue().equals("*")) {
			Token operator = consume("aop", "Arithmetic Operator expected");
			Expr right = factor();
			node = new BinaryExpr(node, operator.getValue(), right);
		}
		return node;
		
	}
	
	private Expr factor() throws Exception { //factor -> int | double | id
		Token token = peek();
		
		if (token.getType().equals("int")) {
			consume("int", "Integer expected");
			return new IntExpr(token.getValue());
		}
		if (token.getType().equals("double")) {
			consume("double","Double expected");
			return new DoubleExpr(token.getValue());
		}
		if (token.getType().equals("id")) {
			consume("id","Identifier expected");
			return new VariableExpr(token.getValue());
		}
		throw new Exception ("Invalid token: Number or Identifier expected.");
	}


}
