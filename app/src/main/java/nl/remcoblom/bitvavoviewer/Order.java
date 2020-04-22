package nl.remcoblom.bitvavoviewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Order implements Comparable<Order> {

    private final Market market;
    private final String side;
    private final double amount;
    private final double price;

    public Order(Market market, JSONObject jsonObject) {
        try {
            if (!jsonObject.getString(APIRequester.KEY_MARKET).equals(market.getName())) {
                throw new IllegalArgumentException("Order() - market");
            }
            this.market = market;
            String sideValue = jsonObject.getString(APIRequester.KEY_SIDE);
            if (!isValidValue(sideValue)) {
                throw new IllegalArgumentException("Order() - side");
            }
            this.side = sideValue;
            this.amount = jsonObject.getDouble(APIRequester.KEY_AMOUNT_REMAINING);
            this.price = jsonObject.getDouble(APIRequester.KEY_PRICE);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private boolean isValidValue(String value) {
        return value.equals(APIRequester.VALUE_BUY) || value.equals(APIRequester.VALUE_SELL);
    }

    public Market getMarket() {
        return market;
    }

    public String getSide() {
        return side;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public int compareTo(Order order) {
        if (this.equals(order)) {
            return 0;
        } else if (market.equals(order.getMarket())) {
            return (int) (price - order.getPrice());
        } else {
            return market.compareTo(order.getMarket());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.amount, amount) == 0 &&
                Double.compare(order.price, price) == 0 &&
                Objects.equals(market, order.market) &&
                Objects.equals(side, order.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(market, side, amount, price);
    }
}
