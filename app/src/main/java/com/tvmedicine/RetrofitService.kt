package com.tvmedicine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**Implementation of api methods and their parameters. Any method expects the name of the api method to be passed in the form method_name.php*/
interface RetrofitServices {
    @GET("api/{method}")
    /**Auth methods. Parameters: phone_number, password*/
    fun auth(
            @Path("method") method_name: String?,
            @Query("phone_number") phone_number: String?,
            @Query("password") password: String
    ): Call<List<AuthModel?>?>?
    @GET("api/{method}")
            /**Get all patient methods. Parameters: None*/
    fun getAllPatient(
            @Path("method") method_name: String?
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
            /**Get patient by ID methods. Parameters: id*/
    fun getPatientFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int?
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
            /**Get all treatment methods. Parameters: None*/
    fun getAllTreatment(
            @Path("method") method_name: String?,
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get treatment by ID methods. Parameters: id*/
    fun getTreatmentFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get treatment for User by phone number. Parameters: phone_number*/
    fun getTreatmentByUser(
            @Path("method") method_name: String?,
            @Query("phone_number") phone_number: String?
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get all doctors methods. Parameters: None*/
    fun getAllDoctor(
            @Path("method") method_name: String?,
    ): Call<List<DoctorModel?>?>?
    @GET("api/{method}")
            /**Get doctor by ID methods. Parameters: id*/
    fun getDoctorFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int?
    ): Call<List<DoctorModel?>?>?
    @GET("api/{method}")
            /**Get symptoms methods. Parameters: none*/
    fun getAllSymptoms(
            @Path("method") method_name: String?,
    ): Call<List<SymptomsModel?>?>?
    @GET("api/{method}")
            /**Add new treatment method. Parameters: phone_number,start_date,symptoms_id,sound_server_link_id*/
    fun addTreatment(
            @Path("method") method_name: String?,
            @Query("phone_number") phone_number: String?,
            @Query("start_date") start_date:String?,
            @Query("symptoms_id") symptoms_id:Int?,
            @Query("sound_server_link_id") sound_server_link_id:Int?
    ): Call<List<AuthModel?>?>?
}