package nl.remcoblom.bitvavoviewer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

public class Calculate implements Callable<Double> {

    private Properties properties;

    public Calculate(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Double call() throws Exception {

        APIRequester apiRequester = APIRequester.getInstance(properties);
        Map<Currency, Asset> assets = apiRequester.getAvailableAssets();
        Map<Currency, Market> markets = apiRequester.getAvailableMarkets();

        List<Double> assetsInEur = new ArrayList<>();
        assets.forEach((k, v) -> {
            double assetInEur = 0;
            if (k != Currency.EUR) {
                assetInEur = v.getAmount() * markets.get(k).getPrice();
            } else {
                assetInEur = v.getAmount();
            }
            System.out.println(k + ": " + assetInEur);
            assetsInEur.add(assetInEur);
        });
        return assetsInEur.stream().mapToDouble(i -> i).sum();
    }
}
