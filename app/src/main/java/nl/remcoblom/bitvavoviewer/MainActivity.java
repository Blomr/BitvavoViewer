package nl.remcoblom.bitvavoviewer;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private TextView totalTextView;
    private TextView assetTextView1;
    private TextView assetTextView2;
    private TextView assetTextView3;
    private TextView assetTextView4;
    private TextView assetTextView5;
    private TextView assetTextView6;
    private TextView assetTextView7;
    private TextView assetTextView8;
    private TextView assetTextView9;
    private TextView assetTextView10;
    private TextView assetTextView11;
    private TextView assetTextView12;
    private TextView assetTextView13;
    private TextView assetTextView14;

    private TextView toolbarText;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Properties properties;
    private Map<Currency, Asset> assets;
    private Map<Currency, Market> markets;
    private Map<Currency, Double> assetsInEUR;

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
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    refreshDataFromBitvavo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                toolbarText.setText(String.valueOf(getTotalValueAssetsInEUR()));
            }
        });

        initialize();

        recyclerView = findViewById(R.id.assetsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AssetAdapter(assets, assetsInEUR);
        recyclerView.setAdapter(adapter);

        toolbarText = findViewById(R.id.toolbarText);
        toolbarText.setText(String.valueOf(getTotalValueAssetsInEUR()));

//        totalTextView = findViewById(R.id.totalTextView);
//        assetTextView1 = findViewById(R.id.assetTextView1);
//        assetTextView2 = findViewById(R.id.assetTextView2);
//        assetTextView3 = findViewById(R.id.assetTextView3);
//        assetTextView4 = findViewById(R.id.assetTextView4);
//        assetTextView5 = findViewById(R.id.assetTextView5);
//        assetTextView6 = findViewById(R.id.assetTextView6);
//        assetTextView7 = findViewById(R.id.assetTextView7);
//        assetTextView8 = findViewById(R.id.assetTextView8);
//        assetTextView9 = findViewById(R.id.assetTextView9);
//        assetTextView10 = findViewById(R.id.assetTextView10);
//        assetTextView11 = findViewById(R.id.assetTextView11);
//        assetTextView12 = findViewById(R.id.assetTextView12);
//        assetTextView13 = findViewById(R.id.assetTextView13);
//        assetTextView14 = findViewById(R.id.assetTextView14);
//
//        setTextViews();
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTextViews() {
//        try {
//            AssetsInEURTask assetsInEURTask = new AssetsInEURTask(properties);
//            ExecutorService service = Executors.newSingleThreadExecutor();
//
//            Map<Currency,Double> assetsInEUR = service.submit(assetsInEURTask).get();
            Map<Currency,Double> assetsInEUR = getAssetsInEUR();
            assetTextView1.setText(Currency.EUR + ": " + assetsInEUR.get(Currency.EUR));
            assetTextView2.setText(Currency.BTC + ": " + assetsInEUR.get(Currency.BTC));
            assetTextView3.setText(Currency.LTC + ": " + assetsInEUR.get(Currency.LTC));
            assetTextView4.setText(Currency.XRP + ": " + assetsInEUR.get(Currency.XRP));
            assetTextView5.setText(Currency.XLM + ": " + assetsInEUR.get(Currency.XLM));
            assetTextView6.setText(Currency.VET + ": " + assetsInEUR.get(Currency.VET));
            assetTextView7.setText(Currency.VTHO + ": " + assetsInEUR.get(Currency.VTHO));
            assetTextView8.setText(Currency.NEO + ": " + assetsInEUR.get(Currency.NEO));
            assetTextView9.setText(Currency.GAS + ": " + assetsInEUR.get(Currency.GAS));
            assetTextView10.setText(Currency.ONT + ": " + assetsInEUR.get(Currency.ONT));
            assetTextView11.setText(Currency.ONG + ": " + assetsInEUR.get(Currency.ONG));
            assetTextView12.setText(Currency.XVG + ": " + assetsInEUR.get(Currency.XVG));
            assetTextView13.setText(Currency.TRX + ": " + assetsInEUR.get(Currency.TRX));
            assetTextView14.setText(Currency.WTC + ": " + assetsInEUR.get(Currency.WTC));
            totalTextView.setText("Total: " + assetsInEUR.values().stream().mapToDouble(i -> i).sum());
//        } catch (InterruptedException | ExecutionException | JSONException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
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
}
