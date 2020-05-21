package nl.remcoblom.bitvavoviewer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Properties properties;
    private Map<Currency, Asset> assets;
    private Map<Currency, Market> markets;
    private Map<Currency, Double> assetsInEUR;

    private SwipeRefreshLayout swipeRefreshLayout;
//    private TextView toolbarText;
    private TextView totalsTextView;

    public MainListFragment() {
        // Required empty public constructor
    }

    private void initialize() {
        try {
            properties = loadProperties();
            loadDataFromBitvavo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Properties loadProperties() throws IOException {
        if (properties == null) {
            InputStream inputStream = this.getActivity().getBaseContext().getAssets().open("Bitvavo.properties");
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        swipeRefreshLayout = getActivity().findViewById(R.id.swipeContainer);
//        swipeRefreshLayout.setOnRefreshListener(this);

//        toolbarText = container.findViewById(R.id.toolbarText);
//
//        initialize();
//
//        recyclerView = container.findViewById(R.id.assetsRecyclerView);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this.getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new AssetAdapter(assets, assetsInEUR, markets);
//        recyclerView.setAdapter(adapter);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_list, container, false);
    }

    private void setToolbarText(double amount) {
        NumberFormat format =  NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
//        toolbarText.setText(format.format(amount));
        totalsTextView.setText(format.format(amount));
    }

    private double getTotalValueAssetsInEUR() {
        return assetsInEUR.values().stream().mapToDouble(i -> i).sum();
    }

    @Override
    public void onRefresh() {
        try {
            refreshDataFromBitvavo();
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);

//        toolbarText =  view.findViewById(R.id.toolbarText);
        totalsTextView = view.findViewById(R.id.totalsTextView);

        initialize();

        recyclerView = view.findViewById(R.id.assetsRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AssetAdapter(assets, assetsInEUR, markets);
        recyclerView.setAdapter(adapter);

        setToolbarText(getTotalValueAssetsInEUR());
    }
}
