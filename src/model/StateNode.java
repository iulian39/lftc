package model;

import java.util.HashMap;

public class StateNode {
    private String name;
    private HashMap<String, StateNode> transitions;
    private boolean isFinal;

    public StateNode(String name) {
        this.name = name;
        this.transitions = new HashMap<>();
    }

    public void addTransition(String symbol, StateNode nextState) {
        transitions.put(symbol, nextState);
    }

    public StateNode makeTransition(String symbol) {
        return transitions.get(symbol);
    }

    public String getName() {
        return name;
    }

    public HashMap<String, StateNode> getTransitions() {
        return transitions;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isFinal() {
        return isFinal;
    }

}
