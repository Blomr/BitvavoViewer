package nl.remcoblom.bitvavoviewer;

import org.json.JSONException;
import org.json.JSONObject;

import static nl.remcoblom.bitvavoviewer.APIRequester.*;

public class Asset {

    private final Currency currency;
    private final double amount;

    public Asset(Currency currency, double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Asset(JSONObject jsonObject) {
        try {
            this.currency = Currency.valueOf(jsonObject.getString(KEY_CURRENCY));
            this.amount = jsonObject.getDouble(KEY_AVAILABLE) + jsonObject.getDouble(KEY_INORDER);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public double getValueInOtherCurrency(Market market) {
        return amount * market.getPrice();
    }

    @Override
    public String toString() {
        return currency + ": " + amount;
    }
}
