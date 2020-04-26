package nl.remcoblom.bitvavoviewer;

import org.json.JSONException;

import java.util.Properties;
import java.util.concurrent.Callable;

public abstract class APIRequesterTask<V> implements Callable<V> {

    private APIRequester apiRequester;

    public APIRequesterTask(Properties properties) throws JSONException {
        this.apiRequester = APIRequester.getInstance(properties);
    }

    public APIRequester getAPIRequester() {
        return apiRequester;
    }
}
