package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
	
    private int currentLineNumber = 0;
    private String currentCommand;
    private ArrayList<String> instructions = new ArrayList<String>();

    public Parser(File input) throws FileNotFoundException, Exception {
        try {                                                                   // add all non-whitespace lines
            removeWhitespace(input);                                            // to an array list of instructions
            if(!instructions.isEmpty()) {
               currentCommand = instructions.get(currentLineNumber);
            } else {
                throw new Exception("No valid instructions.");
            }
        } catch (FileNotFoundException fnf) {
            throw new FileNotFoundException(fnf.getMessage());
        }
    }

    public boolean hasMoreCommands() {
        return (currentLineNumber < instructions.size() - 1);
    }

    public void advance() {
        currentLineNumber++;
        currentCommand = instructions.get(currentLineNumber);
    }

    // resets to first instruction -- necessary because SymbolTable and Assembler use same Parser object
    public void reset() {
        currentLineNumber = 0;
        currentCommand = instructions.get(currentLineNumber);
    }

    public String commandType() {
    	
    	int space = currentCommand.indexOf(" ");
    	String command = currentCommand.substring(0, 3);
    	
    	if(command.equals("AND") || command.equals("EOR") || command.equals("SUB") || command.equals("RSB") ||
    			command.equals("ADD") || command.equals("ADC") || command.equals("SBC") || command.equals("RSC") ||
    			command.equals("TST") || command.equals("TEQ") || command.equals("CMP") || command.equals("CMN") ||
    			command.equals("ORR") || command.equals("MOV") || command.equals("BIC") || command.equals("MVN")){
    	
    		return "DATA_PROCESS_COMMAND";
    	
    	} else if (command.equals("B") || command.equals("BL") || command.equals("BLX") 
    			|| command.equals("BX") || command.equals("BXJ")){
    		
    		return "BRANCH_COMMAND";
    		
    	} else if (command.equals("MLA") || command.equals("MUL") || command.equals("SMLA") ||
    			command.equals("SMLAD") || command.equals("SMLAL") || command.equals("SMLAL")){
    		
    		
    		return "MULTIPLY_COMMAND";
    		
    	} else if (command.equals("LDR") || command.equals("LDRB") || command.equals("LDRBT") ||
    			command.equals("LDRD") || command.equals("LDREX") || command.equals("LDRH") ||
    			command.equals("LDRSB") || command.equals("LDRSH") || command.equals("LDRT") ||
    			command.equals("STR") || command.equals("STRB") || command.equals("STRBT") ||
    			command.equals("STRD") || command.equals("STREX") || command.equals("STRH") ||
    			command.equals("STRT")){
    		
    		return "LOAD_STORE_COMMAND";
    	
    	}
    		
            return "INVALID INSTRUCTION";
        
    }

    // parse symbol
    public String condition() {
        if(currentCommand.charAt(0) == '@') {
            return currentCommand.substring(1, currentCommand.length());
        } else if(currentCommand.charAt(0) == '(') {
            return currentCommand.substring(1, currentCommand.length()-1);
        } else return "INVALID INSTRUCTION";
    }
    
    //parse opcode
    public String opcode() {
        if(currentCommand.charAt(0) == '@') {
            return currentCommand.substring(1, currentCommand.length());
        } else if(currentCommand.charAt(0) == '(') {
            return currentCommand.substring(1, currentCommand.length()-1);
        } else return "INVALID INSTRUCTION";
    }
    
  //parse S
    public String sbit() {
        if(currentCommand.charAt(0) == '@') {
            return currentCommand.substring(1, currentCommand.length());
        } else if(currentCommand.charAt(0) == '(') {
            return currentCommand.substring(1, currentCommand.length()-1);
        } else return "INVALID INSTRUCTION";
    }
    
    // parse destination
    public String dest() {
        int eq = currentCommand.indexOf('=');
        if(eq > 0) {
            return currentCommand.substring(0, eq);
        } else return "null";
    }

    // parse computation
    public String comp () {
        int eq = currentCommand.indexOf('=');
        int semi = currentCommand.indexOf(';');
        if(eq > 0) {
            return currentCommand.substring(eq + 1);
        } else if(semi > 0) {
            return currentCommand.substring(0, semi);
        } else return "INVALID INSTRUCTION";
    }

    // parse jump
    public String jump() {
        int semi = currentCommand.indexOf(';');
        if(semi > 0) {
            return currentCommand.substring(semi + 1);
        } else return "null";
    }
    
 // parse symbol
    public String symbol() {
        if(currentCommand.charAt(0) == '@') {
            return currentCommand.substring(1, currentCommand.length());
        } else if(currentCommand.contains(":")) {							//check for labels
            return currentCommand.substring(1, currentCommand.length()-1);
        } else return "INVALID INSTRUCTION";
    }

    public void removeWhitespace(File input) throws FileNotFoundException {
        try {
            Scanner in = new Scanner(input);

            while(in.hasNext()) {
                String next = in.nextLine();                                // reads each line and splits into
                String[] line = next.split("\\s");                          // an array of string tokens
                String command = "";

                for(int i = 0; i < line.length; i++) {
                    if(line.length == 0) {                                  // ignores empty lines
                        break;
                    } else if(line[i].length() > 1 && line[i].substring(0, 2).equals("//") && i != 0) {
                        break;                                              // ignores inline comments
                    } else if(line[i].length() > 1 && line[i].substring(0, 2).equals("//") && i == 0) {
                        break;                                              // ignores whole-line comments
                    }  else {
                        command += line[i];
                    }
                }
                if(!command.equals("")) {
                    instructions.add(command);
                }
            }

            in.close();

        } catch(FileNotFoundException fnf) {
            throw new FileNotFoundException();
        }
    }
}
