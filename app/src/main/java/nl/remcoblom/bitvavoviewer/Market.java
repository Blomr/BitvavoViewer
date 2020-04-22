package nl.remcoblom.bitvavoviewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;

public class Market implements Comparable<Market> {

    private final Currency baseCurrency;
    private final Currency marketCurrency;
    private final double price;

    public Market(Currency baseCurrency, Currency marketCurrency, double price) {
        this.baseCurrency = baseCurrency;
        this.marketCurrency = marketCurrency;
        this.price = price;
    }

    public Market(JSONObject jsonObject) {
        try {
            this.baseCurrency = getBaseCurrencyFromMarketString(jsonObject.getString(APIRequester.KEY_MARKET));
            this.marketCurrency = getMarketCurrencyFromMarketString(jsonObject.getString(APIRequester.KEY_MARKET));
            this.price = jsonObject.getDouble(APIRequester.KEY_PRICE);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Market parse(String marketSymbol, Map<Currency, Market> availableMarkets) {
        return availableMarkets.get(getBaseCurrencyFromMarketString(marketSymbol));
    }

    public static Currency getBaseCurrencyFromMarketString(String marketSymbol) {
        String[] currencies = marketSymbol.split("-");
        return Currency.valueOf(currencies[0]);
    }

    public static Currency getMarketCurrencyFromMarketString(String marketSymbol) {
        String[] currencies = marketSymbol.split("-");
        return Currency.valueOf(currencies[1]);
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getMarketCurrency() {
        return marketCurrency;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return baseCurrency + "-" + marketCurrency;
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#####.#########");
        return baseCurrency + "-" + marketCurrency + ":" + decimalFormat.format(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market = (Market) o;
        return baseCurrency == market.baseCurrency &&
                marketCurrency == market.marketCurrency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, marketCurrency);
    }

    @Override
    public int compareTo(Market market) {
        return this.toString().compareTo(market.toString());
    }
}
