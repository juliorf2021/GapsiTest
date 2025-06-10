package com.example.gapsitest.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gapsitest.R;
import com.example.gapsitest.data.model.Product;
import com.example.gapsitest.data.repository.ProductRepository;
import com.example.gapsitest.data.repository.ProductRepositoryImpl;
import com.example.gapsitest.data.service.ApiService;
import com.example.gapsitest.di.AppModule;
import com.example.gapsitest.presenter.ProductContract;
import com.example.gapsitest.presenter.ProductPresenter;
import com.example.gapsitest.view.adapter.ProductAdapter;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductSearchActivity extends AppCompatActivity implements ProductContract.View {
    private ProductContract.Presenter presenter;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ProgressBar progressBar;


    private PopupWindow searchHistoryPopup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        progressBar = findViewById(R.id.progressBar);

        // Configurar RecyclerView
        adapter = new ProductAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1) {
                    presenter.loadMoreProducts();
                }
            }
        });

        // Configurar búsqueda
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showHistoryIfAvailable();
            }
        });

        searchEditText.setOnClickListener(v -> {
            showHistoryIfAvailable();
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Inicializar Presenter y Repository
        ProductRepository repository = AppModule.provideProductRepository(this);
        presenter = new ProductPresenter(this, repository);

        // Cargar historial
        presenter.loadSearchHistory();
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            if (searchHistoryPopup != null && searchHistoryPopup.isShowing()) {
                searchHistoryPopup.dismiss();
            }
            presenter.searchProducts(query, true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        }
    }

    @Override
    public void showProducts(List<Product> products, boolean isNewSearch) {
        if (isNewSearch) {
            adapter.setProducts(products);
        } else {
            adapter.addProducts(products);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateSearchHistory(List<String> history) {
        // Mostrar historial en un RecyclerView o AutoCompleteTextView
    }


    private void showSearchHistoryPopup() {
        List<String> history = presenter.getSearchHistory();
        if (history == null || history.isEmpty()) return;

        if (searchHistoryPopup != null && searchHistoryPopup.isShowing()) {
            searchHistoryPopup.dismiss();
        }

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.WHITE); // Fondo blanco para el contenedor
        container.setElevation(4f);

        ListView listView = new ListView(this);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(new ColorDrawable(Color.LTGRAY));
        listView.setDividerHeight(1);

        int marginInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8,
                getResources().getDisplayMetrics()
        );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);
        container.addView(listView, params);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                history
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selected = history.get(position);
            searchEditText.setText(selected);
            searchHistoryPopup.dismiss();
            presenter.searchProducts(selected, true);
        });

        searchHistoryPopup = new PopupWindow(
                container,
                searchEditText.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        searchHistoryPopup.setFocusable(false);
        searchHistoryPopup.setOutsideTouchable(true);
        searchHistoryPopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchHistoryPopup.setElevation(16f);

        searchHistoryPopup.showAsDropDown(searchEditText);

        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showHistoryIfAvailable() {
        if (!presenter.getSearchHistory().isEmpty() &&
                (searchHistoryPopup == null || !searchHistoryPopup.isShowing())) {
            showSearchHistoryPopup();
        }
    }
}
