import java.util.*;
public class LexerPartial {
	
	public static int testKeyID(String testString, int initpos) {
		int state = 0;
		int curpos = initpos;
		while (curpos < testString.length()) {
			char curChar = testString.charAt(curpos);
			if ((curChar == ' ') || (curChar == '\n') || (curChar == '\t') || (curChar == ';')) {
				break;
			}
			switch(state) {
			case 0:
				if (Character.isLetter(curChar)) {
					state = 1;
				}
				else return -1;
				break;
			case 1:
				if ((Character.isLetter(curChar)) || (Character.isDigit(curChar))){
					state = 2;
				}
				else return -1;
				break;
			case 2:
				if ((Character.isLetter(curChar)) || (Character.isDigit(curChar))){
					state = 2;
				}
				else return -1;
				break;
			}
			curpos++;
		}
		if (state == 1 || state == 2) {
			return curpos - 1;
		}
		return -1;
	}
	
	static boolean isDouble;
	
	public static int testKeyNum (String testString, int initpos) {
		isDouble = false;	//When the lexer sees a number, if it has a ".", this is set to true
		int state = 0;
		int curpos = initpos;
		while (curpos < testString.length()) {
			char curChar = testString.charAt(curpos);
			if ((curChar == ' ') || (curChar == '\n') || (curChar == '\t') || (curChar == ';') || (curChar == '.' && isDouble)) {
				break;
			}
			switch(state) {
			case 0:
				if (Character.isDigit(curChar) || curChar == '-') {
					state = 1;
				}
				else return -1;
				break;
			case 1:
				if (Character.isDigit(curChar)) {
					state = 1;
				}
				else if (curChar == '.') {
					isDouble = true;
					state = 2;
				}
				else return -1;
				break;
			case 2:
				if (Character.isDigit(curChar)) {
					state = 2;
				}
				else return -1;
				break;
			}
			curpos++;
		}
		if (state == 1 || state == 2) {
			return curpos - 1;
		}	
		return -1;
	}
	
	public static ArrayList<Token> lex (String progStr) throws Exception {
		ArrayList<Token> tokenList = new ArrayList<Token>();
		
		int curind = 0;
		int endind;
		progStr = progStr + "\n";
		
		while(curind<progStr.length() - 1) { //Main lexing loop
			System.out.println(curind + ": " + tokenList);
			char curChar = progStr.charAt(curind);
			char nextChar = progStr.charAt(curind + 1);
			if ((curChar == ' ') || (curChar == '\t') || (curChar == '\n')){
				curind++;
				//continue;
			}
			//char nextChar = progStr.charAt(curind + 1);
			else if ((curChar == '=') && (nextChar != '=')){
				tokenList.add(new Token("Assign", "="));
				curind++;
			}
			else if (curChar == ';') {
				tokenList.add(new Token("eos",";"));
				curind++;
			}
			else if ((curChar == '+') || (curChar == '/') || (curChar == '*')) {
				tokenList.add(new Token("aop",curChar+""));
				curind++;
			}
			else if (curChar == '-') {
				if ((nextChar == ' ') || (nextChar == '\t') || (nextChar == '\n')) {
					tokenList.add(new Token("aop",curChar+""));
					curind++;
				}
				else if (Character.isDigit(nextChar)) {
					endind = testKeyNum(progStr, curind);
					if (endind > 0) {
						if (isDouble) {
							tokenList.add(new Token("double", progStr.substring(curind, endind + 1)));
							curind = endind + 1;
						}
						else {
							tokenList.add(new Token("int", progStr.substring(curind, endind + 1)));
							curind = endind + 1;
						}
					}
					else throw new Exception ("Lexical Error: Unkown Token");
				}
				else throw new Exception("Lexical Error: Invalid \'-\'");
			}
			else if (Character.isLetter(curChar)) {
				endind = testKeyID(progStr, curind);
				if (endind > 0) {
					tokenList.add(new Token("idkey",progStr.substring(curind, endind + 1)));
					curind = endind + 1;
				}
				else throw new Exception ("Lexical Error: Unkown Token");
			}
			else if (Character.isDigit(curChar)) {
				endind = testKeyNum(progStr, curind);
				if (endind > 0) {
					if (isDouble) {
						tokenList.add(new Token("double", progStr.substring(curind, endind + 1)));
						curind = endind + 1;
					}
					else {
						tokenList.add(new Token("int", progStr.substring(curind, endind + 1)));
						curind = endind + 1;
					}
				}
				else throw new Exception ("Lexical Error: Unkown Token");
			}
			else if (curChar == '<' || curChar == '>' || (curChar == '!'  && nextChar == '=') || (curChar == '=' && nextChar == '=')) {
				if ((curChar == '<' || curChar == '>') && (nextChar == '=')) {
					tokenList.add(new Token("comp", curChar + "" + nextChar));
					curind = curind + 2;
				}
				else if (curChar == '<' || curChar == '>') {
					tokenList.add(new Token("comp", curChar + ""));
					curind++;
				}
				else {
					tokenList.add(new Token("comp", curChar + "" + nextChar));
					curind = curind + 2;
				}
			}
			else {
				throw new Exception ("Lexical Error: Unkown Token");
				//curind++;
			}
		}
		
		
		
		
		
		return tokenList;
	}
}
