

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;


public class SicAssembler {


    Hashtable<String, String> symtab = new Hashtable<>();
    ArrayList<InstructionSet> instructionset;
    ArrayList<AssemblyCode> code;

    public void ScanInstructionSet() throws FileNotFoundException {


        File f = new File("Instruction set.txt");
        Scanner scan = new Scanner(f);
        instructionset = new ArrayList<>();
        while (scan.hasNext()) {
            String insName = scan.next();
            String insOpcode = scan.next();
            instructionset.add(new InstructionSet(insName, insOpcode));
        }
    }

    public void ReadAssemblyCode() throws FileNotFoundException {
        code = new ArrayList<>();
        File f = new File("in.txt");
        Scanner scan = new Scanner(f);
        while (scan.hasNextLine()) {
            String l = scan.nextLine();
            String[] inst = l.split("\\s+");
            if (inst.length == 3) {
                String label = inst[0];
                String instruction = inst[1];
                String value = inst[2];
                code.add(new AssemblyCode(label, instruction, value, "", ""));
            } else {
                String label = "-----";
                String instruction = inst[0];
                String value = inst[1];
                code.add(new AssemblyCode(label, instruction, value, "", ""));
            }
        }

    }

    public void PassOne() {
        String startingAddress = code.get(0).getValue();
        code.get(0).setAdress(startingAddress);
        int calculator = Integer.parseInt(startingAddress, 16);
        int counter = 0;
        symtab.put(code.get(0).getLabel(), startingAddress);
        for (int i = 1; i < code.size(); i++)
        { calculator += counter;
            String addressInHex = Integer.toHexString(calculator);
            addressInHex = Refill(addressInHex, 4);
            code.get(i).setAdress(addressInHex);


            String value = code.get(i).getValue();

            switch (code.get(i).getInstruction()) {
                case "RESB" -> {
                    counter = Integer.parseInt(value);
                    break;
                }
                case "RESW" -> {
                    int v = Integer.parseInt(value);
                    counter = v * 3;
                    break;
                }
                case "BYTE" -> {


                    counter = 1;
                    break;
                }
                default -> counter = 3;
            }


            symtab.put(code.get(i).getLabel(), addressInHex);

        }
        System.out.println(symtab);
    }



    public void PassTwo() {

        for (int i = 1; i < code.size(); i++) {

            String instruction = code.get(i).getInstruction();
            String value = code.get(i).getValue();
            String opcode = OpcodeGenerator(instruction);
            String address = symtab.get(value);


            if (instruction.equals("RESB") || instruction.equals("RESW")) {
                code.get(i).setOpcode("-----");

            } else if (instruction.equals("WORD")) {
                String objectcode = Integer.toHexString(Integer.parseInt(value));
                while (objectcode.length() < 6) {
                    objectcode = Refill(objectcode,6);
                }
                code.get(i).setOpcode(objectcode);

            }


            else if (instruction.equals("BYTE")) {
                if (value.startsWith("X")) {
                    code.get(i).setOpcode(value);

                } else if (value.startsWith("C")) {
                    String opcode2 = "";
                    //J STARTS WITH 2 SINCE WE IGNORE ' & C
                    for (int j= 2; j < value.length() - 1; j++) {
                        {
                            opcode2 = opcode2 + Integer.toHexString(value.charAt(j));
                        }
                        code.get(i).setOpcode(opcode2);

                    } }



            }
            else if (value.endsWith(",X"))
            { value = value.replace(",X", "");
                address = symtab.get(value);
                int addressToHex = Integer.parseInt(address, 16);
                String addressToBinary = Integer.toBinaryString(addressToHex);
                while (addressToBinary.length() < 15) {
                    addressToBinary = Refill(addressToBinary,15);
                }
                addressToBinary = "1" + addressToBinary;
                addressToHex = Integer.parseInt(addressToBinary, 2);
                address = Integer.toHexString(addressToHex);

                code.get(i).setOpcode(opcode + address); }




            else {code.get(i).setOpcode(opcode + address);}}

        for (AssemblyCode x : code) {
            System.out.println(x.getAdress() + "\t" + x.getLabel() + "\t" + x.getInstruction() + "\t" + x.getValue() + "\t" + x.getOpcode());
        }

    }








    public String Refill(String s, int length) {
        StringBuilder sBuilder = new StringBuilder(s);
        while (sBuilder.length() < length) {
            sBuilder.insert(0, "0");
        }
        s = sBuilder.toString();
        return s;
    }


    public void HTE_Record() throws IOException {

        int i = 0;
        int j = 0;
        File f = new File("out.txt");
        FileWriter w = new FileWriter(f);
        String lab = code.get(0).getLabel();
        while (lab.length() < 6) {
            lab = lab+ " ";
        }
        String start = code.get(0).getValue();
        String end = code.get(code.size() - 1).getAdress();
        int start2 = Integer.parseInt(start, 16);
        int end2 = Integer.parseInt(end, 16);
        int length = end2 - start2;
        String programLength = Integer.toHexString(length);
        while (programLength.length() < 6) {
            programLength = Refill(programLength, 6);
        }
        while (start.length() < 6) {
            start = Refill(start, 6);
        }

        System.out.println("H" + "." + lab + "." + start + "." + programLength);
        w.write("H" + "." + lab + "." + start + "." + programLength);
        w.write(System.lineSeparator());
        System.out.println("E" + "." + start);
        w.write("E" + "." + start);
        w.close();

        for (j = 1; j <= 2; j++) {
            System.out.print("T");


            for (i = 1; i <=10; i++) {
                if (j == 1) {
                    System.out.print(code.get(i).getOpcode());
                    System.out.print(".");


                } else if (j > 1) {
                    if ((code.get(i + (10 * (j - 1))).getOpcode()).equals("-----") || (code.get(i + (10 * (j - 1))).getOpcode()).equals("End of code -- null")) {
                        System.out.print("");

                    } else {
                        System.out.print(code.get(i + (10 * (j - 1))).getOpcode());
                        System.out.print(".");


                    }

                }
            }
            System.out.println("");


        }


    }




    public String OpcodeGenerator(String Instruction) {
        for (InstructionSet x : instructionset) {
            if (x.getOperationname().equals(Instruction)) {
                return x.getOpcode();
            }

        }
        return "End of code -- ";
    }
}


