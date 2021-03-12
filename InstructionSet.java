public class InstructionSet {

    String operationname;
    String opcode;

    public InstructionSet(String operationname, String opcode) {
        this.operationname = operationname;
        this.opcode = opcode;
    }

    public String getOperationname() {
        return operationname;
    }

    public void setOperationname(String operationname) {
        this.operationname = operationname;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }
}
