package com.developer.mechanicroomvendor.AccountInfo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developer.mechanicroomvendor.Model.GarageBusinessKYCModel;
import com.developer.mechanicroomvendor.Model.KYCModel;
import com.developer.mechanicroomvendor.PdfPath.FilePath;
import com.developer.mechanicroomvendor.R;
import com.developer.mechanicroomvendor.Retrofit.RetrofitApiClient;
import com.developer.mechanicroomvendor.Retrofit.RetrofitClientInstance;
import com.developer.mechanicroomvendor.Model.ServerResponse;
import com.developer.mechanicroomvendor.Utils.MyActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class BusinessActivity extends MyActivity implements View.OnClickListener {
    ImageView shop_lisence_image,shop_lisence_close_btn,shop_image,shop_photo_close_btn,shopact_pdf_close_btn;
    RelativeLayout shopatlisence_lyt,shop_photo_lyt;
    String img_path_shop_lisence,img_path_shop_photo;
    private static final int STORAGE_PERMISSION_CODE = 123;
    Button btn_bussiness_upload;
    AppCompatEditText edt_gst_no;
    TextView txt_shop_act,txt_shop;
    String shop_act_img_path,shop_img_path,shop_act_pdf_path;
     AlertDialog dialog;
     TextView txt_shopact_pdf;
     Button pdf_browse_btn;
        Button shop_act_btn;
    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
            requestStoragePermission();
        shop_lisence_close_btn = findViewById(R.id.shop_lisence_close_btn);
        //shop_lisence_image = findViewById(R.id.shop_lisence_image);
        /*shopatlisence_lyt = findViewById(R.id.shopatlisence_lyt);
        shopatlisence_lyt.setOnClickListener(this);*/
            shop_act_btn=findViewById(R.id.shop_act_btn);
            shop_act_btn.setOnClickListener(this);

        shop_image = findViewById(R.id.shop_image);
        shop_photo_close_btn = findViewById(R.id.shop_photo_close_btn);
            shop_photo_lyt = findViewById(R.id.shop_photo_lyt);
            shop_photo_lyt.setOnClickListener(this);


            shop_lisence_close_btn.setOnClickListener(this);
            shop_photo_close_btn.setOnClickListener(this);

            edt_gst_no = findViewById(R.id.edt_gst_no);
            edt_gst_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    edt_gst_no.setCursorVisible(true);

                }
            });

            btn_bussiness_upload= findViewById(R.id.btn_bussiness_upload);
            btn_bussiness_upload.setOnClickListener(this);

            txt_shop_act = findViewById(R.id.txt_shop_act);
            txt_shop = findViewById(R.id.txt_shop);

            txt_shopact_pdf = findViewById(R.id.txt_shopact_pdf);
            pdf_browse_btn = findViewById(R.id.pdf_browse_btn);
            pdf_browse_btn.setOnClickListener(this);

            shopact_pdf_close_btn = findViewById(R.id.shopact_pdf_close_btn);
            shopact_pdf_close_btn.setOnClickListener(this);

            getBussinessData();

        }
        else {
            connected = false;
            Toast.makeText(this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
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

    private void getBussinessData() {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String vendor_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<KYCModel> reg_call = service.getGarageBusinessKYCUrl(vendor_id);

        reg_call.enqueue(new Callback<KYCModel>() {
            @Override
            public void onResponse(Call<KYCModel> call, Response<KYCModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    ArrayList<GarageBusinessKYCModel> garageBusinessKYCKYCList = response.body().getGarageBusinessKYC();
                    String Base_url = response.body().getBase_url();
                    setData(garageBusinessKYCKYCList,Base_url);
                }
                else  if (response.body().isStatus() == false)
                {
                    uploading.dismiss();
                    String toastmsg = response.body().getMessage();
                    showToastmsg(toastmsg);
                }
                else
                {
                    uploading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<KYCModel> call, Throwable t) {
                uploading.dismiss();
            }
        });
    }

    private void setData(ArrayList<GarageBusinessKYCModel> garageBusinessKYCKYCList, String base_url) {

        if ( garageBusinessKYCKYCList.get(0).getShop_act() != null) {
            if (! garageBusinessKYCKYCList.get(0).getShop_act().isEmpty()) {
               // shop_lisence_image.setVisibility(View.VISIBLE);
                shop_lisence_close_btn.setVisibility(View.VISIBLE);
               // txt_shop_act.setVisibility(View.GONE);
                shop_act_img_path = garageBusinessKYCKYCList.get(0).getShop_act();
               String  shop_act_img = base_url + garageBusinessKYCKYCList.get(0).getShop_act();
                String filename = shop_act_img.substring(shop_act_img.lastIndexOf("/")+1);
                txt_shop_act.setText(""+filename);
              /*  Glide.with(this).load(shop_act_img).error(R.drawable.mcroomround)
                        .placeholder(R.drawable.mcroomround).into(shop_lisence_image);*/
            }
            else
            {
                ////txt_shop_act.setVisibility(View.VISIBLE);
               // shop_lisence_image.setVisibility(View.GONE);
                shop_lisence_close_btn.setVisibility(View.GONE);
                txt_shop_act.setText("");
            }
        }
        else
        {
           //// txt_shop_act.setVisibility(View.VISIBLE);
          //  shop_lisence_image.setVisibility(View.GONE);
            shop_lisence_close_btn.setVisibility(View.GONE);
            txt_shop_act.setText("");
        }


        if ( garageBusinessKYCKYCList.get(0).getShop_act_document() != null) {
            if (! garageBusinessKYCKYCList.get(0).getShop_act_document().isEmpty()) {
                // shop_lisence_image.setVisibility(View.VISIBLE);
                shopact_pdf_close_btn.setVisibility(View.VISIBLE);
                // txt_shop_act.setVisibility(View.GONE);
                shop_act_pdf_path = garageBusinessKYCKYCList.get(0).getShop_act_document();
                String  shop_act_pdf = base_url + garageBusinessKYCKYCList.get(0).getShop_act_document();
                String filename = shop_act_pdf.substring(shop_act_pdf.lastIndexOf("/")+1);
                txt_shopact_pdf.setText(""+filename);

            }
            else
            {

                shopact_pdf_close_btn.setVisibility(View.GONE);
                txt_shopact_pdf.setText("");
            }
        }
        else
        {

            shopact_pdf_close_btn.setVisibility(View.GONE);
            txt_shopact_pdf.setText("");
        }

        if ( garageBusinessKYCKYCList.get(0).getShop_photo() != null) {
            if (! garageBusinessKYCKYCList.get(0).getShop_photo().isEmpty()) {
                shop_image.setVisibility(View.VISIBLE);
                shop_photo_close_btn.setVisibility(View.VISIBLE);
                txt_shop.setVisibility(View.GONE);
                shop_img_path = garageBusinessKYCKYCList.get(0).getShop_photo();

                String first_pic = base_url + garageBusinessKYCKYCList.get(0).getShop_photo();
                Glide.with(this).load(first_pic).error(R.drawable.mcroomround)
                        .placeholder(R.drawable.mcroomround).into(shop_image);
            }
            else
            {
                txt_shop.setVisibility(View.VISIBLE);
                shop_image.setVisibility(View.GONE);
                shop_photo_close_btn.setVisibility(View.GONE);
            }
        }
        else
        {
            txt_shop.setVisibility(View.VISIBLE);
            shop_image.setVisibility(View.GONE);
            shop_photo_close_btn.setVisibility(View.GONE);
        }

        if (garageBusinessKYCKYCList.get(0).getGst_no() != null) {
            if (!garageBusinessKYCKYCList.get(0).getGst_no().isEmpty()) {
              //  if (!garageBusinessKYCKYCList.get(0).getGst_no().equals("0")) {
                    edt_gst_no.setText(garageBusinessKYCKYCList.get(0).getGst_no());
               // }
            }

        }

    }


    private void UploadImageToServer(String filepath,  String photoType) {
        //String highlight_name

        String  width = "200";
        String  height = "200";
        String user_id = pref.getString("vendor_id","");
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Uploading File", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        int compressionRatio = 2; //1 == originalImage, 2 = 50% compression, 4=25% compress
        File file = new File (filepath);


        try {
            Bitmap bitmap = BitmapFactory.decodeFile (file.getPath ());
            bitmap.compress (Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream(file));
        }
        catch (Throwable t) {
            Log.e("ERROR", "Error compressing file." + t.toString ());
            t.printStackTrace ();
        }


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", file.getName(), requestFile);
        //RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(selectedImage)), file);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody photo_Type = RequestBody.create(MediaType.parse("text/plain"), photoType);
        RequestBody p_width = RequestBody.create(MediaType.parse("text/plain"), width);
        RequestBody p_height = RequestBody.create(MediaType.parse("text/plain"), height);
       // RequestBody p_highlight_name = RequestBody.create(MediaType.parse("text/plain"), highlight_name);

        Call<ServerResponse> upload = service.uploadKYCFile(body, userid, photo_Type, p_width, p_height);

        upload.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, final Response<ServerResponse> response) {
                final ServerResponse serverResponse = response.body();
                if (serverResponse.isStatus()) {
                    //Handle Response

                    uploading.dismiss();
                    getBussinessData();
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



    private void showToastmsg(String toastmsg) {
        Toast.makeText(this, ""+toastmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_bussiness_upload:
                if (edt_gst_no.getText().toString().isEmpty()) {
                    edt_gst_no.setError("Enter GST Number");
                    edt_gst_no.requestFocus();
                } else if (edt_gst_no.getText().toString().length()!=15) {
                    edt_gst_no.setError("Enter Valid GST Number");
                    edt_gst_no.requestFocus();
                } else {

                    String gst_number= edt_gst_no.getText().toString();

                    UploadGSTServer(gst_number);
                }
                break;
            case R.id.pdf_browse_btn:
                Intent pdfintent = new Intent(Intent.ACTION_GET_CONTENT);
                pdfintent.setType("application/pdf");
                pdfintent = Intent.createChooser(pdfintent,"Choose a file");
                startActivityForResult(pdfintent, 3);
//                Intent pdfintent = new Intent();
//                pdfintent.setType("application/pdf");
//                pdfintent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(pdfintent, "Select PDF"), 3);
                break;
            case R.id.shop_act_btn:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.shop_photo_lyt:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
                break;

            case R.id.shop_lisence_close_btn:
                if (shop_act_img_path !=null) {
                    String photoType = "shop_act";
                    DeleteShopActPhotoCopy(shop_act_img_path,photoType);
                }
                break;

            case R.id.shop_photo_close_btn:
                if (shop_img_path !=null) {
                    String photoType = "shop_photo";
                    DeleteShopActPhotoCopy(shop_img_path,photoType);
                }
                break;
            case R.id.shopact_pdf_close_btn:
                if (shop_act_pdf_path !=null) {
                    String photoType = "shop_act";
                    DeleteShopActPhotoCopy(shop_act_pdf_path,photoType);
                }
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("result", "" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    img_path_shop_lisence = c.getString(columnIndex);
                    c.close();
                  //  Bitmap thumbnail = (BitmapFactory.decodeFile(img_path_shop_lisence));
                   // Log.d("###",img_path_shop_lisence);
                 //  String imageStoragePath = String.valueOf(data.getData());

                    String photoType = "shop_act";
                    UploadImageToServer(img_path_shop_lisence,photoType);

                }
            }
        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 2) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    // imageStoragePath = String.valueOf(data.getData());
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    img_path_shop_photo = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(img_path_shop_photo));

                    //txt_highlight_img.setText("" + img_path_highlight);
                    String photoType = "shop_photo";
                    UploadImageToServer(img_path_shop_photo,photoType);
                }
            }
        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 3) {
                if (resultCode == RESULT_OK && data.getData() != null) {
//                    Uri selectedImage = data.getData();
//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getContentResolver().query(selectedImage,  filePath, null, null, null);
//                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    cursor.moveToFirst();
//                    String imageStoragePath = cursor.getString(column_index);
//                    cursor.close();
/***************************************************************************************************************************/

//                    Uri uri = data.getData();
//                    String uriString = uri.toString();
//                    File myFile = new File(uriString);
//
//                    if (uriString.startsWith("content://")) {
//                        Cursor cursor = null;
//                        try {
//                            cursor = getContentResolver().query(uri, null, null, null, null);
//                            if (cursor != null && cursor.moveToFirst()) {
//                                imageStoragePath = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                                Toast.makeText(this, ""+uriString, Toast.LENGTH_SHORT).show();
//                            }
//                        } finally {
//                            cursor.close();
//                        }
//
//                    }  else if (uriString.startsWith("file://")) {
//                        imageStoragePath = myFile.getPath();
//                       // Toast.makeText(this, ""+imageStoragePath, Toast.LENGTH_SHORT).show();
//                    }
                    Uri uripath = data.getData();
//                    String imageStoragePath = null;
//                    try {
//                        InputStream inputStream = BusinessActivity.this.getContentResolver().openInputStream(path);
//                        byte[] pdfBytes = new byte[inputStream.available()];
//                        inputStream.read(pdfBytes);
//                         imageStoragePath= Base64.encodeToString(pdfBytes,Base64.DEFAULT);
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                     Log.d("%%%", imageStoragePath);
                   // Toast.makeText(this, ""+imageStoragePath, Toast.LENGTH_SHORT).show();
                    String photoType = "shop_act";
                    //String pdf_path = String.valueOf(uripath);

                    UploadPdfToServer(uripath,photoType);
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UploadPdfToServer(Uri uripath, String photoType) {

        String user_id = pref.getString("vendor_id","");
        String pdf_file_path = FilePath.getPath(this, uripath);
       // Toast.makeText(this, ""+pdf_file_path, Toast.LENGTH_SHORT).show();
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Uploading File", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        File file = new File(pdf_file_path);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("userfile", file.getName(), requestBody);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody photo_Type = RequestBody.create(MediaType.parse("text/plain"), photoType);

        Call<ServerResponse> uploadpdf = service.uploadPDFKYCFile(fileToUpload, userid, photo_Type);
        uploadpdf.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                final ServerResponse serverResponse = response.body();
                if (response.body()!=null) {
                    if (serverResponse.isStatus()) {
                        //Handle Response
                        uploading.dismiss();
                        getBussinessData();
                        String toastmsg = response.body().getMessage();
                        showUpdatemsg(toastmsg);
                    } else {
                        uploading.dismiss();
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    }
                }
                else
                {
                    Toast.makeText(BusinessActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                uploading.dismiss();
            }
        });

    }



    private void DeleteShopActPhotoCopy(final String img_path, final String photoType) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.delete_dialog, null);
        final TextView txt_dialog = alertLayout.findViewById(R.id.txt_dialog);
        final Button btn_yes = (Button) alertLayout.findViewById(R.id.btn_delete);
        final Button btn_no = (Button) alertLayout.findViewById(R.id.btn_cancel);

        txt_dialog.setText("Do you Really want to delete?");

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);


        alert.setView(alertLayout);
        alert.setCancelable(false);
          dialog = alert.create();
        //  dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation_2;
        dialog.setCancelable(false);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                    deleteImage(img_path,photoType);


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

    private void deleteImage(String img_path,String photoType) {
        String vendor_id = pref.getString("vendor_id","");
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Uploading File", "Please wait...", false, false);

        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
//        File file = new File(img_path);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", img_path, requestFile);
//        //RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(selectedImage)), file);
//        RequestBody vendorId = RequestBody.create(MediaType.parse("text/plain"), vendor_id);
//        RequestBody photo_Type = RequestBody.create(MediaType.parse("text/plain"), photoType);

        Call<ServerResponse> upload = service.deleteDocumentPhotoFile(img_path, vendor_id, photoType);

        upload.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, final Response<ServerResponse> response) {
                final ServerResponse serverResponse = response.body();
                if (response.body()!=null) {
                    if (serverResponse.isStatus()) {
                        //Handle Response
                        uploading.dismiss();
                        getBussinessData();
                        String toastmsg = response.body().getMessage();
                        showUpdatemsg(toastmsg);
                    } else {
                        uploading.dismiss();
                        String toastmsg = response.body().getMessage();
                        showToastmsg(toastmsg);
                    }
                }
                else {
                    Toast.makeText(BusinessActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
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

    private void UploadGSTServer(String gst_number) {
        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Uploading File", "Please wait...", false, false);

        String user_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<ServerResponse> business_gst_call = service.getBussinessGSTUrl(user_id,gst_number);
        business_gst_call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                final ServerResponse serverResponse = response.body();
                if (serverResponse.isStatus()) {
                    //Handle Response
                    edt_gst_no.setCursorVisible(false);
                    uploading.dismiss();
                    getBussinessData();
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
                uploading.dismiss();
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
}