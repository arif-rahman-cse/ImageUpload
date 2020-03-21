package com.smarifrahman.imageupload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {


    /*
    @FormUrlEncoded
    @POST("user-config")
    Call<UpdateUserResponse> updateUserInfo(
            @Header("Authorization") String autorization,
            @Field("language_id") int languageId,
            @Field("country_id") int countryId,
            @Field("state_id") int stateId,
            @Field("zabia_status") String zabihaStatus,
            @Field("address") String address,
            @Field("image") String image
    );

     */


    /*
    @Multipart
    @POST("user-config")
    Call<UpdateUserResponse> updateProfileImage(
            @Header("Authorization") String autorization,
            @Part("language_id") RequestBody languageId,
            @Part ("country_id") RequestBody countryId,
            @Part ("state_id") RequestBody stateId,
            @Part ("zabia_status") RequestBody zabihaStatus,
            @Part ("address") RequestBody address,
            @Part MultipartBody.Part image
    );
     */


    @Multipart
    @POST("contact-form-7/v1/contact-forms/173/feedback")
    Call<ImageResponse> postBazar(
            @Field("product-name") String productName,
            @Field("p-quantity") String quantity,
            @Field("price") String price,
            @Field("seller") String seller,
            @Field("phone") String phone,
            @Field("product") String image
            //@Part("product-img") MultipartBody.Part product_img

    );





}
