package nl.remcoblom.bitvavoviewer;

import java.util.*;
import java.util.concurrent.Callable;

public class AssetsInEURTask implements Callable<Map<Currency,Double>> {

    private Properties properties;

    public AssetsInEURTask(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Map<Currency, Double> call() throws Exception {
        APIRequester apiRequester = APIRequester.getInstance(properties);
        Map<Currency, Asset> assets = apiRequester.getAvailableAssets();
        Map<Currency, Market> markets = apiRequester.getAvailableMarkets();
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
