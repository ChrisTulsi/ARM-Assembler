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
	private JTextArea assemblyCode, binaryCode;
	private JButton translate;
	private JFileChooser fileChooser;
	private File input, output;
	
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
		
		assemblyCode = new JTextArea(30,38);
		binaryCode = new JTextArea(30,38);
		assemblyCode.setEditable(false);
		binaryCode.setEditable(false);
		
		translate = new JButton("Translate");
		translate.addActionListener(this);
		
		add(new JScrollPane(assemblyCode));
		add(translate);
		add(new JScrollPane(binaryCode));
		
		fileChooser = new JFileChooser();
		
		setTitle("ARM Assembler");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1000,550);
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
					} else if (p.commandType().equals("DATA_PROCESS_COMMAND")) { // handle A_COMMANDs
						
						BigInteger machine = BigInteger.ZERO;
						BigInteger sum = BigInteger.ZERO;
						int opcode = Code.opcode(p.opcode()); // get codes from Code module
						int condition = Code.condition(p.condition());
						int i = 0;
						int s = p.sbit();
						//int rn = st.getAddress(p.symbol());
						//int rd = st.getAddress(p.symbol());
						int operand2 = st.getAddress(p.symbol());
						
						
						if (opcode != 2 ) { // if no invalid codes
								
							sum = machine.add(leftShift(condition,28));
							machine = sum;
							sum = machine.add(leftShift(opcode,21));
							machine = sum;
							sum = machine.add(leftShift(s,20));
		
							writeOut.println(sum);
							currentInstruction++;
							
						} else { // handles invalid codes
								System.out.println("Error at instruction "
										+ currentInstruction + " of .asm file.");
								System.out
										.println("Resulting .hack file is incomplete.");
								writeOut.close();
								return;
						}
					
					}	else { // handles invalid instructions
						System.out.println("Error at instruction "
								+ currentInstruction + " of .asm file.");
						System.out
								.println("Resulting .out file is incomplete.");
						writeOut.close();
						return;
					}

					if (p.hasMoreCommands()) { // advance if more commands
						p.advance();
					} else { // break if last command
						break;
					}
				}
					
				writeOut.close();
			
				try {
					FileReader reader = new FileReader(output.getAbsolutePath());
					BufferedReader br2 = new BufferedReader(reader);
					binaryCode.read(br2, null);
					br2.close();
					
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
		
		for (int i = 0; i < num; i++) {
			
			sum = big.multiply(BigInteger.TEN);
			big = sum;
		}
		
		return sum;
	}
	
	public int rightShift(int instruction, int num){
		
		for (int i = 0; i < num; i++) {
			
			instruction /= 10;
		}
		
		return instruction;
	}
	
	public int dataInstruction(int condition,int i , int opcode, int s, int rn , int rd, int operand2 ){
		return condition + i + opcode + s + rn + rd + operand2;
	}
}
