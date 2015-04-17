package assembler;

import java.math.BigInteger;
import java.util.Hashtable;

public class SymbolTable {
    private Hashtable<String, Integer> symbols = new Hashtable<String, Integer>();
    private Parser p;
    private int nextAvailMem;
    private int currentLineNumber;

    public SymbolTable(Parser p) {          // takes Parser object from Assembler
        this.initialize();                  // adds all pre-defined symbols to table
        this.p = p;
        nextAvailMem = 32;                  // keeps track of next available memory location
        currentLineNumber = 0;              // keeps track of current line number for L_COMMANDs
        this.firstPass();                   // parses all instructions for L_COMMANDs
    }

    private void initialize() {
    	
    	symbols.put("r0", 0);
    	symbols.put("r1", 1);
    	symbols.put("r2", 10);
    	symbols.put("r3", 10);
    	symbols.put("r4", 100);
    	symbols.put("r5", 101);
    	symbols.put("r6", 110);
    	symbols.put("r7", 111);
    	symbols.put("r8", 1000);
    	symbols.put("r9", 1001);
    	symbols.put("r10", 1010);
    	symbols.put("r11", 1011);
    	symbols.put("r12", 1100);
    	symbols.put("r13", 1101);
    	symbols.put("r14", 1110);
    	symbols.put("r15", 1111);
    }

    private void firstPass() {
        while(true) {
            if(p.commandType().equals("L_COMMAND")) {
                symbols.put(p.symbol(), currentLineNumber);
            } else currentLineNumber++;

            if(p.hasMoreCommands()) {
                p.advance();
            } else {
                break;
            }
        }
        p.reset();                          // resets Parser for Assembler
    }

    public void addEntry(String symbol) {
        symbols.put(symbol, nextAvailMem);
        nextAvailMem+=nextAvailMem;
    }

    public boolean contains(String symbol) {
        return symbols.containsKey(symbol);
    }

    public int getAddress(String symbol) {
    	
    	if(symbol.contains("#")){
    		return Integer.parseInt(Integer.toBinaryString(Integer.parseInt(symbol.substring(symbol.indexOf("#")+1, symbol.length()))));
    	}
        return symbols.get(symbol);
    }
}