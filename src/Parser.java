import console.FileReader;
import model.Nonterminal;
import model.ProductionRule;
import model.StateType;
import model.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Parser {
    //if true it means that the analyzer is searching for the next symbol. If it is false, the analyzer does backtracking
    private StateType state;
    private int index; // position in the input sequence
    private List<Token> workStack; //the history of the productions
    private List<Token> inputStack; //sequence that is to be processed
    private boolean first = true;
    private String sequence; //needs to be checked by the analyzer if it is accepted
    private Nonterminal start;

    public Parser() {
        this.state = StateType.Q;
        this.index = 0;
        workStack = new ArrayList<>();
        inputStack = new ArrayList<>();
    }

    public Parser(StateType state, int index, List<Token> workStack, List<Token> inputStack, Nonterminal start) {
        this.state = state;
        this.index = index;
        this.workStack = new ArrayList<>(workStack);
        this.inputStack = new ArrayList<>(inputStack);
        this.start = new Nonterminal(start.getValue());
        this.start.setProductionRules(start.getProductionRules());
    }

    public void initialize( String sequence) {
        this.sequence = sequence;
    }

    private String getWorkStackString() {
        return workStack.isEmpty() ? "\u03B5" : //prints epsilon
                workStack.stream()
                        .map(e -> e instanceof Nonterminal ? e.getValue() + (((Nonterminal) e).getChosenRule() + 1) : e.getValue())
                        .reduce("", String::concat);
    }

    private String getInputStackString() {
        return inputStack.isEmpty() ? "\u03B5" : //prints epsilon
                inputStack.stream()
                        .map(Token::getValue)
                        .reduce("", String::concat);
    }

    /**
     * prints the current production
     */
    public void printProduction() {
        System.out.println("(" + state + "," + (index + 1) + "," + getWorkStackString() + "," + getInputStackString() + ")");
    }

    public boolean parse() {
        printProduction();

        if (index == sequence.length()) {
            if (inputStack.isEmpty()) {
                success();
                printProduction();
                return true;
            }

            state = StateType.B;
            goBack();
            return parse();
        }

        if (state.equals(StateType.E))
            return false;

        if (inputStack.isEmpty() && state.equals(StateType.Q)) {
            localFailure();
            return parse();
        }

        if (state.equals(StateType.Q) && !inputStack.isEmpty() && inputStack.get(0) instanceof Nonterminal) {
            expansion();
            return parse();
        } else {
            if (!inputStack.isEmpty() && state.equals(StateType.Q) && inputStack.get(0).getValue().equals(Character.toString(sequence.charAt(index)))) {
                advance();
                return parse();
            } else {
                switch (state) {
                    case Q:
                        localFailure();
                        return parse();
                    case B:
                        if (workStack.size() == 0) {
                            state = StateType.E;
                            printProduction();
                            return false;
                        } else {
                            Token token = workStack.get(workStack.size() - 1);
                            if (!(token instanceof Nonterminal)) {
                                goBack();
                            } else {
                                anotherTry();
                            }
                            return parse();
                        }

                }
            }
        }
        return false;
    }

    /**
     * puts the nonterminal from the beginning of the input stack and appends it to the end of the work stack. Instead of the nonterminal
     * the input stack starts now with the first rule of the nonterminal
     */
    public void expansion() {
        workStack.add(inputStack.remove(0));
        Nonterminal nonterminal = (Nonterminal) workStack.get(workStack.size() - 1);
        List<Token> rule = new ArrayList<>(nonterminal.getProductionRule().getRule());
        for (int i = 0; i < rule.size(); i++) {
            if (rule.get(i) instanceof Nonterminal) {
                Nonterminal n = new Nonterminal(rule.get(i).getValue());
                n.setProductionRules(((Nonterminal) rule.get(i)).getProductionRules());
                rule.set(i, n);
            }
        }
        inputStack.addAll(0, rule);

    }

    /**
     * The first terminal from the input stack is appended to the end of the work stack and the index is incremented
     */
    public void advance() {
        index++;
        workStack.add(inputStack.remove(0));
    }

    public void localFailure() {
        state = StateType.B;
    }

    /**
     * Decrements the index and moves the last token from the work stack to the beginning of the input stack
     */
    public void goBack() {
        index--;
//        if (workStack.get(workStack.size() - 1) instanceof Nonterminal) {
//            Nonterminal nonterminal = (Nonterminal) workStack.get(workStack.size() - 1);
//            int ruleLength = nonterminal.getProductionRule().getRule().size();
//            inputStack.subList(0, ruleLength).clear(); //remove old rule
//        }
        inputStack.add(0, workStack.remove(workStack.size() - 1));
    }

    /**
     * Another try has 3 options:
     * 1. if the nonterminal at the end of the workstack has another unused rule, then that rule is put to the beginning of the inputstack
     * instead of the old one
     * 2. if there is possibility for backtracking then set back the nonterminal to the beginning of the inputstack(and delete the rule from before)
     * 3. if the index is 0 and the nonterminal is the start terminal then change to the error state
     */
    public void anotherTry() {
        Nonterminal nonterminal = (Nonterminal) workStack.get(workStack.size() - 1);
        if (nonterminal.ruleAmount() > nonterminal.getChosenRule() + 1) {
            state = StateType.Q;
            int ruleLength = nonterminal.getProductionRule().getRule().size();
            inputStack.subList(0, ruleLength).clear(); //remove old rule
            nonterminal.nextRule();
            inputStack.addAll(0, nonterminal.getProductionRule().getRule());
        } else {
            if (index == 0 && workStack.size() == 1 && workStack.get(0).equals(start) && inputStack.equals(start.getProductionRule().getRule())) {
                state = StateType.E;
                int ruleLength = nonterminal.getProductionRule().getRule().size();
                inputStack.subList(0, ruleLength).clear(); //remove old rule
                Nonterminal n = new Nonterminal(nonterminal.getValue());
                n.setProductionRules(nonterminal.getProductionRules());
                workStack.remove(workStack.size() - 1);
                inputStack.add(0, n);
            } else {
                state = StateType.B;
                int ruleLength = nonterminal.getProductionRule().getRule().size();
                inputStack.subList(0, ruleLength).clear(); //remove old rule
                Nonterminal n = new Nonterminal(nonterminal.getValue());
                n.setProductionRules(nonterminal.getProductionRules());
                workStack.remove(workStack.size() - 1);
                inputStack.add(0, n);
            }
        }
    }

    public void success() {
        state = StateType.F;
    }

    public List<Token> getWorkStack() {
        return workStack;
    }

    public StateType getState() {
        return state;
    }

    public List<Token> getInputStack() {
        return inputStack;

    }

    public void setStart(Nonterminal start) {
        this.start = start;
    }

    public void readRules() {
        Nonterminal nonterminal = new Nonterminal("S"); // initialize the start non terminal - S default
        List<String> lines = null;
        List<Nonterminal> nonterminals = new ArrayList<>();

        nonterminals.add(nonterminal);

        try {
            FileReader fr = new FileReader("rules.txt");
            lines = fr.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s : lines) {
            List<Token> lineRule = new ArrayList<>();
            ProductionRule prod = new ProductionRule();
            String start = s.split("->")[0];
            String imp = s.split("->")[1];


            for (char ch : imp.toCharArray()) {
                if (Character.isUpperCase(ch)) {
                    Optional<Nonterminal> checkIfAddedAlready = nonterminals.stream().filter(e -> e.getValue().equals(String.valueOf(ch))).findAny();
                    if (checkIfAddedAlready.isPresent())
                        lineRule.add(checkIfAddedAlready.get());
                    else {
                        nonterminals.add(new Nonterminal(String.valueOf(ch)));
                        lineRule.add(nonterminals.get(nonterminals.size() - 1));
                    }

                } else
                    lineRule.add(new Token(String.valueOf(ch)));
            }
            prod.setRule(lineRule);

            Optional<Nonterminal> CheckExistence = nonterminals.stream().filter(e -> e.getValue().equals(start)).findAny();
            if (!CheckExistence.isPresent()) {
                nonterminals.add(new Nonterminal(start));
                nonterminals.get(nonterminals.size() - 1).addProductionRule(prod);
            } else
                CheckExistence.get().addProductionRule(prod);


        }

        this.inputStack.addAll(nonterminals);

    }
}
