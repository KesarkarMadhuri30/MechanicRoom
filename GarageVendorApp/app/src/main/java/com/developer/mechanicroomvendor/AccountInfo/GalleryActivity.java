package com.developer.mechanicroomvendor.AccountInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.mechanicroomvendor.Model.GalleryModel;
import com.developer.mechanicroomvendor.Model.GarageGalleryModel;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Model.ServerResponse;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class GalleryActivity extends MyActivity implements View.OnClickListener {
    AppCompatEditText edt_highlight_name;
    TextView txt_highlight_img;
    Button btn_gallery_upload,browse_btn;
    String img_path_highlight;
    LinearLayout gallery_img_lyt;
    RecyclerView highlight_re_view;
    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
            requestStoragePermission();
            edt_highlight_name = findViewById(R.id.edt_highlight_name);
        txt_highlight_img = findViewById(R.id.txt_highlight_img);

            gallery_img_lyt = findViewById(R.id.gallery_img_lyt);

        highlight_re_view = findViewById(R.id.highlight_re_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        highlight_re_view.setLayoutManager(layoutManager);
        highlight_re_view.setNestedScrollingEnabled(false);

        getGalleryData();

         browse_btn = findViewById(R.id.browse_btn);
        browse_btn.setOnClickListener(this);

        btn_gallery_upload = findViewById(R.id.btn_gallery_upload);
        btn_gallery_upload.setOnClickListener(this);
        }
        else {
            connected = false;
            Toast.makeText(GalleryActivity.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getGalleryData() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String vendor_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<GalleryModel> reg_call = service.getGalleryInfoUrl(vendor_id);

        reg_call.enqueue(new Callback<GalleryModel>() {
            @Override
            public void onResponse(Call<GalleryModel> call, Response<GalleryModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    ArrayList<GarageGalleryModel> garageGalleryList = response.body().getGarageGalleryModel();
                    String Base_url = response.body().getBase_url();
                    gallery_img_lyt.setVisibility(View.VISIBLE);
                    setData(garageGalleryList,Base_url);


                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    gallery_img_lyt.setVisibility(View.GONE);
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else
                {
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<GalleryModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

//        if (requestCode==101)
//        {
//            Log.d("###",""+grantResults.length+"\t\t\t"+grantResults[0]);
//        }
    }

    private void setData(ArrayList<GarageGalleryModel> garageGalleryList,String Base_url) {
        edt_highlight_name.setText("");
        txt_highlight_img.setText("");
        AdapterHighlight adapterHighlight = new AdapterHighlight(getApplicationContext(),garageGalleryList,Base_url);
        highlight_re_view.setAdapter(adapterHighlight);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.browse_btn:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 8);
                break;
            case R.id.btn_gallery_upload:
//                if (edt_highlight_name.getText().toString().isEmpty()) {
//                    edt_highlight_name.setError("Enter Highlight Name");
//                    edt_highlight_name.requestFocus();
//                } else

                    if (txt_highlight_img.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select Highlight image", Toast.LENGTH_SHORT).show();
                } else {
                        String highlight_name;
                    if (edt_highlight_name.getText().toString().isEmpty())
                    {
                        highlight_name = "No Name";
                    }
                    else {
                         highlight_name = edt_highlight_name.getText().toString();
                    }
                    UploadImageToServer(img_path_highlight,highlight_name);
                }
                break;

        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }



        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void UploadImageToServer(String img_path_highlight, String highlight_name) {
        String photoType = "vendor_images";
        String  width = "200";
        String  height = "200";
        String user_id = pref.getString("vendor_id","");
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Uploading File", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        int compressionRatio = 2; //1 == originalImage, 2 = 50% compression, 4=25% compress
        File file = new File (img_path_highlight);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile (file.getPath ());
            bitmap.compress (Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
        }
        catch (Throwable t) {
            Log.e("ERROR", "Error compressing file." + t.toString ());
            t.printStackTrace ();
        }
        //File file = new File(img_path_highlight);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", img_path_highlight, requestFile);
        //RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(selectedImage)), file);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody photo_Type = RequestBody.create(MediaType.parse("text/plain"), photoType);
        RequestBody p_width = RequestBody.create(MediaType.parse("text/plain"), width);
        RequestBody p_height = RequestBody.create(MediaType.parse("text/plain"), height);
        RequestBody p_highlight_name = RequestBody.create(MediaType.parse("text/plain"), highlight_name);

        Call<ServerResponse> upload = service.uploadFile(body, userid, photo_Type, p_width, p_height,p_highlight_name);

        upload.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, final Response<ServerResponse> response) {
                final ServerResponse serverResponse = response.body();
                if (serverResponse.isStatus()) {
                    //Handle Response
                    uploading.dismiss();
                   getGalleryData();
                    String toastmsg = response.body().getMessage();
                    showUpdatemsg(toastmsg);
                } else {
                    uploading.dismiss();
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    uploading.dismiss();
                    Toast.makeText(getApplicationContext(), "Unable To Upload\nError: Socket Time out. Please try again", Toast.LENGTH_LONG).show();
                }
                t.printStackTrace();
            }
        });
    }
    private void showUpdatemsg(String toastmsg) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        TextView msg =popupView.findViewById(R.id.s_msg);
        msg.setText(""+toastmsg);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(popupView);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();



        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
        dialog.show();
        handler.postDelayed(runnable, 2000);
    }

    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 8) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    // imageStoragePath = String.valueOf(data.getData());
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    img_path_highlight = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(img_path_highlight));

                    txt_highlight_img.setText("" + img_path_highlight);

                }
            }
        }
    }


    private class AdapterHighlight extends RecyclerView.Adapter<AdapterHighlight.ViewHolder> {
        Context context;
        ArrayList<GarageGalleryModel> galleryList = new ArrayList<>();
        String Base_url;

        public AdapterHighlight(Context context, ArrayList<GarageGalleryModel> galleryList, String base_url) {
            this.context = context;
            this.galleryList = galleryList;
            this.Base_url = base_url;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View rootview = LayoutInflater.from(context).inflate(R.layout.highlight_listitem, parent, false);
            return new ViewHolder(rootview);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
            holder.txt_highlight_name.setText("" + galleryList.get(i).getHighlight_name());
            String image_url = Base_url + galleryList.get(i).getGarage_pic();
            Glide.with(context)
                    .load(image_url)
                    .error(R.drawable.mcroomround)
                    .placeholder(R.drawable.mcroomround)
                    .into(holder.highlight_img);
            holder.txt_highlight_name.setSelected(true);

            holder.highlight_close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(100);

                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.delete_dialog, null);
                    final TextView txt_dialog = alertLayout.findViewById(R.id.txt_dialog);
                    final Button btn_yes = (Button) alertLayout.findViewById(R.id.btn_delete);
                    final Button btn_no = (Button) alertLayout.findViewById(R.id.btn_cancel);

                    txt_dialog.setText("Do you Really want to delete?");

                    final AlertDialog.Builder alert = new AlertDialog.Builder(GalleryActivity.this);


                    alert.setView(alertLayout);
                    alert.setCancelable(false);
                    final AlertDialog dialog = alert.create();
                    //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
                    dialog.setCancelable(false);

                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            String image_id = galleryList.get(i).getId();
                           deleteImage(image_id);

                        }
                    });

                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return galleryList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_highlight_name;
            ImageView highlight_img,highlight_close_btn;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_highlight_name = itemView.findViewById(R.id.txt_highlight_name);
                highlight_img = itemView.findViewById(R.id.highlight_img);
                highlight_close_btn = itemView.findViewById(R.id.highlight_close_btn);
            }
        }
    }

    private void deleteImage(String id) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<ServerResponse> reg_call = service.deleteGalleryImage(id);

        reg_call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Log.d("###", "onsuccess");
                if (response.body() != null) {
                    if (response.body().isStatus() == true) {
                        uploading.dismiss();
                        String toastmsg = response.body().getMessage();
                        showUpdatemsg(toastmsg);
                        getGalleryData();


                    } else if (response.body().isStatus() == false) {
                        uploading.dismiss();
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    } else {
                        uploading.dismiss();
                    }
                }
                else
                {
                    uploading.dismiss();
                    Toast.makeText(GalleryActivity.this, "Some problems occurred, please try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }
}