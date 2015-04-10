package assembler;

public class Code {
	
	public static String opcode(String d) {
        if(d.equals("AND")) {
            return "0000";
        } else if(d.equals("EOR")) {
            return "0001";
        } else if(d.equals("SUB")) {
            return "0010";
        } else if(d.equals("RSB")) {
            return "0011";
        } else if(d.equals("ADD")) {
            return "0100";
        } else if(d.equals("ADC")) {
            return "0101";
        } else if(d.equals("SBC")) {
            return "0110";
        } else if(d.equals("RSC")) {
            return "0111";
        } else if(d.equals("TST")) {
            return "1000";
        } else if(d.equals("TEQ")) {
            return "1001";
        } else if(d.equals("CMP")) {
            return "1010";
        } else if(d.equals("CMN")) {
            return "1011";
        } else if(d.equals("ORR")) {
            return "1100";
        } else if(d.equals("MOV")) {
            return "1101";
        } else if(d.equals("BIC")) {
            return "1110";
        } else if(d.equals("MVN")) {
            return "1111";
        } else return "NG";
    }
	
	
	public static String branch(String d){
		
		if(d.equals("B")){
			return "";
		} else if(d.equals("BL")) {
            return "";
		} else if(d.equals("BLX")) {
            return "";
		} else if(d.equals("BX")) {
            return "";
		} else if(d.equals("BXJ")) {
            return "";
		} else return "NG";
	}
	
	
	// converts destination to machine language instruction
    public static String dest(String d) {
        if(d.equals("M")) {
            return "001";
        } else if(d.equals("D")) {
            return "010";
        } else if(d.equals("MD") || d.equals("DM")) {
            return "011";
        } else if(d.equals("A")) {
            return "100";
        } else if(d.equals("AM") || d.equals("MA")) {
            return "101";
        } else if(d.equals("AD") || d.equals("DA")) {
            return "110";
        } else if(d.equals("AMD") || d.equals("ADM") || d.equals("DMA") || d.equals("DAM")
                || d.equals("MAD") || d.equals("MDA")) {
            return "111";
        } else if(d.equals("null")) {
            return "000";
        } else return "NG";
    }

}
