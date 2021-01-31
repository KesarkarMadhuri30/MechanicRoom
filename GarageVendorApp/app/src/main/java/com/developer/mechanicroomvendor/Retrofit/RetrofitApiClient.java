package com.developer.mechanicroomvendor.Retrofit;

import com.developer.mechanicroomvendor.Model.BookingModel;
import com.developer.mechanicroomvendor.Model.CityModel;
import com.developer.mechanicroomvendor.Model.GalleryModel;
import com.developer.mechanicroomvendor.Model.GarageModel;
import com.developer.mechanicroomvendor.Model.GarageUpdateModel;
import com.developer.mechanicroomvendor.Model.KYCModel;
import com.developer.mechanicroomvendor.Model.RatingModel;
import com.developer.mechanicroomvendor.Model.RegModel;
import com.developer.mechanicroomvendor.Model.UserModel;
import com.developer.mechanicroomvendor.Model.ReviewModel;
import com.developer.mechanicroomvendor.Model.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitApiClient {
    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("registration")
    @FormUrlEncoded
    Call<RegModel> postRegUser(@Field("phone") String phone,
                 @Field("user_type") String user_type
    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("checkOTP")
    @FormUrlEncoded
    Call<RegModel> postOtpUser(@Field("phone") String phone,
                               @Field("otp") String otp
    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("customerInfo")
    @FormUrlEncoded
    Call<UserModel> postUserProfile(@Field("phone") String phone,
                                    @Field("name") String name,
                                    @Field("email") String email,
                                    @Field("user_type") String user_type
    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("resentOTP")
    @FormUrlEncoded
    Call<RegModel> postResendOtpUser(@Field("phone") String phone);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageInfo/{phone}")
    Call<GarageModel> getAccountInfoUrl(@Path("phone") String phone);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageGallery/{vendor_id}")
    Call<GalleryModel> getGalleryInfoUrl(@Path("vendor_id") String vendor_id);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("city")
    Call<CityModel> getCityUrl();


    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("garageInfo")
    @FormUrlEncoded
    Call<GarageUpdateModel> postOwnerProfileUpdate(@Field("user_id") String user_id,
                                             @Field("phone") String phone,
                                             @Field("name") String name,
                                             @Field("email") String email

    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("garageInfo")
    @FormUrlEncoded
    Call<GarageUpdateModel> postGarageProfileUpdate(@Field("user_id") String user_id,
                                             @Field("garage_name") String garage_name,
                                             @Field("contact_no")String contact_no,
                                             @Field("services") String services,
                                              @Field("pick_drop") String pick_drop

    );
    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("garageInfo")
    @FormUrlEncoded
    Call<GarageUpdateModel> postGarageTimeUpdate(@Field("user_id") String user_id,
                                              @Field("open_at") String open_at,
                                              @Field("close_at")String close_at,
                                              @Field("week_off") String week_off

    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("garageInfo")
    @FormUrlEncoded
    Call<GarageUpdateModel> postGarageLocationUpdate(@Field("user_id") String user_id,
                                                 @Field("address") String address,
                                                 @Field("area")String area,
                                                 @Field("pincode") String pincode,
                                                     @Field("city") String city

    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @Multipart
    @POST("galleryImage")
    Call<ServerResponse> uploadFile(
            @Part MultipartBody.Part file,
            @Part("user_id") RequestBody userid,
            @Part("photoType") RequestBody photoType,
            @Part("width") RequestBody width,
            @Part("height") RequestBody height,
            @Part("highlightName") RequestBody highlightName);

    @Headers("X-API-KEY: VEHICLE@2020")
    @Multipart
    @POST("galleryImage")
    Call<ServerResponse> uploadKYCFile(
            @Part MultipartBody.Part file,
            @Part("user_id") RequestBody userid,
            @Part("photoType") RequestBody photoType,
            @Part("width") RequestBody width,
            @Part("height") RequestBody height
         );

    @Headers("X-API-KEY: VEHICLE@2020")
    @Multipart
    @POST("document")
    Call<ServerResponse> uploadPDFKYCFile(
            @Part MultipartBody.Part file,
            @Part("user_id") RequestBody userid,
            @Part("photoType") RequestBody photoType
    );


    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("garageGST")
    @FormUrlEncoded
    Call<ServerResponse> getBussinessGSTUrl(@Field("user_id") String user_id,
                                            @Field("gst_no") String gst_number

    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageMerchantKYC/{vendor_id}")
    Call<KYCModel> getGarageMerchantKYCUrl(@Path("vendor_id") String vendor_id);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageBusinessKYC/{vendor_id}")
    Call<KYCModel> getGarageBusinessKYCUrl(@Path("vendor_id") String vendor_id);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageReviews/{vendor_id}")
    Call<ReviewModel> getGarageReviews(@Path("vendor_id") String vendor_id);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageRating/{vendor_id}")
    Call<RatingModel> getGarageRating(@Path("vendor_id") String vendor_id);


    @Headers("X-API-KEY: VEHICLE@2020")
   // @GET("vendorGalleryImage/{id}")
    //@POST("documentPhotoCopy")
    @DELETE("vendorGalleryImage/{id}")
    Call<ServerResponse> deleteGalleryImage(@Path("id") String id

    );


//    @Headers("X-API-KEY: VEHICLE@2020")
//    @Multipart
//    @PUT("documentPhotoCopy")
//    Call<ServerResponse> deleteDocumentPhotoFile(
//            @Part MultipartBody.Part file,
//            @Part("vendor_id") RequestBody userid,
//            @Part("photoType") RequestBody photoType
//    );


    @Headers("X-API-KEY: VEHICLE@2020")
    @PUT("documentPhotoCopy")
    @FormUrlEncoded
    Call<ServerResponse> deleteDocumentPhotoFile(@Field("userfile") String userfile,
                                                    @Field("vendor_id") String vendor_id,
                                                    @Field("photoType")String photoType

    );


    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("booking/{vendor_id}")
    Call<BookingModel> getGarageAppointment(@Path("vendor_id") String vendor_id);


    @Headers("X-API-KEY: VEHICLE@2020")
    @PUT("booking_confirmed")
    @FormUrlEncoded
    Call<ServerResponse> putBookingStatus(@Field("booking_no") String booking_no,
                                                         @Field("confirmed")String confirmed
    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @PUT("service_payment")
    @FormUrlEncoded
    Call<ServerResponse> putServicePayment(@Field("booking_no") String booking_no,
                                          @Field("total_payment")String total_payment
    );
}
