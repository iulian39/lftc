package model;

import console.MyFileReader;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
    public List<StateNode> states;
    public String[] alphabet;

    public void setStates(List<StateNode> states) {
        this.states = states;
    }

    public void setAlphabet(String[] alphabet) {
        this.alphabet = alphabet;
    }

    public StateMachine(String fileName) {
        states = new ArrayList<>();
        MyFileReader.initialize(this, fileName);
    }

    public String[] getAlphabet() {
        return alphabet;
    }

    public List<StateNode> getStateNodes() {
        return states;
    }
}
