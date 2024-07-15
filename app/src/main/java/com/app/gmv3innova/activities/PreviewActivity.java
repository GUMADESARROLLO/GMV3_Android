package com.app.gmv3innova.activities;

import static com.app.gmv3innova.utilities.Constant.GET_DETALLE_PEDIDO;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.gmv3innova.R;
import com.app.gmv3innova.models.PEDIDO_LINEAS;
import com.app.gmv3innova.utilities.DBHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;

import java.util.List;

public class PreviewActivity extends AppCompatActivity {
    DBHelper dbhelper;
    String num_pedido;

    TextView txt_num_pedido;
    TextView txt_code_pedido;
    TextView txt_name;
    TextView txt_code_client;
    TextView txt_dir;
    TextView txt_total;
    TextView txt_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.checkout_order_list);
        }
        txt_num_pedido = findViewById(R.id.txt_num_pedido);
        txt_code_pedido = findViewById(R.id.txt_code_pedido);
        txt_name = findViewById(R.id.txt_name);
        txt_code_client = findViewById(R.id.txt_code_client);
        txt_dir = findViewById(R.id.txt_dir);
        txt_total = findViewById(R.id.txt_total);
        txt_date = findViewById(R.id.txt_fecha);

        initDB();
    }

    public void initDB(){
        Intent intent = getIntent();

        num_pedido = intent.getStringExtra("num_pedido");

        Log.e("TAG_num_pedido", "initDB: "+ num_pedido );

        dbhelper = new DBHelper(this);

        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        api_get_pedido();
    }
    private void api_get_pedido() {
        JsonArrayRequest request = new JsonArrayRequest(GET_DETALLE_PEDIDO + num_pedido, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), R.string.failed_fetch_data, Toast.LENGTH_LONG).show();
                    return;
                }
                List<PEDIDO_LINEAS> pedido = new Gson().fromJson(response.toString(), new TypeToken<List<PEDIDO_LINEAS>>() {}.getType());

                UIPedido(pedido);
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

    private void UIPedido(List<PEDIDO_LINEAS> DatosPedido){

        txt_num_pedido.setText(DatosPedido.get(0).getPEDIDO_NUM());
        txt_code_pedido.setText(DatosPedido.get(0).getPEDIDO_ID());
        txt_name.setText(DatosPedido.get(0).getPEDIDO_NOMBRE());
        txt_code_client.setText(DatosPedido.get(0).getPEDIDO_CLIENTE());
        txt_dir.setText(DatosPedido.get(0).getPEDIDO_DIR());
        txt_total.setText(DatosPedido.get(0).getPEDIDO_TOTAL());
        txt_date.setText(DatosPedido.get(0).getPEDIDO_FECHA());

        String[] data_order_list    = DatosPedido.get(0).getPEDIDO_ORDEN().split("],");
        int cLineas                 = data_order_list.length -1;
        String str_detalles_linea = "";

        for (int i = 0; i < cLineas; i++) {

            String[] Lineas_detalles    = data_order_list[i].split(";");

            String Quantity     = Lineas_detalles[0].replace("[","");
            String prod_cod    = Lineas_detalles[1];
            String Menu_name     = Lineas_detalles[2];
            String Bonificado   = Lineas_detalles[3];
            String _Sub_total_price   = Lineas_detalles[4];

            str_detalles_linea += (Quantity + " [ " + prod_cod + " ] " + Menu_name + " " + Bonificado + " " + _Sub_total_price  + "\n\n");

        }

        str_detalles_linea += data_order_list[cLineas].replace(";","").replace("[","").replace("]","");
        ((TextView) findViewById(R.id.product_name)).setText(str_detalles_linea);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_print, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            case R.id.btn_mn_print:
                Toast.makeText(this, "Print Resumen", Toast.LENGTH_SHORT).show();
                onPrint();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void onPrint(){

    }


}