package model;

import java.util.ArrayList;
import java.util.List;

public class ProductionRule {
    private List<Token> rule;

    public ProductionRule() {
        this.rule = new ArrayList<>();
    }

    public List<Token> getRule() {
        return rule;
    }

    public void setRule(List<Token> rule) {
        this.rule = rule;
    }
}
