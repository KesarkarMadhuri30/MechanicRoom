package com.developer.mechanicrooms.Retrofit;

import com.developer.mechanicrooms.BrandModel;
import com.developer.mechanicrooms.Model.BikeModel;
import com.developer.mechanicrooms.Model.BookingModel;
import com.developer.mechanicrooms.Model.CityModel;
import com.developer.mechanicrooms.Model.GalleryModel;
import com.developer.mechanicrooms.Model.GarageDetailsModel;
import com.developer.mechanicrooms.Model.GarageModel;
import com.developer.mechanicrooms.Model.HistoryModel;
import com.developer.mechanicrooms.Model.RatingModel;
import com.developer.mechanicrooms.Model.RegProfileModel;
import com.developer.mechanicrooms.Model.ReviewModel;
import com.developer.mechanicrooms.Model.ServerResponse;
import com.developer.mechanicrooms.Model.ServicingLocationResponse;
import com.developer.mechanicrooms.RegModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<RegProfileModel> postUserProfile(@Field("phone") String phone,
                                          @Field("name") String name,
                                          @Field("email") String email,
                                          @Field("user_type") String user_type
    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @PUT("customer_contactno")
    @FormUrlEncoded
    Call<ServerResponse> putUpdateMobile(@Field("phone") String phone,
                                         @Field("customer_id")String customer_id
    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("check_customerOTP")
    @FormUrlEncoded
    Call<ServerResponse> postcheckOtp(@Field("phone") String phone,
                               @Field("otp") String otp,
                               @Field("customer_id")String customer_id
    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("customerInfo/{phone}")
    Call<RegProfileModel> getAccountInfoUrl(@Path("phone") String phone);

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("resentOTP")
    @FormUrlEncoded
    Call<RegModel> postResendOtpUser(@Field("phone") String phone);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("city")
    Call<CityModel> getCityUrl();

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("brand")
    Call<BrandModel> getBrandUrl();

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("brandModel/{id}")
    Call<BikeModel> getBrandModelUrl(@Path("id") String id);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageInfo/{phone}")
    Call<GarageDetailsModel> getGarageInfoUrl(@Path("phone") String phone);




    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garages/{city}/{pincode}/{area}")
    Call<GarageModel> getGaragesUrl(@Path("city") String city,
                                    @Path("pincode") String pincode,
                                    @Path("area") String area
                                  );


    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageReviews/{vendor_id}")
    Call<ReviewModel> getGarageReviews(@Path("vendor_id") String vendor_id);

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageRating/{vendor_id}")
    Call<RatingModel> getGarageRating(@Path("vendor_id") String vendor_id);

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("customer_rating")
    @FormUrlEncoded
    Call<RatingModel> postRatingUser(@Field("customerID") String customerID,
                                     @Field("rating") String rating,
                                     @Field("vendorID") String vendorID);

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("customer_reviews")
    @FormUrlEncoded
    Call<ReviewModel> postReviewsUser(@Field("customerID") String customerID,
                                     @Field("vendorID") String vendorID,
                                      @Field("reviews") String reviews);

/*customer_id,customer_name,customer_phone,garage_id,
                garage_name,garage_phone,vehicle_type,v_service,b_date,b_time,pick,addi_req*/

    @Headers("X-API-KEY: VEHICLE@2020")
    @POST("customer_booking")
    @FormUrlEncoded
    Call<BookingModel> postServiceBooking(@Field("customer_id") String customer_id,
                                       @Field("customer_name") String customer_name,
                                       @Field("customer_phone") String customer_phone,
                                          @Field("garage_id") String garage_id,
                                          @Field("garage_name") String garage_name,
                                          @Field("garage_phone") String garage_phone,
                                          @Field("vehical_type") String vehicle_type,
                                          @Field("service_requirement") String service_requirement,
                                          @Field("pick_date") String pick_date,
                                          @Field("pick_time") String pick_time,
                                          @Field("pick_up_drop") String pick_up_drop,
                                          @Field("additional_requirements") String additional_requirements
                                          );

    @Headers("X-API-KEY: VEHICLE@2020")
    @PUT("servicingLocation")
    @FormUrlEncoded
    Call<ServicingLocationResponse> putServicingLocation(@Field("customer_id") String customer_id,
                                                            @Field("address") String address,
                                                            @Field("area")String area,
                                                         @Field("pincode")String pincode,
                                                         @Field("city") String city,
                                                         @Field("brand_id")String brand_id,
                                                         @Field("model_id")String model_id

    );

    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("garageGallery/{vendor_id}")
    Call<GalleryModel> getGalleryInfoUrl(@Path("vendor_id") String vendor_id);



    @Headers("X-API-KEY: VEHICLE@2020")
    @GET("customer_history/{customer_id}")
    Call<HistoryModel> getCustomerHistory(@Path("customer_id") String customer_id);
}
