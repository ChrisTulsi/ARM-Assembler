package assembler;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GUI extends JFrame implements ActionListener{
	
	private JMenuBar menuBar;
	private JMenu fileMenu, help;
	private JMenuItem open, exit, about;
	private JTextArea assemblyCode, binaryCode, hexCode;
	private JButton translate;
	private JFileChooser fileChooser;
	private File input, output, hexOut;
	
	public GUI(){
		
		Container contentPane = getContentPane();
	    contentPane.setLayout(new FlowLayout());
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		open = new JMenuItem("Open");
		open.addActionListener(this);
		fileMenu.add(open);
		
		fileMenu.addSeparator();
		
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		fileMenu.add(exit);	
		
		help = new JMenu("Help");
		menuBar.add(help);
		
		about = new JMenuItem("About");
		about.addActionListener(this);
		help.add(about);
		
		assemblyCode = new JTextArea(30,30);
		binaryCode = new JTextArea(30,30);
		hexCode = new JTextArea(30,30);
		assemblyCode.setEditable(false);
		binaryCode.setEditable(false);
		hexCode.setEditable(false);
		
		
		translate = new JButton("Translate");
		translate.addActionListener(this);
		
		add(new JScrollPane(assemblyCode));
		add(translate);
		add(new JScrollPane(binaryCode));
		add(new JScrollPane(hexCode));
		
		fileChooser = new JFileChooser();
		
		setTitle("ARM Assembler");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1200,550);
		setResizable(false);
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		//handles events for the menu items
				String source = evt.getActionCommand();
				
				if(source.equals("Open")) open();	
				if(source.equals("Exit")) System.exit(0);
				if(source.equals("About")) about();
				
			//handles events for the buttons in the toolbar
				if(evt.getSource() instanceof JButton) {		
				JButton clicked = (JButton)evt.getSource();
				
				if(clicked == translate) translate();
				
				}
				
	}

	private void about() {
		JOptionPane.showMessageDialog(this, "Group members: "
				+ "\nChris Tulsi  14/0719/1627"
				+ "\n?"
				+ "\n?"
				+ "\n?"
				+ "\n?");
		
	}

	private void open() {
		
		int ret = fileChooser.showOpenDialog(null);
		
		if (ret == JFileChooser.APPROVE_OPTION) {
			
			input = fileChooser.getSelectedFile();
            //This is where a real application would open the file.
			int extension = input.getName().indexOf('.');
			String fileName = input.getName().substring(0, extension);
		    output = new File(input.getParent(), fileName + ".out");
		    hexOut = new File(input.getParent(), fileName + ".hex"); 
            
		    try {
				FileReader reader = new FileReader(input.getAbsolutePath());
				BufferedReader br = new BufferedReader(reader);
				assemblyCode.read(br, null);
				br.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
        } else {
           System.out.println("rtyu");
        }
		
	}
	
	private void translate() {
		
			try {
																				
				PrintWriter writeOut = new PrintWriter(output);
				PrintWriter writeHex = new PrintWriter(hexOut);

				Parser p = new Parser(input); // create Parser

				SymbolTable st = new SymbolTable(p); // create SymbolTable

				int currentInstruction = 0; // set current line number to 0

				// while(true) loop includes last instruction, unlike
				// while(p.hasMoreCommands())
				while (true) {
					
					if (p.commandType().equals("L_COMMAND")) { // ignore L_COMMANDs
						
						if (p.hasMoreCommands()) {
							p.advance();
							continue;
						} else {
							break;
						}
					} else if (p.commandType().equals("DATA_PROCESS_COMMAND")) { // handle DATA_PROCESS_COMMANDs
						
						BigInteger machine = BigInteger.ZERO;
						BigInteger sum = BigInteger.ZERO;
						
						int condition = Code.condition(p.condition());
						int i = p.ibit();
						int opcode = Code.opcode(p.opcode()); // get codes from Code module
						int s = p.sbit();
						int rn = st.getAddress(p.rn());
						int rd = 0;
						int operand2 = 0;
						
						if(p.twoOperand()){
							rd = st.getAddress(p.rd());
						}else{
							rd = st.getAddress(p.rd());
							operand2 = st.getAddress(p.operand2());
						}
						
						if (opcode != 2 ) { // if no invalid codes
							
							machine = sum;
							sum = machine.add(leftShift(condition,28));
							machine = sum;
							sum = machine.add(leftShift(i,21));
							machine = sum;
							sum = machine.add(leftShift(opcode,20));
							machine = sum;
							sum = machine.add(leftShift(s,19));
							machine = sum;
							sum = machine.add(leftShift(rn,15));
							machine = sum;
							sum = machine.add(leftShift(rd,11));
							machine = sum;
							sum = machine.add(leftShift(operand2,0));
							machine = sum;
							
							String bin = appendString(sum);
							
							writeOut.println(bin);
							writeHex.println(binToHex(bin));
							currentInstruction++;
							
						} else { // handles invalid codes
								System.out.println("Error at instruction "
										+ currentInstruction + " of .asm file.");
								System.out
										.println("Resulting .hack file is incomplete.");
								writeOut.close();
								writeHex.close();
								return;
						}
					}else if (p.commandType().equals("BRANCH_INSTRUCTION")) {//handles Branch command
						
						BigInteger machine = BigInteger.ZERO;
						BigInteger sum = BigInteger.ZERO;
						
						int condition = Code.condition(p.condition());
						int constant = 101;
						int lBit = p.lBit();
						int offset = st.getAddress(p.offset());
						
						
						machine = sum;
						sum = machine.add(leftShift(condition,28));
						machine = sum;
						sum = machine.add(leftShift(constant,25));
						machine = sum;
						sum = machine.add(leftShift(lBit,24));
						machine = sum;
						sum = machine.add(rightShift(offset,2));
						machine = sum;
						
						String bin = appendString(sum);
						
						writeOut.println(bin);
						writeHex.println(binToHex(bin));
						
						currentInstruction++;
						
					}	else if(p.commandType().equals("LOAD_STORE_INSTRUCTION")) {

						BigInteger machine = BigInteger.ZERO;
						BigInteger sum = BigInteger.ZERO;
						
						int condition = Code.condition(p.condition());
						int constant = 1;
						int ibit = 0;
						int pbit = 0;
						int ubit = 0;
						int bbit = 0;
						int wbit = 0;
						int lbit = 0;
						int rn = 0;
						int rd = 0;
						int offset = 0;
						
						currentInstruction++;
						
					}	else { // handles invalid instructions
						System.out.println("Error at instruction "
								+ currentInstruction + " of .outfile.");
						System.out
								.println("Resulting .out file is incomplete.");
						writeOut.close();
						writeHex.close();
						return;
					}

					if (p.hasMoreCommands()) { // advance if more commands
						p.advance();
					} else { // break if last command
						break;
					}
				}
					
				writeOut.close();
				writeHex.close();
				try {
					FileReader reader = new FileReader(output.getAbsolutePath());
					BufferedReader br2 = new BufferedReader(reader);
					binaryCode.read(br2, null);
					br2.close();
					
					FileReader reader2 = new FileReader(hexOut.getAbsolutePath());
					BufferedReader br3 = new BufferedReader(reader2);
					hexCode.read(br3, null);
					br3.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
		
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
				
	}
	
	public BigInteger leftShift(int instruction, int num){
		
		BigInteger big = new BigInteger(Integer.toString(instruction));
		BigInteger sum = BigInteger.ZERO;
		int i = 0;
		
		for (; i < num; i++) {
			
			sum = big.multiply(BigInteger.TEN);
			big = sum;
		}
		
		return (i == 0)?big:sum;
	}
	
	public BigInteger rightShift(int instruction, int num){
		
		BigInteger big = new BigInteger(Integer.toString(instruction));
		BigInteger sum = BigInteger.ZERO;
		int i = 0;
		
		for (; i < num; i++) {
			
			sum = big.divide(BigInteger.TEN);
			big = sum;
		}
		
		return (i == 0)?big:sum;
	}
	public String binToHex(String bin){
		
		String one= Integer.toHexString(Integer.parseInt(bin.substring(0, 4),2));
		String two= Integer.toHexString(Integer.parseInt(bin.substring(4, 8),2));
		String three= Integer.toHexString(Integer.parseInt(bin.substring(8, 12),2));
		String four= Integer.toHexString(Integer.parseInt(bin.substring(12, 16),2));
		String five= Integer.toHexString(Integer.parseInt(bin.substring(16, 20),2));
		String six= Integer.toHexString(Integer.parseInt(bin.substring(20, 24),2));
		String seven= Integer.toHexString(Integer.parseInt(bin.substring(24, 28),2));
		String eight= Integer.toHexString(Integer.parseInt(bin.substring(28, 32),2));
		
		return one + two + three + four + five + six + seven + eight;
	}
	
	public String appendString(BigInteger machine){
		String binary = machine.toString();
		
		while(binary.length() != 32){
			binary = "0"+binary;
		}
		
		return binary;
	}
}
