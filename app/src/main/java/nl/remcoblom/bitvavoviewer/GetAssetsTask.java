package nl.remcoblom.bitvavoviewer;

import org.json.JSONException;

import java.util.Map;
import java.util.Properties;

public class GetAssetsTask extends APIRequesterTask<Map<Currency, Asset>> {

    public GetAssetsTask(Properties properties) throws JSONException {
        super(properties);
    }

    @Override
    public Map<Currency, Asset> call() throws Exception {
        return getAPIRequester().getAvailableAssets();
    }
}
