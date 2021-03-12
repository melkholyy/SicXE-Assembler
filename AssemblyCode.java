public class AssemblyCode {
    String label;
    String instruction;
    String value ;
    String opcode ;
    String adress ;

    public AssemblyCode(String label, String instruction, String value, String opcode, String adress) {
        this.label = label;
        this.instruction = instruction;
        this.value = value;
        this.opcode = opcode;
        this.adress = adress;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}