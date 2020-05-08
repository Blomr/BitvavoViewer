package nl.remcoblom.bitvavoviewer;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Properties properties;
    private Map<Currency, Asset> assets;
    private Map<Currency, Market> markets;
    private Map<Currency, Double> assetsInEUR;

    private SwipeRefreshLayout swipeRefreshLayout;

    private void initialize() {
        try {
            properties = loadProperties();
            loadDataFromBitvavo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        System.out.println(swipeRefreshLayout == null);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    refreshDataFromBitvavo();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                adapter.notifyDataSetChanged();
//                setToolbarText(getTotalValueAssetsInEUR());
//            }
//        });

        initialize();

        recyclerView = findViewById(R.id.assetsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AssetAdapter(assets, assetsInEUR, markets);
        recyclerView.setAdapter(adapter);

        setToolbarText(getTotalValueAssetsInEUR());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_refresh) {
            try {
                refreshDataFromBitvavo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private Properties loadProperties() throws IOException {
        if (properties == null) {
            InputStream inputStream = getBaseContext().getAssets().open("Bitvavo.properties");
            properties = new Properties();
            properties.load(inputStream);
        }
        return properties;
    }

    private void loadDataFromBitvavo() throws JSONException, ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        GetAssetsTask getAssetsTask = new GetAssetsTask(properties);
        assets = service.submit(getAssetsTask).get();
        GetMarketsTask getMarketsTask = new GetMarketsTask(properties);
        markets = service.submit(getMarketsTask).get();
        service.shutdown();
        assetsInEUR = getAssetsInEUR();
    }

    private void refreshDataFromBitvavo() throws JSONException, ExecutionException, InterruptedException {
            swipeRefreshLayout.setRefreshing(true);
            ExecutorService service = Executors.newSingleThreadExecutor();
            GetAssetsTask getAssetsTask = new GetAssetsTask(properties);
            Map<Currency, Asset> latestAssets = service.submit(getAssetsTask).get();
            for (Currency currency : assets.keySet()) {
                assets.put(currency, latestAssets.get(currency));
            }
            GetMarketsTask getMarketsTask = new GetMarketsTask(properties);
            Map<Currency, Market> latestMarkets = service.submit(getMarketsTask).get();
            for (Currency currency : markets.keySet()) {
                markets.put(currency, latestMarkets.get(currency));
            }
            service.shutdown();
            Map<Currency, Double> latestAssetsInEUR = getAssetsInEUR();
            for (Currency currency : assetsInEUR.keySet()) {
                assetsInEUR.put(currency, latestAssetsInEUR.get(currency));
            }
            adapter.notifyDataSetChanged();
            setToolbarText(getTotalValueAssetsInEUR());
            swipeRefreshLayout.setRefreshing(false);
    }

    private Map<Currency, Double> getAssetsInEUR() {
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

    private double getTotalValueAssetsInEUR() {
        return assetsInEUR.values().stream().mapToDouble(i -> i).sum();
    }

    private void setToolbarText(double amount) {
        TextView toolbarText = findViewById(R.id.toolbarText);
        NumberFormat format =  NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
        toolbarText.setText(format.format(amount));
    }

    @Override
    public void onRefresh() {
        try {
            refreshDataFromBitvavo();
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
