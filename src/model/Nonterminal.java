package model;

import java.util.ArrayList;
import java.util.List;

public class Nonterminal extends Token {

    private List<ProductionRule> productionRules;
    private int chosenRule;

    public Nonterminal(String value) {
        super(value);
        chosenRule = 0;
        productionRules = new ArrayList<>();
    }

    public Nonterminal(Nonterminal nonterminal) {
        super(nonterminal.getValue());
        chosenRule = 0;
        productionRules = new ArrayList<>(nonterminal.getProductionRules());
    }

    public List<ProductionRule> getProductionRules() {
        return productionRules;
    }

    public void addProductionRule(ProductionRule productionRule) {
        productionRules.add(productionRule);
    }

    public void nextRule() {
        chosenRule++;
    }

    public int ruleAmount() {
        return productionRules.size();
    }

    public int getChosenRule() {
        return chosenRule;
    }

    public void setChosenRule(int chosenRule) {
        this.chosenRule = chosenRule;
    }

    public ProductionRule getProductionRule() {
        return productionRules.get(chosenRule);
    }

    public void setProductionRules(List<ProductionRule> productionRules) {
        this.productionRules = productionRules;
    }
}
