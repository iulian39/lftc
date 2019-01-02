import console.ReadSequence;
import model.Nonterminal;
import model.ProductionRule;
import model.Token;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println(
                "1 - perform the parsing of an input sequence \n" +
                "2 - perform the parsing for a program \n" +
                "0 - exit\n");
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Choose: ");
        int input = keyboard.nextInt();

        while (input != 0) {
            switch (input) {
                case 1:
                    String sequence = ReadSequence.read("sequence.txt");
                    System.out.println("Sequence: " + sequence);
                    Parser parser = new Parser();
                    parser.readRules();

                    parser.initialize(sequence);
                    if (parser.parse()) {
                        System.out.println("Sequence " + sequence + " accepted!");
                    } else {
                        System.out.println("Sequence " + sequence + " not accepted!");
                    }
                    break;
                case 2:
                    Nonterminal start = new Nonterminal("S");

                    Nonterminal conditieAux = new Nonterminal("CONDAUX");
                    ProductionRule conditieAuxRul1 = new ProductionRule();
                    ProductionRule conditieAuxRul2 = new ProductionRule();
                    ProductionRule conditieAuxRul3 = new ProductionRule();
                    ProductionRule conditieAuxRul4 = new ProductionRule();
                    ProductionRule conditieAuxRul5 = new ProductionRule();
                    ProductionRule conditieAuxRul6 = new ProductionRule();
                    Nonterminal conditie = new Nonterminal("COND");

                    conditieAuxRul1.setRule(Arrays.asList(new Token(">"), conditie));
                    conditieAuxRul3.setRule(Arrays.asList(new Token("<"), conditie));
                    conditieAuxRul4.setRule(Arrays.asList(new Token("=="), conditie));
                    conditieAuxRul5.setRule(Arrays.asList(new Token("<="), conditie));
                    conditieAuxRul6.setRule(Arrays.asList(new Token(">="), conditie));
                    conditieAux.setProductionRules(Arrays.asList(conditieAuxRul1, conditieAuxRul2, conditieAuxRul3));//,conditieAuxRul4,conditieAuxRul5,conditieAuxRul6));

                    ProductionRule conditieRule1 = new ProductionRule();
                    ProductionRule conditieRule2 = new ProductionRule();
                    conditieRule1.setRule(Arrays.asList(new Token("CONST"), conditieAux));
                    conditieRule2.setRule(Arrays.asList(new Token("ID"), conditieAux));
                    conditie.setProductionRules(Arrays.asList(conditieRule1, conditieRule2));

                    Nonterminal myIf = new Nonterminal("IF");
                    ProductionRule myIfRule1 = new ProductionRule();
                    myIfRule1.setRule(Arrays.asList(new Token("if"), new Token("("), conditie, new Token(")"), new Token("{"), start, new Token("}")));
                    myIf.setProductionRules(Arrays.asList(myIfRule1));

                    Nonterminal afisare = new Nonterminal("AFIS");
                    ProductionRule afisRule1 = new ProductionRule();
                    afisRule1.setRule(Arrays.asList(new Token("cout"), new Token("("), new Token("ID"), new Token(")"), new Token(";"))); //!!!!
                    afisare.setProductionRules(Arrays.asList(afisRule1));

                    Nonterminal citire = new Nonterminal("CITIRE");
                    ProductionRule citRule1 = new ProductionRule();
                    citRule1.setRule(Arrays.asList(new Token("cin"), new Token("("), new Token("ID"), new Token(")"), new Token(";"))); //!!!!
                    citire.setProductionRules(Arrays.asList(citRule1));

                    Nonterminal expresieAux = new Nonterminal("EXPRAUX");
                    ProductionRule expresieAuxRul1 = new ProductionRule();
                    ProductionRule expresieAuxRul2 = new ProductionRule();
                    ProductionRule expresieAuxRul3 = new ProductionRule();
                    ProductionRule expresieAuxRul4 = new ProductionRule();
                    ProductionRule expresieAuxRul5 = new ProductionRule();
                    ProductionRule expresieAuxRul6 = new ProductionRule();
                    Nonterminal expresie = new Nonterminal("EXPR");

                    expresieAuxRul1.setRule(Arrays.asList(new Token("*"), expresie));
                    expresieAuxRul3.setRule(Arrays.asList(new Token("+"), expresie));
                    expresieAuxRul4.setRule(Arrays.asList(new Token("-"), expresie));
                    expresieAuxRul5.setRule(Arrays.asList(new Token("%"), expresie));
                    expresieAuxRul6.setRule(Arrays.asList(new Token("/"), expresie));
                    expresieAux.setProductionRules(Arrays.asList(expresieAuxRul1, expresieAuxRul2, expresieAuxRul3, expresieAuxRul4, expresieAuxRul5, expresieAuxRul6));

                    ProductionRule exprRule1 = new ProductionRule();
                    ProductionRule exprRule2 = new ProductionRule();
                    exprRule1.setRule(Arrays.asList(new Token("CONST"), expresieAux));
                    exprRule2.setRule(Arrays.asList(new Token("ID"), expresieAux));
                    expresie.setProductionRules(Arrays.asList(exprRule1, exprRule2));

                    Nonterminal atribuire = new Nonterminal("ATRIBUIRE");
                    ProductionRule atrRule1 = new ProductionRule();
                    atrRule1.setRule(Arrays.asList(new Token("ID"), new Token("="), expresie, new Token(";"))); //!!!!
                    atribuire.setProductionRules(Arrays.asList(atrRule1));

                    Nonterminal tip = new Nonterminal("F");
                    ProductionRule tipRule1 = new ProductionRule();
                    ProductionRule tipRule2 = new ProductionRule();
                    tipRule1.setRule(Arrays.asList(new Token("int")));
                    tipRule2.setRule(Arrays.asList(new Token("double")));
                    tip.setProductionRules(Arrays.asList(tipRule1, tipRule2));

                    Nonterminal decl = new Nonterminal("D");
                    ProductionRule declRule1 = new ProductionRule();
                    declRule1.setRule(Arrays.asList(tip, new Token("ID"), new Token(";"))); //!!!!
                    decl.setProductionRules(Arrays.asList(declRule1));

                    Nonterminal instr = new Nonterminal("I");
                    ProductionRule instrRule1 = new ProductionRule();
                    ProductionRule instrRule2 = new ProductionRule();
                    ProductionRule instrRule3 = new ProductionRule();
                    ProductionRule instrRule4 = new ProductionRule();
                    ProductionRule instrRule5 = new ProductionRule();
                    instrRule1.setRule(Collections.singletonList(decl));
                    instrRule2.setRule(Collections.singletonList(atribuire));
                    instrRule3.setRule(Collections.singletonList(citire));
                    instrRule4.setRule(Collections.singletonList(afisare));
                    instrRule5.setRule(Collections.singletonList(myIf));
                    instr.setProductionRules(Arrays.asList(instrRule1, instrRule2, instrRule3, instrRule4, instrRule5));


//                    Nonterminal start = new Nonterminal("L");
                    ProductionRule listaiRule1 = new ProductionRule();
                    ProductionRule listaiRule2 = new ProductionRule();
                    listaiRule1.setRule(Collections.singletonList(instr));
                    listaiRule2.setRule(Arrays.asList(instr, start));
                    start.setProductionRules(Arrays.asList(listaiRule1, listaiRule2));  /**start = instr | start instr*/

                    Nonterminal antet = new Nonterminal("A");
                    ProductionRule antetRule = new ProductionRule();
                    antetRule.setRule(Arrays.asList(new Token("int"), new Token("main"), new Token("("), new Token(")")));
                    antet.setProductionRules(Collections.singletonList(antetRule)); /**antet= int main ( )*/
                    Nonterminal program = new Nonterminal("P");
                    ProductionRule programRule = new ProductionRule();
                    programRule.setRule(Arrays.asList(antet, new Token("{"), start, new Token("}"))); //<-recursiv la stanga
                    program.setProductionRules(Collections.singletonList(programRule));
                    //rules!!!

                    TokenParser programParser = new TokenParser();

                    List<Token> programSequence = programParser.getPif();
//                    for(Token i : programSequence) {
//                        appendToFile("pif.txt", i.getValue());
//                    }

                    programParser.initialize(program, programSequence);
                    try {
                        if (programParser.parse()) {
                            System.out.println("Sequence " + programSequence + " accepted!");
                        } else {
                            System.out.println("Sequence " + programSequence + " not accepted!");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("Wrong option\n");

            }
            System.out.print("Choose: ");
            input = keyboard.nextInt();

        }
    }

//    public static void appendToFile(String fileName, String content) throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
//        writer.append(content);
//        writer.append('\n');
//
//        writer.close();
//    }

}
