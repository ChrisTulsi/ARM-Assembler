package assembler;

public class Code {
	
	public static int condition(String d){
		
		if(d.equals("EQ")){
			return 0000;
		} else if(d.equals("NE")){
			return 0001;
		} else if(d.equals("HS") || d.equals("CS")){
			return 0010;
		} else if(d.equals("LO") || d.equals("CC")){
			return 0011;
		} else if(d.equals("MI")){
			return 0100;
		} else if(d.equals("PL")){
			return 0101;
		} else if(d.equals("VS")){
			return 0110;
		} else if(d.equals("VC")){
			return 0111;
		} else if(d.equals("HI")){
			return 1000;
		} else if(d.equals("LS")){
			return 1001;
		} else if(d.equals("GE")){
			return 1010;
		} else if(d.equals("LT")){
			return 1011;
		} else if(d.equals("GT")){
			return 1100;
		} else if(d.equals("LE")){
			return 1101;
		} else if(d.equals("NV")){
			return 1111;
		} else return 1110;
	}
	
	public static int opcode(String d) {
        if(d.equals("AND")) {
            return 0000;
        } else if(d.equals("EOR")) {
            return 1;
        } else if(d.equals("SUB")) {
            return 10;
        } else if(d.equals("RSB")) {
            return 11;
        } else if(d.equals("ADD")) {
            return 100;
        } else if(d.equals("ADC")) {
            return 101;
        } else if(d.equals("SBC")) {
            return 110;
        } else if(d.equals("RSC")) {
            return 111;
        } else if(d.equals("TST")) {
            return 1000;
        } else if(d.equals("TEQ")) {
            return 1001;
        } else if(d.equals("CMP")) {
            return 1010;
        } else if(d.equals("CMN")) {
            return 1011;
        } else if(d.equals("ORR")) {
            return 1100;
        } else if(d.equals("MOV")) {
            return 1101;
        } else if(d.equals("BIC")) {
            return 1110;
        } else if(d.equals("MVN")) {
            return 1111;
        } else return 2;
    }
	
}
