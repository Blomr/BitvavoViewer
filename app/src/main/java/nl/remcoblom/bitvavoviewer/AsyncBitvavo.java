package nl.remcoblom.bitvavoviewer;

import android.os.AsyncTask;
import com.bitvavo.api.Bitvavo;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.concurrent.Callable;

public class AsyncBitvavo implements Callable<JSONArray> {

    private Bitvavo bitvavo;



    public JSONArray ordersOpen(JSONObject jsonObject) throws JSONException {
        return bitvavo.ordersOpen(jsonObject);
    }

    @Override
    public JSONArray call() throws Exception {
        return null;
    }
}
