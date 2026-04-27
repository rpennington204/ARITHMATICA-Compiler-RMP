/**
 * This is the Symbol Table class. This stores variables, their bytecode locations, and their type
 */
import java.util.HashMap;
public class SymbolTable { //Will be populated during parsing

	private HashMap<String, Symbol> sTable = new HashMap<>();
	private int curSlot = 1;
	
	public void declare (String name, String type) throws Exception {
		if (sTable.containsKey(name)) {
			throw new Exception("Variable " + name + " is already declared");
		}
		int slot = curSlot;
		sTable.put(name, new Symbol(type, slot));
		
		if (type.equals("DOUBLE")) {curSlot += 2;}
		else {curSlot += 1;}
		
	}
	
	public Symbol lookup(String name) throws Exception{
		if (!sTable.containsKey(name)) {
			throw new Exception("Variable " + name + " is used before declaration");
		}
		return sTable.get(name);
	}

	public int getCurSlot() {
		return curSlot;
	}

	public void setCurSlot(int curSlot) {
		this.curSlot = curSlot;
	}
	
	
}
