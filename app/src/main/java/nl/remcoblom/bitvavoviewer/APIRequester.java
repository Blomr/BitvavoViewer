package nl.remcoblom.bitvavoviewer;

import com.bitvavo.api.Bitvavo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

class APIRequester {

    private static APIRequester instance;
    private static Bitvavo bitvavo;
    private static Properties bitvavoApiKeyProperties;
    public static final String KEY_MARKET = "market";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CURRENCY = "symbol";
    public static final String KEY_AVAILABLE = "available";
    public static final String KEY_INORDER = "inOrder";
    public static final String KEY_SIDE = "side";
    public static final String KEY_AMOUNT_REMAINING = "amountRemaining";
    public static final String VALUE_BUY = "buy";
    public static final String VALUE_SELL = "sell";

    private APIRequester() {
    }

    private static JSONObject getAPIKey() throws JSONException {
        String keyAPIKey = "APIKEY";
        String keyAPISecret = "APISECRET";
        String valueAPIKey = bitvavoApiKeyProperties.getProperty(keyAPIKey);
        String valueAPISecret = bitvavoApiKeyProperties.getProperty(keyAPISecret);
        return new JSONObject("{" +
                keyAPIKey + ":" + valueAPIKey + "," +
                keyAPISecret + ":" + valueAPISecret + "," +
                "ACCESSWINDOW: 50000, " +
                "DEBUGGING: false }");
    }

    static APIRequester getInstance(Properties properties) throws JSONException {
        if (instance == null) {
            instance = new APIRequester();
            bitvavoApiKeyProperties = properties;
            bitvavo = new Bitvavo(getAPIKey());
        }
        return instance;
    }

    Map<Currency, Market> getAvailableMarkets() throws JSONException {
        JSONArray arrayResponse = bitvavo.tickerPrice(new JSONObject());
        Map<Currency, Market> availableMarkets = new HashMap<>();
        for (int i = 0; i < arrayResponse.length(); i++) {
            JSONObject jsonObject = arrayResponse.getJSONObject(i);
            Currency currency = Market.getBaseCurrencyFromMarketString(jsonObject.getString(KEY_MARKET));
            Market market = new Market(jsonObject);
            availableMarkets.put(currency, market);
        }
        return availableMarkets;
    }

    Map<Currency, Asset> getAvailableAssets() throws JSONException {
        JSONArray arrayResponse = bitvavo.balance(new JSONObject());
        Map<Currency, Asset> availableAssets = new HashMap<>();
        for (int i = 0; i < arrayResponse.length(); i++) {
            JSONObject jsonObject = arrayResponse.getJSONObject(i);
            Asset asset = new Asset(jsonObject);
            availableAssets.put(asset.getCurrency(), asset);
        }
        return availableAssets;
    }

    Map<Market, Set<Order>> getOpenOrders(Map<Currency, Market> availableMarkets) throws JSONException {
        JSONObject jsonObjectRequest = new JSONObject();
        JSONArray arrayResponse = bitvavo.ordersOpen(jsonObjectRequest);
        Map<Market, Set<Order>> openOrders = new HashMap<>();
        for (int i = 0; i < arrayResponse.length(); i++) {
            JSONObject jsonObject = arrayResponse.getJSONObject(i);
            Market market = Market.parse(jsonObject.getString(KEY_MARKET), availableMarkets);
            openOrders.compute(market, (k, v) -> {
                Set<Order> orderSet;
                if (v == null) {
                    orderSet = new TreeSet<>();
                } else {
                    orderSet = v;
                }
                orderSet.add(new Order(market, jsonObject));
                return orderSet;
            });
        }
        return openOrders;
    }
}
