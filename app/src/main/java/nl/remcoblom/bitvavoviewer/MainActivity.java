package nl.remcoblom.bitvavoviewer;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

public class MainActivity extends AppCompatActivity {

    private TextView helloWorldTextView;

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
                calculate();
            }
        });

        helloWorldTextView = findViewById(R.id.helloWorldTextView);
        calculate();
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

    private void calculate() {
        try {
            InputStream inputStream = getBaseContext().getAssets().open("Bitvavo.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            Calculate calculate = new Calculate(properties);
            ExecutorService service = Executors.newSingleThreadExecutor();
            double result = service.submit(calculate).get();
            System.out.println("Total: " + result);
            helloWorldTextView.setText(String.valueOf(result));
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
