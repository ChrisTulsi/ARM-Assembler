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
    	
    	String command = currentCommand.substring(0,3);
    	
    	if(command.equals("AND") || command.equals("EOR") || command.equals("SUB") || command.equals("RSB") ||
    			command.equals("ADD") || command.equals("ADC") || command.equals("SBC") || command.equals("RSC") ||
    			command.equals("TST") || command.equals("TEQ") || command.equals("CMP") || command.equals("CMN") ||
    			command.equals("ORR") || command.equals("MOV") || command.equals("BIC") || command.equals("MVN")){
    	
    		return "DATA_PROCESS_COMMAND";
    	
    	}	else if (command.startsWith("B")){
    		return "BRANCH_INSTRUCTION";
    	}
            return "INVALID INSTRUCTION";
        
    }
    
    // parse symbol
    public String symbol() {
        if(currentCommand.endsWith(":")) {
            return currentCommand.substring(0, currentCommand.length()-1);
        } else return "INVALID INSTRUCTION";
    }
    
	public boolean twoOperand(){ 	// check if two or three operand
		String half = currentCommand.substring(currentCommand.indexOf(",") + 1, currentCommand.length());
		
		if(half.contains(",")){
			return false;
			}
			
		return true;
	}
	
	public String opcode(){		//handles up code for dataprocess instruction 
	 		return currentCommand.substring(0,3);
	}
    
	public String condition(){//checks for conditions
		
		   if(currentCommand.contains("EQ")) return "EQ";
		   else if(currentCommand.contains("NE")) return "NE";
		   else if(currentCommand.contains("HS")) return "HS";
		   else if(currentCommand.contains("CS")) return "CS";
		   else if(currentCommand.contains("LO")) return "LO";
		   else if(currentCommand.contains("CC")) return "CC";
		   else if(currentCommand.contains("MI")) return "MI";
		   else if(currentCommand.contains("PL")) return "PL";
		   else if(currentCommand.contains("VS")) return "VS";
		   else if(currentCommand.contains("VC")) return "VC";
		   else if(currentCommand.contains("HI")) return "HI";
		   else if(currentCommand.contains("LS")) return "LS";
		   else if(currentCommand.contains("GE")) return "GE";
		   else if(currentCommand.contains("LT")) return "LT";
		   else if(currentCommand.contains("GT")) return "GT";
		   else if(currentCommand.contains("LE")) return "LE";
		   else if(currentCommand.contains("NV")) return "NV";   
		   return "";
	 }
	
	public int ibit(){	//handles i bit
		if (currentCommand.contains("#")) return 1;
		return 0;
	}
	
	public int sbit(){	 //handles sbit
		String command = currentCommand.substring(currentCommand.indexOf("r")-1);
		if(command.equals("S")) return 1;
		 return 0;
	 }
	
	public String rn(){		//handles rn
		return currentCommand.substring(currentCommand.indexOf(",")-2 , currentCommand.indexOf(","));
	}
	
	public String rd(){		//handles rd 
		
		if(currentCommand.contains("#")){
			return currentCommand.substring(currentCommand.indexOf("#"),currentCommand.length());
		}else
		return currentCommand.substring(currentCommand.indexOf(",")+1 , (!twoOperand())?currentCommand.lastIndexOf(","):currentCommand.length());
	}
	
	public String operand2(){		//handles operand
		
		if(currentCommand.contains("#")){
			return currentCommand.substring(currentCommand.indexOf("#"),currentCommand.length());
		}else
	
		return currentCommand.substring(currentCommand.lastIndexOf(",")+1,currentCommand.length());
	}
	
	public int lBit(){	//handle LBit for BRANCH INSTRUCTION
		
		if(currentCommand.contains("L")) return 1;
		
		return 0;
	}
	
	public int offset(){ //handles offset for BRANCH INSTRUCTION
	
		return 0;
	}
	 
    public void removeWhitespace(File input) throws FileNotFoundException {			//Handles whitespaces
        try {
            Scanner in = new Scanner(input);

            while(in.hasNext()) {
                String next = in.nextLine();                                // reads each line and splits into
                String[] line = next.split("\\s");                          // an array of string tokens
                String command = "";

                for(int i = 0; i < line.length; i++) {
                    if(line.length == 0) {                                  // ignores empty lines
                        break;
                    } else if(line[i].length() > 1 && line[i].substring(0,2).equals("//") && i != 0) {
                        break;                                              // ignoes inline comments
                    } else if(line[i].length() > 1 && line[i].substring(0,2).equals("//") && i == 0) {
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
