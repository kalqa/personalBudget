package com.example.mybudgetv2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mybudgetv2.fragments.BudgetHintsFragment;
import com.example.mybudgetv2.fragments.CurrentExpensesFragment;
import com.example.mybudgetv2.fragments.EarningsAndExpensesPlansFragment;
import com.example.mybudgetv2.fragments.ExpensesSummaryFragment;
import com.example.mybudgetv2.fragments.FinancialCalculatorFragment;
import com.example.mybudgetv2.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        displayView(R.id.home, "Home");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void displayView(int viewId, String title) {
        Fragment fragment;

        switch (viewId) {
            case R.id.home:
                fragment = new HomeFragment();
                break;

            case R.id.current_expenses:
                fragment = new CurrentExpensesFragment();
                break;

            case R.id.earnings_and_expenses_plans:
                fragment = new EarningsAndExpensesPlansFragment();
                break;

            case R.id.expenses_summary:
                fragment = new ExpensesSummaryFragment();
                break;

            case R.id.financial_calculator:
                fragment = new FinancialCalculatorFragment();
                break;

            case R.id.budget_hints:
                fragment = new BudgetHintsFragment();
                break;

            default:
                fragment = new HomeFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        if (getSupportActionBar() != null && title != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId(), item.getTitle().toString());
        return true;
    }
}