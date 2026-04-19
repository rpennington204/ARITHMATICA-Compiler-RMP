import java.util.*;
public class TestScan {
	public static void main (String[] args) throws Exception{
		String myProg = "INT W; \n INT X; \n INT Y; \n DOUBLE Z; \n W = 10; \n X + -5; \n A = W + X; \n Z = -10.5 \n IF X != Y;";
		
		ArrayList<Token> lexTokens = LexerPartial.lex(myProg);
		
		System.out.println(lexTokens);
	}
}
