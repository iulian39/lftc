package model;

public enum StateType {
    Q, //normal state
    B, //back state
    F, //end state
    E; //error state

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
