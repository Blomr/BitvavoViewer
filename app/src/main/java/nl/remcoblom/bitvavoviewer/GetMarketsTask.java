package nl.remcoblom.bitvavoviewer;

import org.json.JSONException;

import java.util.Map;
import java.util.Properties;

public class GetMarketsTask extends APIRequesterTask<Map<Currency, Market>> {

    public GetMarketsTask(Properties properties) throws JSONException {
        super(properties);
    }

    @Override
    public Map<Currency, Market> call() throws Exception {
        return getAPIRequester().getAvailableMarkets();
    }
}
