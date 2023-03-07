package com.app.gmv3innova.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.gmv3innova.R;
import com.app.gmv3innova.adapters.AdapterLast3M;
import com.app.gmv3innova.models.Factura_lineas;
import com.app.gmv3innova.utilities.ItemOffsetDecoration;
import com.app.gmv3innova.utilities.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.app.gmv3innova.utilities.Constant.GET_LAST_3M;

public class ActivityUltimos3m extends AppCompatActivity {
    String cod_factura,name_text;

    private AdapterLast3M mAdapter;
    private RecyclerView recyclerView;
    private List<Factura_lineas> productList;
    TextView txt_factura_total;
    View lyt_empty_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_simple);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        cod_factura = intent.getStringExtra("factura_id");
        name_text = intent.getStringExtra("Name_cliente");
        lyt_empty_history = findViewById(R.id.lyt_empty_result);
        getSupportActionBar().setTitle("Compras Ultimos 3 Meses.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.grey_5));
        }
        Utils.setSystemBarLight(this);

        txt_factura_total = findViewById(R.id.id_factura_total);




        recyclerView = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        mAdapter = new AdapterLast3M(this, productList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        fetchData();
    }


    private void fetchData() {
        JsonArrayRequest request = new JsonArrayRequest(GET_LAST_3M + cod_factura, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                    return;
                }

                List<Factura_lineas> items = new Gson().fromJson(response.toString(), new TypeToken<List<Factura_lineas>>() {
                }.getType());
                productList.clear();
                productList.addAll(items);

                if (productList.size() > 0) {
                    lyt_empty_history.setVisibility(View.GONE);
                    double Order_price = 0;

                    for (int i = 0; i < items.size(); i++) {
                        Log.e("INFO", "Error: " + items.get(i).getVENTA());
                        double Sub_total_price = Double.parseDouble(items.get(i).getVENTA());
                        Order_price += Sub_total_price;


                    }
                    String _Order_price = String.format(Locale.ENGLISH, "%1$,.2f", Order_price);
                    txt_factura_total.setText(("C$ ").concat(_Order_price));
                } else {
                    lyt_empty_history.setVisibility(View.VISIBLE);
                }






                // refreshing recycler view
                mAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e("INFO", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        Utils.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_no_facturado:

                Intent intent = new Intent(ActivityUltimos3m.this, ActivityNoFacturado.class);
                intent.putExtra("factura_id",cod_factura);

                startActivity(intent);
                return true;

            case android.R.id.home:
                finish();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
