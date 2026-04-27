/**
 * This is the bytecode generator class. This checks the nodes of a list of parse trees (the statements).
 * From these parse trees, it emits ByteCode instructions that are stored in order in a list. 
 */
import java.util.*;
public class BytecodeGenerator {
	private List<String> instructions = new ArrayList<>();
	private SymbolTable symbols;
	private int labelCounter = 0;
	private boolean scnrInit = false;
	
	public List<String> generateCode(Program program) throws Exception{ //Each statement is an AST, this iterates through a list of parse trees
		for (Stmt stmt : program.statements) {
			generateStmt(stmt);
		}
		return instructions;
	}
	
	public BytecodeGenerator(SymbolTable symbols) {this.symbols = symbols;}

	private void generateStmt(Stmt stmt) throws Exception {
        if (stmt instanceof AssignmentStmt a) { //Generates assignment code
            Symbol sym = symbols.lookup(a.variable);
            
            String exprType = generateExpr(a.expression);
            
            if (!sym.type.equals(exprType)) {
                throw new RuntimeException(
                    "Type error: cannot assign " + exprType +
                    " to variable '" + a.variable + "' of type " + sym.type
                );
            }
        	
            emitStore(a.variable);
        }

        else if (stmt instanceof VarDeclStmt v) { //generates variable declaration code
            if (v.initializer != null) {
                String exprType = generateExpr(v.initializer);
                
                if (!v.type.equals(exprType)) {
                    throw new RuntimeException(
                        "Type error: cannot initialize variable '" + v.name +
                        "' of type " + v.type + " with " + exprType
                    );
                }
                
                emitStore(v.name);
            }
        }

        else if (stmt instanceof PrintStmt p) { //generates print statement code
            emit("GETSTATIC java/lang/System.out : Ljava/io/PrintStream;");
        	emitLoad(p.value);
        	if (symbols.lookup(p.value).type.equals("INT")) {emit("INVOKEVIRTUAL java/io/PrintStream.println (I)V");}
        	if (symbols.lookup(p.value).type.equals("DOUBLE")) {emit("INVOKEVIRTUAL java/io/PrintStream.println (D)V");}
        }

        else if (stmt instanceof IfStmt i) { //calls class to generate if statement
            generateIf(i);
        }
        
        else if (stmt instanceof InputStmt i) {
        	Symbol sym = symbols.lookup(i.variable);
        	int scnrInd = -1;
        	
        	if (!scnrInit) {
        		scnrInd = symbols.getCurSlot();
        		symbols.setCurSlot(scnrInd + 1);
        		scnrInit = true;
        	}
        	
        	emit("NEW java/util/Scanner");
        	emit("DUP");
        	emit("GETSTATIC java/lang/System in Ljava/io/InputStream;");
        	emit("INVOKESPECIAL java/util/Scanner <init> (Ljava/io/InputStream;)V");
        	emit("ASTORE "  + scnrInd);
        	emit("ALOAD " + scnrInd);
        	
        	
        	if (sym.type.equals("INT")) {
        		emit("INVOKEVIRTUAL java/util/Scanner nextInt ()I"); 
                emit("ISTORE " + sym.slot);
                }
        	else {
        		emit("INVOKEVIRTUAL java/util/Scanner nextDouble ()D");
        		emit("DSTORE " + sym.slot);
        	}
        }
        	
    }
	
	private String generateExpr(Expr expr) throws Exception {
        if (expr instanceof IntExpr i) { //Generates int push code
            emit("BIPUSH " + i.value);
            return "INT";
        }

        else if (expr instanceof DoubleExpr d) { //generates double push code
            emit("LDC2_W " + d.value);
            return "DOUBLE";
        }

        else if (expr instanceof VariableExpr v) { //generates variable load code
        	Symbol sym = symbols.lookup(v.name);

            emitLoad(v.name);
            return sym.type;
        }

        else if (expr instanceof BinaryExpr b) { //generates the bytecode for arithmetic operations
            String leftType = generateExpr(b.left);
            String rightType = generateExpr(b.right);
            
            if (!leftType.equals(rightType)) {
                throw new RuntimeException(
                    "Type error: cannot mix " + leftType + " and " + rightType
                );
            }
            
            String resultType = leftType;

            switch (b.operand) {
                case "+" -> emit(resultType.equals("INT") ? "IADD" : "DADD");
                case "-" -> emit(resultType.equals("INT") ? "ISUB" : "DSUB");
                case "*" -> emit(resultType.equals("INT") ? "IMUL" : "DMUL");
                case "/" -> emit(resultType.equals("INT") ? "IDIV" : "DDIV");
            }
            return resultType;
        }
        throw new RuntimeException("Unkown Expression");
    }
	
	 private void emitLoad(String value) throws Exception { //helper to emit load functions
		 Symbol sym = symbols.lookup(value);

		    switch (sym.type) {
		        case "INT" -> emit("ILOAD " + sym.slot);
		        case "DOUBLE" -> emit("DLOAD " + sym.slot);
		    }
	    }
	 
	 private void emitStore(String name) throws Exception { //helper to emit store functions
		    Symbol sym = symbols.lookup(name);

		    switch (sym.type) {
		        case "INT" -> emit("ISTORE " + sym.slot);
		        case "DOUBLE" -> emit("DSTORE " + sym.slot);
		    }
		}
	/* 
	 private void emitConst(Expr expr) {
		    if (expr instanceof IntExpr i) {
		        emit("BIPUSH " + i.value);
		    }
		    else if (expr instanceof DoubleExpr d) {
		        emit("LDC2_W " + d.value); 
		    }
		}

	 private boolean isNumber(String s) {
	        return s.matches("-?\\d+(\\.\\d+)?");
	 }
	 */
	 private void generateIf(IfStmt i) throws Exception { //generates code for if-then statements
	        generateExpr(i.left);
	        generateExpr(i.right);

	        String labelEnd = newLabel();

	        switch (i.comp_operator) {
	            case "==" -> emit("IF_ICMPNE " + labelEnd);
	            case "!=" -> emit("IF_ICMPEQ " + labelEnd);
	            case "<"  -> emit("IF_ICMPGE " + labelEnd);
	            case ">"  -> emit("IF_ICMPLE " + labelEnd);
	        }

	        generateStmt(i.thenBranch);

	        emit(labelEnd + ":");
	    }
	 
	  private void emit(String instruction) {
	        instructions.add(instruction);
	    }

	 private String newLabel() {
	        return "L" + (labelCounter++);
	    }

	public List<String> getInstructins(){
		return instructions;
	}
	


}
