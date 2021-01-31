package com.developer.mechanicroomvendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.mechanicroomvendor.Model.RowData;

import java.util.ArrayList;

public class JobCartActivity extends AppCompatActivity {
    ArrayList<RowData> items = new ArrayList<RowData>();
    private RowdataAdapter adapter;
    RecyclerView rv_jobcart;
    TextView btn_add;
    public TextView total_price;
    EditText txt_service_name,txt_service_price;
    int total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_cart);

        total_price = findViewById(R.id.total_price);
        txt_service_name =findViewById(R.id.txt_service_name);
        txt_service_price = findViewById(R.id.txt_service_price);
        btn_add = findViewById(R.id.btn_add);

        rv_jobcart = (RecyclerView) findViewById(R.id.listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_jobcart.setLayoutManager(layoutManager);
        // adding first item to List, it's optional step
//        items.add(new RowData("0:00"));

        adapter = new RowdataAdapter(this, items);
        rv_jobcart.setAdapter(adapter);
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // add new row to Adapter
                if (txt_service_name.getText().toString().isEmpty())
                {
                    txt_service_name.setError("Enter Service name");
                    txt_service_name.requestFocus();
                }
                else if (txt_service_price.getText().toString().isEmpty())
                {
                    txt_service_price.setError("Enter Service price");
                    txt_service_price.requestFocus();
                }
                else {
                    String name = txt_service_name.getText().toString();
                    String price= txt_service_price.getText().toString();
                    int s_price = Integer.parseInt(price);
                     total = total + s_price;
                     total_price.setText(""+total);
                    adapter.addRow(new RowData(name, price,false));
                    txt_service_name.setText("");
                    txt_service_price.setText("");
                }
            }
        });
    }

    private class RowdataAdapter extends RecyclerView.Adapter<RowdataAdapter.ViewHolder> {
        Activity context;
        ArrayList<RowData> itemsList=new ArrayList<RowData>();

        public RowdataAdapter(Activity context, ArrayList<RowData> itemsList) {
            this.context = context;
            this.itemsList = itemsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.job_cart_listitem, parent, false);
            return new ViewHolder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//           /* holder.txt_job_cart.setText(""+itemsList.get(position).getS());
//            if (itemsList.get(position).getB()==true)
//            {
//                Log.d("@@@","onAdapter"+itemsList.get(position).getS());
////                txt_name.setCursorVisible(false);
////                txt_name.setFocusable(false);
////                holder.txt_job_cart.setCursorVisible(true);
////                holder.txt_job_cart.setFocusable(true);
//                holder.txt_job_cart.setKeyListener((KeyListener) holder.txt_job_cart.getTag());
//            }
//            else
//            {
//                Log.d("@@@","false onAdapter"+itemsList.get(position).getS());
////                holder.txt_job_cart.setCursorVisible(false);
////                txt_name.setCursorVisible(true);
////                txt_name.setFocusable(true);
//                holder.txt_job_cart.setTag(holder.txt_job_cart.getKeyListener());
//                holder.txt_job_cart.setKeyListener(null);
//            }
//            holder.update.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                   if (itemsList.get(position).getB()==true) {
////                       holder.txt_job_cart.setCursorVisible(true);
////                       txt_name.setCursorVisible(false);
////                       txt_name.setFocusable(false);
//                       itemsList.get(position).setB(false);
//                       itemsList.get(position).setS(holder.txt_job_cart.getText().toString());
//                   }
//                   else
//                   {
//                       itemsList.get(position).setB(true);
////                       holder.txt_job_cart.setCursorVisible(false);
////                       txt_name.setCursorVisible(true);
////                       txt_name.setFocusable(true);
//                   }
//                    adapter.notifyDataSetChanged();
//                }
//            });*/

            holder.txt_service_name.setText(""+itemsList.get(position).getService_name());
            holder.txt_service_price.setText("Price: "+itemsList.get(position).getService_price());

            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemsList.remove(position);
                    int price = Integer.parseInt(itemsList.get(position).getService_price());
                    total = total - price;
                    adapter.notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }

        public void addRow(RowData newRow) {
            // items represents List<RowData> in your Adapter class
            this.itemsList.add(newRow);

            // sends request to update ListAdapter
            adapter.notifyDataSetChanged();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            EditText txt_job_cart;
            TextView txt_service_name,txt_service_price;
            Button update;
            ImageView delete_btn;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

               //txt_job_cart = itemView.findViewById(R.id.txt_job_cart);
               // update =  itemView.findViewById(R.id.update);

                txt_service_name = itemView.findViewById(R.id.txt_service_name);
                txt_service_price = itemView.findViewById(R.id.txt_service_price);
                delete_btn = itemView.findViewById(R.id.delete_btn);
            }
        }
    }
}