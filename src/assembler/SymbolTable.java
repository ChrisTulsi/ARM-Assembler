package assembler;

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
        for(int i = 0; i <= 15; i++) {
            String key = "R" + i;
            symbols.put(key, i);
        }
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
        nextAvailMem++;
    }

    public boolean contains(String symbol) {
        return symbols.containsKey(symbol);
    }

    public int getAddress(String symbol) {
        return symbols.get(symbol);
    }
}