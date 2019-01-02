package console;

import model.StateMachine;
import model.StateNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MyFileReader {

    public static void initialize(StateMachine sm, String fileName) {
        BufferedReader br = null;
        FileReader fr = null;

        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);

            String line;

            line = br.readLine();
            sm.alphabet = line.trim().split(" ");
            sm.alphabet = Arrays.copyOf(sm.alphabet, sm.alphabet.length - 1);
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.trim().split(" ");
                StateNode currentState = getState(sm.states, splitLine[0]);
                if (currentState == null) {
                    currentState = new StateNode(splitLine[0]);
                    sm.states.add(currentState);
                }

                for (int i = 1; i < splitLine.length - 1; i++) {
                    StateNode nextState = getState(sm.states, splitLine[i]);
                    boolean isTransition = true;
                    if (nextState == null) {
                        if (splitLine[i].equals("-")) {
                            isTransition = false;
                        } else {
                            isTransition = true;
                            nextState = new StateNode(splitLine[i]);
                            sm.states.add(nextState);
                        }
                    }
                    if (isTransition)
                        currentState.addTransition(sm.alphabet[i - 1], nextState);
                }

                currentState.setFinal(splitLine[splitLine.length - 1].equals("1"));
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }

    public static StateNode getState(List<StateNode> states, String name) {
        return states.stream()
                .filter(stateNode -> stateNode.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
