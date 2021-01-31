package com.developer.mechanicroomvendor.AccountInfo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.developer.mechanicroomvendor.Model.GarageKYCModel;
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

public class MerchantActivity extends MyActivity implements View.OnClickListener {
    RelativeLayout adharcard_lyt,pancard_lyt,vendorprofile_lyt;
    String img_path_adharcard,img_path_pancard,img_path_vendorprofile;
    ImageView adharcard_close_btn,adharcard_image,pancard_image,pancard_close_btn,vendorprofile_image,vendorprofile_close_btn;
    ImageView adhar_pdf_close_btn,pan_pdf_close_btn;
    private static final int STORAGE_PERMISSION_CODE = 123;

    TextView txt_adharcard,txt_pancard,txt_vp,txt_aadhar_pdfname,txt_pancard_pdfname;

    String aadhar_path,pancard_path,profile_path,adhar_card_pdf_path,pancard_pdf_path;
    Button aadhar_btn,pancard_btn,adhar_pdf_btn,pancard_pdf_btn;

    private static final int BUFFER_SIZE = 1024 * 2;
    private static final String IMAGE_DIRECTORY = "/demonuts_upload_gallery";


    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
            requestStoragePermission();

        //adharcard_image = findViewById(R.id.adharcard_image);
        adharcard_close_btn = findViewById(R.id.adharcard_close_btn);
   /*     adharcard_lyt = findViewById(R.id.adharcard_lyt);
            adharcard_lyt.setOnClickListener(this);*/
            aadhar_btn = findViewById(R.id.aadhar_btn);
            aadhar_btn.setOnClickListener(this);

      //  pancard_image = findViewById(R.id.pancard_image);
        pancard_close_btn = findViewById(R.id.pancard_close_btn);
  /*          pancard_lyt= findViewById(R.id.pancard_lyt);
            pancard_lyt.setOnClickListener(this);*/
            pancard_btn = findViewById(R.id.pancard_btn);
            pancard_btn.setOnClickListener(this);

            adhar_pdf_btn = findViewById(R.id.adhar_pdf_btn);
            adhar_pdf_btn.setOnClickListener(this);

            pancard_pdf_btn = findViewById(R.id.pancard_pdf_btn);
            pancard_pdf_btn.setOnClickListener(this);

            txt_aadhar_pdfname = findViewById(R.id.txt_aadhar_pdfname);
            adhar_pdf_close_btn = findViewById(R.id.adhar_pdf_close_btn);
            adhar_pdf_close_btn.setOnClickListener(this);

            txt_pancard_pdfname = findViewById(R.id.txt_pancard_pdfname);
            pan_pdf_close_btn = findViewById(R.id.pan_pdf_close_btn);
            pan_pdf_close_btn.setOnClickListener(this);

            vendorprofile_image = findViewById(R.id.vendorprofile_image);
            vendorprofile_close_btn = findViewById(R.id.vendorprofile_close_btn);
            vendorprofile_lyt = findViewById(R.id.vendorprofile_lyt);
            vendorprofile_lyt.setOnClickListener(this);

            txt_adharcard = findViewById(R.id.txt_adharcard);
            txt_pancard = findViewById(R.id.txt_pancard);
            txt_vp = findViewById(R.id.txt_vp);
            getMerchantData();

            adharcard_close_btn.setOnClickListener(this);
            pancard_close_btn.setOnClickListener(this);
            vendorprofile_close_btn.setOnClickListener(this);
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

    private void getMerchantData() {

        final ProgressDialog uploading;
        uploading = ProgressDialog.show(this, "Loading", "Please wait...", false, false);

        String vendor_id = pref.getString("vendor_id","");
        RetrofitApiClient service = RetrofitClientInstance.getInstance().getApi();
        Call<KYCModel> reg_call = service.getGarageMerchantKYCUrl(vendor_id);

        reg_call.enqueue(new Callback<KYCModel>() {
            @Override
            public void onResponse(Call<KYCModel> call, Response<KYCModel> response) {
                Log.d("###", "onsuccess");
                if (response.body().isStatus()== true) {
                    uploading.dismiss();
                    ArrayList<GarageKYCModel> garageMerchantKYCList = response.body().getGarageMerchantKYC();
                    String Base_url = response.body().getBase_url();
                    setData(garageMerchantKYCList,Base_url);


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

    private void setData(ArrayList<GarageKYCModel> garageMerchantKYCList, String base_url) {

        if ( garageMerchantKYCList.get(0).getAadhaar_card() != null) {
            if (! garageMerchantKYCList.get(0).getAadhaar_card().isEmpty()) {
              //  adharcard_image.setVisibility(View.VISIBLE);
                adharcard_close_btn.setVisibility(View.VISIBLE);
                //txt_adharcard.setVisibility(View.GONE);
                aadhar_path =  garageMerchantKYCList.get(0).getAadhaar_card();
               String aadhar_name =  garageMerchantKYCList.get(0).getAadhaar_card();
                String filename = aadhar_name.substring(aadhar_name.lastIndexOf("/")+1);
                txt_adharcard.setText(""+filename);
//                String first_pic = base_url + garageMerchantKYCList.get(0).getAadhaar_card();
//                Glide.with(this).load(first_pic).error(R.drawable.mcroomround)
//                        .placeholder(R.drawable.mcroomround).into(adharcard_image);
            }
            else
            {
                //txt_adharcard.setVisibility(View.VISIBLE);
              //  adharcard_image.setVisibility(View.GONE);
                txt_adharcard.setText("");
                adharcard_close_btn.setVisibility(View.GONE);
            }
        }
        else
        {
            //txt_adharcard.setVisibility(View.VISIBLE);
            //adharcard_image.setVisibility(View.GONE);
            txt_adharcard.setText("");
            adharcard_close_btn.setVisibility(View.GONE);
        }

        if ( garageMerchantKYCList.get(0).getAadhaar_document() != null) {
            if (! garageMerchantKYCList.get(0).getAadhaar_document().isEmpty()) {

                adhar_pdf_close_btn.setVisibility(View.VISIBLE);
                adhar_card_pdf_path =  garageMerchantKYCList.get(0).getAadhaar_document();
                String aadhar_pdf =  garageMerchantKYCList.get(0).getAadhaar_document();
                String filename = aadhar_pdf.substring(aadhar_pdf.lastIndexOf("/")+1);
                txt_aadhar_pdfname.setText(""+filename);
//
            }
            else
            {
                txt_aadhar_pdfname.setText("");
                adhar_pdf_close_btn.setVisibility(View.GONE);
            }
        }
        else
        {

            txt_aadhar_pdfname.setText("");
            adhar_pdf_close_btn.setVisibility(View.GONE);
        }


        if ( garageMerchantKYCList.get(0).getPan_document() != null) {
            if (! garageMerchantKYCList.get(0).getPan_document().isEmpty()) {

                pan_pdf_close_btn.setVisibility(View.VISIBLE);
                pancard_pdf_path =  garageMerchantKYCList.get(0).getPan_document();
                String pancard_pdf =  garageMerchantKYCList.get(0).getPan_document();
                String filename = pancard_pdf.substring(pancard_pdf.lastIndexOf("/")+1);
                txt_pancard_pdfname.setText(""+filename);
//
            }
            else
            {
                txt_pancard_pdfname.setText("");
                pan_pdf_close_btn.setVisibility(View.GONE);
            }
        }
        else
        {

            txt_pancard_pdfname.setText("");
            pan_pdf_close_btn.setVisibility(View.GONE);
        }



        if ( garageMerchantKYCList.get(0).getPan_card() != null) {
            if (! garageMerchantKYCList.get(0).getPan_card().isEmpty()) {
              //  pancard_image.setVisibility(View.VISIBLE);
                pancard_close_btn.setVisibility(View.VISIBLE);
                //txt_pancard.setVisibility(View.GONE);
                pancard_path = garageMerchantKYCList.get(0).getPan_card();
                String pancard_name =  garageMerchantKYCList.get(0).getPan_card();
                String filename = pancard_name.substring(pancard_name.lastIndexOf("/")+1);
                txt_pancard.setText(""+filename);
//                String first_pic = base_url + garageMerchantKYCList.get(0).getPan_card();
//                Glide.with(this).load(first_pic).error(R.drawable.mcroomround)
//                        .placeholder(R.drawable.mcroomround).into(pancard_image);
            }
            else
            {
             //   txt_pancard.setVisibility(View.VISIBLE);
               // pancard_image.setVisibility(View.GONE);
                txt_pancard.setText("");
                pancard_close_btn.setVisibility(View.GONE);
            }
        }
        else
        {
           // txt_pancard.setVisibility(View.VISIBLE);
            //pancard_image.setVisibility(View.GONE);
            txt_pancard.setText("");
            pancard_close_btn.setVisibility(View.GONE);
        }

        if ( garageMerchantKYCList.get(0).getVendor_profile() != null) {
            if (! garageMerchantKYCList.get(0).getVendor_profile().isEmpty()) {
                vendorprofile_image.setVisibility(View.VISIBLE);
                vendorprofile_close_btn.setVisibility(View.VISIBLE);
                txt_vp.setVisibility(View.GONE);
                profile_path = garageMerchantKYCList.get(0).getVendor_profile();
                String first_pic = base_url + garageMerchantKYCList.get(0).getVendor_profile();
                Glide.with(this).load(first_pic).error(R.drawable.mcroomround)
                        .placeholder(R.drawable.mcroomround).into(vendorprofile_image);
            }
            else
            {
                txt_vp.setVisibility(View.VISIBLE);
                vendorprofile_image.setVisibility(View.GONE);
                vendorprofile_close_btn.setVisibility(View.GONE);
            }
        }
        else
        {
            txt_vp.setVisibility(View.VISIBLE);
            vendorprofile_image.setVisibility(View.GONE);
            vendorprofile_close_btn.setVisibility(View.GONE);
        }
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

    private void UploadImageToServer(String img_path_highlight,String photoType) {
        //String highlight_name

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
            bitmap.compress (Bitmap.CompressFormat.PNG, compressionRatio, new FileOutputStream(file));
        }
        catch (Throwable t) {
            Log.e("ERROR", "Error compressing file." + t.toString ());
            t.printStackTrace ();
        }
       // File file = new File(img_path_highlight);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", img_path_highlight, requestFile);
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
                    getMerchantData();
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
            case R.id.aadhar_btn:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.pancard_btn:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
                break;
            case R.id.vendorprofile_lyt:
                Intent ii = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(ii, 3);
                break;
            case R.id.adhar_pdf_btn:
                Intent pdfintent = new Intent(Intent.ACTION_GET_CONTENT);
                pdfintent.setType("application/pdf");
                startActivityForResult(pdfintent, 4);
                break;

            case R.id.pancard_pdf_btn:
                Intent pdf_intent = new Intent(Intent.ACTION_GET_CONTENT);
                pdf_intent.setType("application/pdf");
                startActivityForResult(pdf_intent, 5);
                break;
            case R.id.adharcard_close_btn:
                if (aadhar_path !=null) {
                    String photoType = "aadhaar_card";
                    DeleteShopActPhotoCopy(aadhar_path,photoType);
                }
                break;

            case R.id.pancard_close_btn:
                if (pancard_path !=null) {
                    String photoType = "pan_card";
                    DeleteShopActPhotoCopy(pancard_path,photoType);
                }
                break;
            case R.id.vendorprofile_close_btn:
                if (profile_path !=null) {
                    String photoType = "vendor_profile";
                    DeleteShopActPhotoCopy(profile_path,photoType);
                }
                break;

            case R.id.adhar_pdf_close_btn:
                if (adhar_card_pdf_path !=null) {
                    String photoType = "aadhaar_card";
                    DeleteShopActPhotoCopy(adhar_card_pdf_path,photoType);
                }
                break;

            case R.id.pan_pdf_close_btn:
                if (pancard_pdf_path !=null) {
                    String photoType = "pan_card";
                    DeleteShopActPhotoCopy(pancard_pdf_path,photoType);
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
                    // imageStoragePath = String.valueOf(data.getData());
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    img_path_adharcard= c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(img_path_adharcard));

                    //txt_highlight_img.setText("" + img_path_highlight);
                    String photoType = "aadhaar_card";
                    UploadImageToServer(img_path_adharcard,photoType);
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
                    img_path_pancard = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(img_path_pancard));

                    //txt_highlight_img.setText("" + img_path_highlight);
                    String photoType = "pan_card";
                    UploadImageToServer(img_path_pancard,photoType);
                }
            }
        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 3) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    // imageStoragePath = String.valueOf(data.getData());
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    img_path_vendorprofile = c.getString(columnIndex);
                    c.close();
                    Bitmap thumbnail = (BitmapFactory.decodeFile(img_path_vendorprofile));

                    //txt_highlight_img.setText("" + img_path_highlight);
                    String photoType = "vendor_profile";
                    UploadImageToServer(img_path_vendorprofile,photoType);
                }
            }
        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 4) {
                if (data.getData() != null) {
                    Uri uriselected = data.getData();

//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getContentResolver().query(selectedImage,  filePath, null, null, null);
//                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    cursor.moveToFirst();
//
//                     String imageStoragePath = cursor.getString(column_index);
//                     cursor.close();
//                     Log.d("###",imageStoragePath);
                    String photoType = "aadhaar_card";
                    UploadPdfToServer(uriselected,photoType);
                }
            }
        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 5) {
                if (data.getData() != null) {
                    Uri uriPdf = data.getData();

//                    String[] filePath = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getContentResolver().query(selectedPdf,  filePath, null, null, null);
//                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    cursor.moveToFirst();
//
//                    String imageStoragePath = cursor.getString(column_index);
//                    cursor.close();
//                    Log.d("###",imageStoragePath);
                    String photoType = "pan_card";
                    UploadPdfToServer(uriPdf,photoType);

                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void UploadPdfToServer(Uri uripath, String photoType) {

        String user_id = pref.getString("vendor_id","");
        String pdf_file_path = FilePath.getPath(this, uripath);
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
                if (serverResponse.isStatus()) {
                    //Handle Response
                    uploading.dismiss();
                    getMerchantData();
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



    private void DeleteShopActPhotoCopy(final String img_path, final String photoType)
    {
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

    private void deleteImage(String img_path, String photoType) {
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
                if (response.body() !=null) {
                    if (serverResponse.isStatus()) {
                        //Handle Response
                        uploading.dismiss();
                        getMerchantData();
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
                    Toast.makeText(MerchantActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
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
}