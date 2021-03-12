
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MAIN {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        SicAssembler myassembler =new SicAssembler();
        myassembler.ScanInstructionSet();
        myassembler.ReadAssemblyCode();
        myassembler.PassOne();
        myassembler.PassTwo();
        myassembler.HTE_Record();


    }


}
