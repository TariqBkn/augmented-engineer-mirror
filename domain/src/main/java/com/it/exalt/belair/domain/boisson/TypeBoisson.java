package com.it.exalt.belair.domain.boisson;

public enum TypeBoisson {
    NON_ALCOOLISEE(0),
    ALCOOLISEE_NORMALE(1),
    ALCOOLISEE_PREMIUM(2);

    private final int cout;

    TypeBoisson(int cout) {
        this.cout = cout;
    }

    public int cout() {
        return cout;
    }
}
