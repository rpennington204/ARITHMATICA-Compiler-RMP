import java.util.*;
public class TestScan {
	public static void main (String[] args) throws Exception{
		String myProg = "INT W; \n INT X; \n INT Y; \n Y = 4; \n DOUBLE Z = 5.5; \n W = 10; \n X = -5; \n INT A = W + X; \n Z = -10.5; \n IF X == Y THEN X = 10; \n INPUT Z; \n PRINT Z; \n DOUBLE F; \n F = 10.5; \n DOUBLE L = F + Z;";
		
		ArrayList<Token> lexTokens = LexerPartial.lex(myProg);
		
		System.out.println(lexTokens);
		
		Parser myParser = new Parser(lexTokens);
		BytecodeGenerator generator = new BytecodeGenerator(myParser.symTable);
		generator.generateCode(myParser.parse());
		for (String instruction : generator.getInstructins()) {System.out.println(instruction);}
	}
}
