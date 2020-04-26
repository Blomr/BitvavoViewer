package nl.remcoblom.bitvavoviewer;

import org.json.JSONException;

import java.util.*;

public class AssetsInEURTask extends APIRequesterTask<Map<Currency, Double>> {

    public AssetsInEURTask(Properties properties) throws JSONException {
        super(properties);
    }

    @Override
    public Map<Currency, Double> call() throws Exception {
        Map<Currency, Asset> assets = getAPIRequester().getAvailableAssets();
        Map<Currency, Market> markets = getAPIRequester().getAvailableMarkets();
        Map<Currency, Double> assetsInEUR = new HashMap<>();
        assets.forEach((k, v) -> {
            double assetInEur = 0;
            if (k != Currency.EUR) {
                assetInEur = v.getAmount() * markets.get(k).getPrice();
            } else {
                assetInEur = v.getAmount();
            }
            assetsInEUR.put(k, assetInEur);
        });
        return assetsInEUR;
    }
}
