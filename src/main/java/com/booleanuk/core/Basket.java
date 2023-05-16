package com.booleanuk.core;


import com.booleanuk.core.models.Bagel;
import com.booleanuk.core.models.Coffee;
import com.booleanuk.core.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Basket {
    List<Item> basket;
    ArrayList<Integer> basketQuantity;
    private ArrayList<Integer> itemQuantityAfterDiscount12_6;
    private int capacity;
    int sizeOfBasket;

    private Invetory invetory;


    public Basket() {
        basket = new ArrayList<>();
        basketQuantity = new ArrayList<>();
        invetory = new Invetory();
        itemQuantityAfterDiscount12_6 = new ArrayList<>();
        capacity = 3;
        sizeOfBasket = 0;
    }

    boolean add(Item item, int quantity) {
        if (quantity < 0) {
            return false;
        }
        if (quantity + sizeOfBasket >= capacity) {
            return false;
        }
        //Check if its a item of the Invetory!
        boolean isValid = isValidInvetoryItem(item);
        if (isValid) {
            if (basket.contains(item)) {
                int index = basket.indexOf(item);
                int previousQuantity = basketQuantity.get(index);
                basketQuantity.set(index, previousQuantity + quantity);
            } else {
                basket.add(item);
                basketQuantity.add(quantity);
                sizeOfBasket += quantity;
            }
        }
        return isValid;
    }


    boolean remove(Item item, int quantity) {
        if (!basket.contains(item) || (quantity < 0)) {
            return false;
        }
        int index = basket.indexOf(item);
        int previousQuantity = basketQuantity.get(index);
        if (previousQuantity < quantity) {
            return false;
        } else if (previousQuantity == quantity) {
            basket.remove(index);
            basketQuantity.remove(index);
        } else {
            basketQuantity.set(index, previousQuantity - quantity);
        }
        sizeOfBasket -= quantity;
        return true;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean setCapacity(int newCapacity) {
        if (sizeOfBasket <= newCapacity && newCapacity > 0) {
            this.capacity = newCapacity;
            return true;
        }
        return false;
    }

    public double getTotalOfBasket() {
        double totalPrice = 0.0;
        for (Item item : basket) {
            int index = basket.indexOf(item);
            int quantityOfItem = basketQuantity.get(index);
            if (item.getClass() == Bagel.class) {
                double priceOfBagelFillings = getPriceOfBagelFillings((Bagel) item, index);
                totalPrice += priceOfBagelFillings + item.getPrice() * quantityOfItem;
            } else {
                totalPrice += item.getPrice() * quantityOfItem;
            }
        }
        totalPrice = (double) Math.round(totalPrice * 100) / 100;
        return totalPrice;
    }

    public double getTotalWithDiscountBasket() {
        double totalPrice = 0.0;
        itemQuantityAfterDiscount12_6 = (ArrayList<Integer>) basketQuantity.clone();
        //First check the bagel only and get the discount price
        for (Item item : basket) {
            if (Bagel.class != item.getClass()) {
                continue;
            }
            int indexOfBasket = basket.indexOf(item);
            //Price with Discount
            double priceAfterDiscount = discountBagelCalculator(indexOfBasket);

            //Fillings Price
            double priceOfBagelFillings = getPriceOfBagelFillings((Bagel) item, indexOfBasket);

            //the Leftovers for now todo check for Coffee + Bagel Discount!
            int quantityOfItemAfterDicount = itemQuantityAfterDiscount12_6.get(indexOfBasket);
            double priceBagelWithoutDiscount = item.getPrice() * quantityOfItemAfterDicount;

            totalPrice += priceOfBagelFillings + priceAfterDiscount + priceBagelWithoutDiscount;
        }
        totalPrice = (double) Math.round(totalPrice * 100) / 100;
        return totalPrice;
    }

    private double getPriceOfBagelPlusCoffee(int indexOfBasket) {
        double total = 0.0;
        int BagelsLeft = itemQuantityAfterDiscount12_6.get(indexOfBasket);
        for (Item item : basket) {
            if (item.getClass() != Coffee.class) {
                continue;
            }

            //todo get COffee+Bagel Back


        }


        return total;

    }

    private double getPriceOfBagelFillings(Bagel item, int indexOfBasket) {
        int quantityOfItemForFillings = basketQuantity.get(indexOfBasket);
        double priceOfBagelFillings = item.getFillingsPrice() * quantityOfItemForFillings;
        return priceOfBagelFillings;
    }

    private double discountBagelCalculator(int index) {
        double total = 0.0;
        int q12 = 0;
        int q6 = 0;
        int quantity = basketQuantity.get(index);
        while (quantity >= 12) {
            q12 += 1;
            quantity -= 12;
        }
        while (quantity >= 6) {
            q6 += 1;
            quantity -= 6;
        }
        itemQuantityAfterDiscount12_6.set(index, quantity);
        total += 2.49 * q6 + 3.99 * q12;
        return total;
    }

    private boolean isValidInvetoryItem(Item item) {
        boolean isValid = false;
        for (Bagel bagel : invetory.bagels) {
            String sku = bagel.getSKU();
            if (Objects.equals(item.getSKU(), sku)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            for (Coffee coffee : invetory.coffees) {
                String sku = coffee.getSKU();
                if (Objects.equals(item.getSKU(), sku)) {
                    isValid = true;
                    break;
                }
            }
        }
        return isValid;
    }
}
