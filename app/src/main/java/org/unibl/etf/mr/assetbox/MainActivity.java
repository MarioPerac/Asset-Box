package org.unibl.etf.mr.assetbox;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.unibl.etf.mr.assetbox.util.LocaleHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.addAssetFragment, R.id.addCensusListFragment)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home || id == R.id.addAssetFragment || id == R.id.addCensusListFragment) {
                navController.navigate(id);
                return true;
            }

            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.languages_menu, menu); // Inflate your menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_english) {
            LocaleHelper.setLanguage(this, "en");
            recreate();
            return true;
        } else if (id == R.id.action_serbian) {
            LocaleHelper.setLanguage(this, "sr");
            recreate();
            return true;
        } else if (id == android.R.id.home) {
            return NavigationUI.navigateUp(navController, appBarConfiguration);
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}
