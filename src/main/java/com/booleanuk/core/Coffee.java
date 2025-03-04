package com.booleanuk.core;


enum COFFEETYPE {
    BLACK,
    WHITE,
    CAPPUCCINO,
    LATTE
}

public class Coffee extends Item {
    final COFFEETYPE type;

    public COFFEETYPE getType() { return type; }

    public Coffee(COFFEETYPE type) {
        this.type = type;
        switch(type) {
            case BLACK:         this.SKU = "COFB"; this.price = 0.99; break;
            case WHITE:         this.SKU = "COFW"; this.price = 1.19; break;
            case CAPPUCCINO:    this.SKU = "COFC"; this.price = 1.29; break;
            case LATTE:         this.SKU = "COFL"; this.price = 1.29; break;
        }
    }
    @Override
    public boolean getAvailable() {
        return BobsInvetory.isCoffeeInInvetory(this.getType());
    }
}
