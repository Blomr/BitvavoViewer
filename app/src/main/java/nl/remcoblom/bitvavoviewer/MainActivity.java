package nl.remcoblom.bitvavoviewer;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                setTextViews();
            }
        });

        totalTextView = findViewById(R.id.totalTextView);
        assetTextView1 = findViewById(R.id.assetTextView1);
        assetTextView2 = findViewById(R.id.assetTextView2);
        assetTextView3 = findViewById(R.id.assetTextView3);
        assetTextView4 = findViewById(R.id.assetTextView4);
        assetTextView5 = findViewById(R.id.assetTextView5);
        assetTextView6 = findViewById(R.id.assetTextView6);
        assetTextView7 = findViewById(R.id.assetTextView7);
        assetTextView8 = findViewById(R.id.assetTextView8);
        assetTextView9 = findViewById(R.id.assetTextView9);
        assetTextView10 = findViewById(R.id.assetTextView10);
        assetTextView11 = findViewById(R.id.assetTextView11);
        assetTextView12 = findViewById(R.id.assetTextView12);
        assetTextView13 = findViewById(R.id.assetTextView13);
        assetTextView14 = findViewById(R.id.assetTextView14);

        setTextViews();
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
        try {
            InputStream inputStream = getBaseContext().getAssets().open("Bitvavo.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            AssetsInEURTask assetsInEURTask = new AssetsInEURTask(properties);
            ExecutorService service = Executors.newSingleThreadExecutor();

            Map<Currency,Double> assetsInEUR = service.submit(assetsInEURTask).get();
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

        } catch (IOException | InterruptedException | ExecutionException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
