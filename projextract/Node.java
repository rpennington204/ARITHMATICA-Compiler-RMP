/**
 * This is the class that represents the nodes of the parse trees
 */
import java.util.*;
abstract class Node { }
class Expr extends Node { }//Nodes of type Expression (ints, doubles, and variables that represent them)

		class IntExpr extends Expr { //ints
			String value;
			IntExpr(String value) {this.value = value;}
		}
		
		class DoubleExpr extends Expr { //doubles
			String value;
			DoubleExpr(String value) {this.value = value;}
		}
		
		class VariableExpr extends Expr { //variables
			String name;
			VariableExpr (String name) {this.name = name;}
		}
		
		class BinaryExpr extends Expr { //Binary Operations
			Expr left;
			String operand;
			Expr right;
			
			BinaryExpr (Expr left, String operand, Expr right){
				this.left = left;
				this.operand = operand;
				this.right = right;
			}
		}
	
	
class Stmt extends Node {}  //Nodes for statements
		class AssignmentStmt extends Stmt {
			String variable;
			Expr expression;
			
			AssignmentStmt (String variable, Expr expression){ //assignment statement
				this.variable = variable;
				this.expression = expression;
			}
		}
		
		class PrintStmt extends Stmt { //print statement
			String value;
			PrintStmt(String value){ this.value = value;}
		}
		
		class InputStmt extends Stmt { //INPUT statement
			String variable;
			InputStmt(String variable){this.variable = variable;}
		}
		
		class IfStmt extends Stmt { //if statement
			Expr left;
			String comp_operator;
			Expr right;
			Stmt thenBranch;
			
			IfStmt(Expr left, String comp_operator, Expr right, Stmt thenBranch){
				this.left = left;
				this.comp_operator = comp_operator;
				this.right = right;
				this.thenBranch = thenBranch;
			}
		}
		
		class VarDeclStmt extends Stmt { //variable declaration statement
		    String type;     
		    String name;
		    Expr initializer; 

		    VarDeclStmt(String type, String name, Expr initializer) {
		        this.type = type;
		        this.name = name;
		        this.initializer = initializer;
		    }
		}
	
	class Program extends Node {
	    List<Stmt> statements = new ArrayList<>();
	    Program(){}
	    Program (List<Stmt> statements){ this.statements = statements;}
	}
	
	

