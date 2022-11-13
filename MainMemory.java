// --------------------------- OS_Project_Phase1--------------
// ----------------------Instructor : Miss Asma Larik-------------
// Group main_memoryber Names:
// Syeda Maham Jafri
// Alliya Parvez
// Sara Ebrahim

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class MainMemory {
    public static void main(String[] args) {
        Register reg = new Register();

        // STEP 1: Initializing the general purpose and special purpose registers

        // Creating a byte array for the main main_memoryory
        byte[] main_memory = new byte[65536];

        // Now first we need to create an array to store our general purpose registers
        Register[] gpr_array = new Register[16];
        //Now we need to initialize all the GPR Registers at each indexof the array to have a total of 16 GPR's
        for (int i = 0; i < gpr_array.length; i++) {
            gpr_array[i] = new Register();
        }

        // Now first we need to create an array to store our special purpose registers
        Register[] spr_array = new Register[16];
        //Now we need to initialize all the GPR Registers at each index of the array to have a total of 16 SPR's
        for (int i = 0; i < spr_array.length; i++) {
            spr_array[i] = new Register();
        }

        // now we first initialize our code counter, the code counter basically keeps track of the total
        //number of instructions that will be read from the file
        Register Code_Counter = new Register();
        // now we link the Code counter with spr array index position
        spr_array[2].value = Code_Counter.value;

        // IR holds the current instructions to be executed,PC holds the address of the current instruction being execute
        // and code base points to the first instruction that was read in the main_memoryory
        // Now we need to associate the PC,IR and the CB registers with the array od special purpose registers that we created

        // Initializing and Linking the PC with its position in the array
        Register PC = new Register();
        spr_array[9].value = PC.value;

        // Initializing and Linking the IR with its position in the array
        Register IR = new Register();
        spr_array[10].value = IR.value;

        // Initializing and Linking the CB with its position in the array
        Register Code_Base = new Register();
        spr_array[0].value = Code_Base.value;

        //Initializing an array for storing the converted into to hex value from the data file -->hex code value
        String[] hex_codes = new String[16];

        //-----------------------> STEP2: File Reading and Fetching the data <----------

        // initialize a byte array that will store the converted int to byte value from the data file given to you
        byte[] byte_val = new byte[16];
        // a counter variable to keep track of how many instructions have been read, also to specify index in main_memoryory
        int i = 0;

        List<String> store = new ArrayList<>();
        // Now we do the file reading part where we read the integer value given in the file, increment our
        // code counter to keep the track of instructions and store them in the main_memoryory in the byte form
        try {
            String path = "C:\\Users\\User\\Downloads\\p0 (1).txt";
            // passing the path of our data file
            File data_file = new File(path);
            Scanner read_file = new Scanner(data_file).useDelimiter(" ");

            while (read_file.hasNext()) {
                // reads all the hexs which we are reading as string present in the data file one by one
               // and then storing them in an array list
                String s = read_file.next();
                System.out.println(s);
                store.add(s);
                //Remember that originally the registers will also store value in bytes*/

                //Now store the hex code form of instrustion as well, so we convert our int data to hex value
                //;
                i++;
                Code_Counter.value = (byte) i;
            }

            byte[] register = new byte[store.size()];
            // for loop for converting the values from string to integer
            for(int j = 0 ; j < store.size(); j++){
                //Integer.parseInt(store.get(j),16); for dealing with the hex values such as F3
                int instruction = Integer.parseInt(store.get(j),16);
                register[j] = (byte)instruction;
                // storing in the memory in byte form
                main_memory[j] = register[j];
                // Storing hex codes, eventhough not needed specifically
                hex_codes[i] = Integer.toHexString(instruction);
            }
            // for checking:
            /*for(int k = 0 ; k < store.size() ; k++){

                System.out.println(store.get(k));

            }*/
            System.out.println();

            // after all the data in file has been read we close our scanner
           read_file.close();
           // Printing out the value of our code counter
            System.out.println("The value of Code Counter is " + Code_Counter.value);
        } catch (FileNotFoundException e) {
            // throwing out a print statement incase the error is caught
            System.out.print("Error: File could not be found at the mentioned location");
        }

        // ------------------> STEP3: Decoding the opcode <--------------
     PC.value = 0;
        //for loop to continue with decoding and executing the instructions till the program terminates
        for (int j = 0; j < Code_Counter.value; j++) {
            int opcode;
            // PC holds the address for current instruction that needs to be executed, so we need to fetch
            //our data from the main_memory at the address stored in PC and give that instruction to IR since it hold the
            //current instruction that needs to be executed
            spr_array[10].value = main_memory[PC.value];
            //converting the value from byte to int
            int int_IRval = main_memory[PC.value] & 0xff;
            // printing the value of instruction register (for your ownself)
            System.out.println("The value of IR is:" + int_IRval);
            String hex_opcode = Integer.toHexString(int_IRval);
            // printing the hex code of the instruction (for you own self)
            System.out.println("The value of hex is:" + hex_opcode);
            //try-catch block
            try {
                opcode = Integer.parseUnsignedInt(hex_opcode);
                //System.out.println("Opcode= " + opcode);
            } catch (NumberFormatException e) {
                opcode = int_IRval;

            }

            //------------------> STEP3: Executing the instructions based on opcodes <--------------
            // use the switch case methods for each of the instruction type
            // TYPE 1: Register - Register Instructions
            if ((opcode >= 16 && opcode <= 19) || (opcode >= 26 && opcode <= 28)) {
                // based on the opcode matching we will perform the operation
                InstructionSet Apply = new InstructionSet();
                switch (opcode) {
                    case 16:
                        //Performing the operation of MOV
                        Apply.move(gpr_array[main_memory[j + 1]], gpr_array[main_memory[j + 2]]);
                        System.out.println("The value after the mov operation is: " + gpr_array[main_memory[j + 1]].value);
                        //incrementing the value of PC
                        Apply.increment(PC, (short) (3));
                        System.out.println();
                        break;
                    case 17:
                        //Performing the operation of ADD
                        Apply.add(gpr_array[main_memory[j + 1]], gpr_array[main_memory[j + 2]]);
                        System.out.println("The value after the add instruction is: " + gpr_array[main_memory[j + 1]].value);
                        //incrementing the value of PC
                        Apply.increment(PC, (short) (3));
                        System.out.println();
                        break;

                    case 18:
                        //Performing the operation of SUB
                        Apply.subtract(gpr_array[main_memory[j + 1]], gpr_array[main_memory[j + 2]]);
                        System.out.println("The value after the subtract instruction is: " + gpr_array[main_memory[j + 1]].value);
                        //incrementing the value of PC
                        Apply.increment(PC, (short) (3));
                        System.out.println();
                        break;
                    case 19:
                        //Performing the operation of MUL
                      //  System.out.println("The value of j is: " + j);
                       System.out.println("The value of PC is: " + PC.value);
                       System.out.println(main_memory[PC.value]);
                        int index = (main_memory[PC.value + 1] & 0xFF);  //converting byte to int
                       // System.out.println("The index is: " + index);
                        Apply.multiply(gpr_array[index], gpr_array[index + 1]);
                        System.out.println("The value after multiply instruction is: " + gpr_array[index].value);
                        System.out.println();
                        //incrementing the value of PC
                        Apply.increment(PC, (short) (3));
                        break;
                    case 26:
                        //Performing the operation of DIV
                        Apply.divide(gpr_array[main_memory[j + 1]], gpr_array[main_memory[j + 2]]);
                        System.out.println("The value after the divide instruction is: " + gpr_array[main_memory[j + 1]].value);
                        System.out.println();
                        //incrementing the value of PC
                        Apply.increment(PC, (short) (3));
                        break;
                    case 27:
                        //Performing the operation of AND
                        Apply.and(gpr_array[main_memory[j + 1]], gpr_array[main_memory[j + 2]]);
                        System.out.println("The value after the AND instruction is: " + gpr_array[main_memory[j + 1]].value);
                        System.out.println();
                        //incrementing the value of PC
                        Apply.increment(PC, (short) (3));
                        break;
                    case 28:
                        //Performing the operation of OR
                        Apply.or(gpr_array[main_memory[j + 1]], gpr_array[main_memory[j + 2]]);
                        System.out.println("The value after the OR instruction is: " + gpr_array[main_memory[j + 1]].value);
                        System.out.println();
                        //incrementing the value of PC
                        Apply.increment(PC, (short) (3));
                        break;
                }
            }
            //TYPE 2:  Register - Immediate Instructions
            else if ((opcode >= 30 && opcode <= 39) || (opcode >= 58 && opcode <= 61)) {
                System.out.println("Opcode= " + opcode);
                InstructionSet RI = new InstructionSet();
                switch (opcode) {
                    case 30:
                        //Performing the operation of MOVI
                       // System.out.println("The value of j is: " + j);    for checking
                       // System.out.println("The value of PC is: " + PC.value);   for checking
                        //System.out.println(main_memory[PC.value]);        for checking
                        int Register = (main_memory[PC.value+1] & 0xFF);  //converting byte to int
                      //  System.out.println("The Register is: " + Register);
                        short immediate = (short) (Byte.toUnsignedInt(main_memory[PC.value + 2]) + Byte.toUnsignedInt(main_memory[PC.value + 3]));
                     //   System.out.println("The immediate is: " + immediate);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        RI.movi(gpr_array[Register], immediate);
                        System.out.println("The value of register after movi instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        RI.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 31:
                        // Performing the operation of ADDI
                        //System.out.println("The value of j is: " + j);
                        //System.out.println("The value of PC is: " + PC.values);
                        //System.out.println(main_memory[PC.value]);
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        //  System.out.println("The Register is: " + Register);
                        immediate = (short) (main_memory[PC.value + 2] + main_memory[PC.value + 3]);
                        //   System.out.println("The immediate is: " + immediate);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        RI.addi(gpr_array[Register], immediate);
                        System.out.println("The value of register after addi instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        RI.increment(PC, (short) (4));
                        System.out.println();

                        break;
                    case 32:
                        //Performing the operation of SUB1
                        //System.out.println(main_memory[PC.value]);
                        Register = (main_memory[PC.value] & 0xFF);//converting byte to int
                        //  System.out.println("The Register is: " + Register);
                        immediate = (short) (main_memory[PC.value + 2] + main_memory[PC.value + 3]);
                        //   System.out.println("The immediate is: " + immediate);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        RI.subi(gpr_array[Register], immediate);
                        System.out.println("The value of register after subi instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        RI.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 33:
                        //Performing the operation of MULI
                        //System.out.println(main_memory[PC.value]);
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        //  System.out.println("The Register is: " + Register);
                        immediate = (short) (main_memory[PC.value + 2] + main_memory[PC.value + 3]);
                        //   System.out.println("The immediate is: " + immediate);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        RI.muli(gpr_array[Register], immediate);
                        System.out.println("The value of register after muli instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        RI.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 34:
                        //Performing the operation of DIVI
                        //System.out.println(main_memory[PC.value]);
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        //  System.out.println("The Register is: " + Register);
                        immediate = (short) (main_memory[PC.value + 2] + main_memory[PC.value + 3]);
                        //   System.out.println("The immediate is: " + immediate);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        RI.divi(gpr_array[Register], immediate);
                        System.out.println("The value of register after divi instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        RI.increment(PC, (short) (4));
                        break;
                    case 35:
                        //Performing the operation of ANDI
                        //System.out.println(main_memory[PC.value]);
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        //  System.out.println("The Register is: " + Register);
                        immediate = (short) (main_memory[PC.value + 2] + main_memory[PC.value + 3]);
                        //   System.out.println("The immediate is: " + immediate);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        RI.andi(gpr_array[Register], immediate);
                        System.out.println("The value of register after ANDI instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        RI.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 36:
                        //Performing the operation of ORI
                        //System.out.println(main_memory[PC.value]);
                        Register = (main_memory[PC.value + 1] & 0xFF);  //converting byte to int
                        //  System.out.println("The Register is: " + Register);
                        immediate = (short) (main_memory[PC.value + 2] + main_memory[PC.value + 3]);
                        //   System.out.println("The immediate is: " + immediate);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        RI.ori(gpr_array[Register], immediate);
                        System.out.println("The value of register after ORI instruction is: " + gpr_array[Register].value);
                        RI.increment(PC, (short) (4));
                        System.out.println();
                        break;
                   
                }
            }
            //TYPE 3:  main_memory Instructions


            //TYPE 4:  Single Operand Instructions
            else if (opcode >= 71 && opcode <= 78) {
                InstructionSet SO = new InstructionSet();
                switch (opcode) {
                    case 71:
                        // performing Shift Left instruction
                        int Register;
                        short immediate;
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        System.out.println("The Register is: " + Register);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        SO.shiftl(gpr_array[Register]);
                        System.out.println("The value of register after SHL instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        SO.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 72:
                        //performing the Shift Right Instruction
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        System.out.println("The Register is: " + Register);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        SO.shiftr(gpr_array[Register]);
                        System.out.println("The value of register after SHR instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        SO.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 73:
                        // performing Rotate Left instruction
                        Register = (main_memory[PC.value + 1] & 0xFF); //converting byte to int
                        System.out.println("The Register is: " + Register);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                       SO.rotateL(gpr_array[Register]);
                        System.out.println("The value of register after RTL instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        SO.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 74:
                        // performing Rotate Right instruction
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        System.out.println("The Register is: " + Register);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                       SO.rotateR(gpr_array[Register]);
                        System.out.println("The value of register after RTR instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        SO.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 75:
                        // performing the increment instruction
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        System.out.println("The Register is: " + Register);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        SO.increment(gpr_array[Register],(short)0);
                        System.out.println("The value of register after INC instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        SO.increment(PC, (short) (4));
                        System.out.println();
                        break;
                    case 76:
                        //performing the decrement instructions
                        Register = (main_memory[PC.value + 1] & 0xFF);//converting byte to int
                        System.out.println("The Register is: " + Register);
                        System.out.println("The value of register before instruction is: " + gpr_array[Register].value);
                        SO.decrement(gpr_array[Register],(short)1);
                        System.out.println("The value of register after DEC instruction is: " + gpr_array[Register].value);
                        //incrementing the value of PC
                        SO.increment(PC, (short) (4));
                        System.out.println();
                        break;
                }
            }
            //TYPE5: No Operand Instrcutions;
            else if (opcode >= 241 && opcode <= 243) {
                InstructionSet NO = new InstructionSet();
                switch (opcode) {
                    case 241:
                        NO.increment(PC, (short)1);
                        break;
                    case 242:
                        //No operation
                        NO.increment(PC, (short)1);
                        break;
                    case 243:
                        PC.value ++;
                        System.out.println("The value of PC is: " + PC.value);
                        System.out.println("The program has ended.");
                        System.out.println();

                        break;
                }
                break;
            }
            System.out.println();
        }
        System.out.println();

        System.out.println("The value of all GPRs after execution of instructions is:");
        for(int a =0; a< gpr_array.length; a++)
        {
            System.out.println("The value of register " + a + " is: "+ Integer.toHexString(gpr_array[a].value));
        }
        System.out.println("");

        System.out.println("The value of all SPRs after execution of instructions is:");
        for(int a =0; a<spr_array.length; a++)
        {
            System.out.println("The value of register " + a + " is: "+ Integer.toHexString(spr_array[a].value & 0xff));
        }
    }
}