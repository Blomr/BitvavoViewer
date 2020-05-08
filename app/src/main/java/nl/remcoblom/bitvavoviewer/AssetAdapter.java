package nl.remcoblom.bitvavoviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetViewHolder> {

    private Map<Currency, Asset> assets;
    private Map<Currency, Double> assetsInEUR;
    private Map<Currency, Market> markets;
    private Currency[] currencies;

    public static class AssetViewHolder extends RecyclerView.ViewHolder {
        public TextView currencySymbol;
        public TextView assetAmount;
        public TextView assetValueInEUR;
        public TextView currencyMarketValue;

        public AssetViewHolder(View view) {
            super(view);
            currencySymbol = view.findViewById(R.id.currencySymbol);
            assetAmount = view.findViewById(R.id.assetAmount);
            assetValueInEUR = view.findViewById(R.id.assetValueInEUR);
            currencyMarketValue = view.findViewById(R.id.currencyMarketValue);
        }
    }

    public AssetAdapter(Map<Currency, Asset> assets, Map<Currency, Double> assetsInEUR, Map<Currency, Market> markets) {
        this.assets = assets;
        this.assetsInEUR = assetsInEUR;
        this.markets =  markets;
        this.currencies = assets.keySet().toArray(new Currency[0]);
        Arrays.sort(currencies, Comparator.naturalOrder());
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout listItem = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        AssetViewHolder assetViewHolder = new AssetViewHolder(listItem);
        return assetViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssetViewHolder holder, int position) {
        holder.currencySymbol.setText(currencies[position].name());
        holder.assetAmount.setText(String.valueOf(assets.get(currencies[position]).getAmount()));
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
        holder.assetValueInEUR.setText(format.format(assetsInEUR.get(currencies[position])));
        if (currencies[position] == Currency.EUR) {
            holder.currencyMarketValue.setText("1");
        } else {
            holder.currencyMarketValue.setText(String.valueOf(markets.get(currencies[position]).getPrice()));
        }
    }

    @Override
    public int getItemCount() {
        return currencies.length;
    }
}
