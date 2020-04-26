package nl.remcoblom.bitvavoviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.AssetViewHolder> {

    private Map<Currency, Asset> assets;
    private Map<Currency, Double> assetsInEUR;
    private Currency[] currencies;

    public static class AssetViewHolder extends RecyclerView.ViewHolder {
        public TextView currencySymbol;
        public TextView assetAmount;
        public TextView assetValueInEUR;
        public AssetViewHolder(View view) {
            super(view);
            currencySymbol = view.findViewById(R.id.currencySymbol);
            assetAmount = view.findViewById(R.id.assetAmount);
            assetValueInEUR = view.findViewById(R.id.assetValueInEUR);
        }
    }

    public AssetAdapter(Map<Currency, Asset> assets, Map<Currency, Double> assetsInEUR) {
        this.assets = assets;
        this.assetsInEUR = assetsInEUR;
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
        holder.assetValueInEUR.setText(String.valueOf(assetsInEUR.get(currencies[position])));
    }

    @Override
    public int getItemCount() {
        return currencies.length;
    }
}
