package com.tvmedicine

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**Implementation of api methods and their parameters. Any method expects the name of the api method to be passed in the form method_name.php*/
interface RetrofitServices {
    @GET("api/{method}")
    /**Auth methods.
     * @param method_name it method name which need use. Type - [String]
     * @param phone_number it phone number which use like login. Type - [String]
     * @param password it password for auth. Type - [String]
     *@return [AuthModel] */
    fun auth(
            @Path("method") method_name: String?,
            @Query("phone_number") phone_number: String?,
            @Query("password") password: String
    ): Call<List<AuthModel?>?>?
    @GET("api/{method}")
            /**Get all patient methods.
             * @return [PatientModel]*/
    fun getAllPatient(
            @Path("method") method_name: String?
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
            /**Get patient by ID methods
             * @param id Patient id information about which need get. Type - [String]
             * @return [PatientModel]*/
    fun getPatientFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int?
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
            /**Get all treatment methods.
             * @return [TreatmentModel]*/
    fun getAllTreatment(
            @Path("method") method_name: String?,
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get treatment by ID methods.
             *  @param id Treatment id information about which need get. Type - [String]
             *  @return [TreatmentModel]*/
    fun getTreatmentFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get treatment for User by phone number.
             * @param phone_number it Param use for get Treatment for user
             * @return [TreatmentModel]*/
    fun getTreatmentByUser(
            @Path("method") method_name: String?,
            @Query("phone_number") phone_number: String?
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get all doctors methods.
             * @return [DoctorModel]*/
    fun getAllDoctor(
            @Path("method") method_name: String?,
    ): Call<List<DoctorModel?>?>?
    @GET("api/{method}")
            /**Get doctor by ID methods.
             * @param id Get Doctor info y ID
             * @return [DoctorModel]*/
    fun getDoctorFromId(
            @Path("method") method_name: String?,
            @Query("id") id:Int?
    ): Call<List<DoctorModel?>?>?
    @GET("api/{method}")
            /**Get symptoms methods.
             * @return [SymptomsModel]*/
    fun getAllSymptoms(
            @Path("method") method_name: String?,
    ): Call<List<SymptomsModel?>?>?
    @GET("api/{method}")
            /**Add new treatment method. P
             * @param phone_number use for get user ID
             * @param start_date Date of start treatment
             * @param symptoms_id User choice him symptoms
             * @param sound_server_link_id User record him sound*
             * @return [AuthModel]*/
    fun addTreatment(
            @Path("method") method_name: String?,
            @Query("phone_number") phone_number: String?,
            @Query("start_date") start_date:String?,
            @Query("symptoms_id") symptoms_id:Int?,
            @Query("sound_server_link_id") sound_server_link_id:Int?
    ): Call<List<AuthModel?>?>?
    /**Add conclusion methods.
     * @param treat_id Treatment ID where need add conclusion
     * @param conc_text Conclusion text for treatment
     * @param phone_number Phone number for get patient ID
     * @return [AuthModel] */
    @GET("api/{method}")
    fun addConclusion(
            @Path("method") method_name: String?,
            @Query("treat_id") treat_id: Int?,
            @Query("conc_text") conc_text: String?,
            @Query("phone_number") phone_number: String?
    ): Call<List<AuthModel?>?>?
    /**Delete treatment methods.
     * @param treat_id Treatment ID which need delete
     * @return [AuthModel]*/
    @GET("api/{method}")
    fun deleteTreatment(
            @Path("method") method_name: String?,
            @Query("treat_id") treat_id: Int?,
            @Query("phone_number") phone_number: String?
    ): Call<List<AuthModel?>?>?
    @GET("api/{method}")
            /**Get symptoms methods.
             * @return [SymptomsModel]*/
    fun getSymptomsForUser(
            @Path("method") method_name: String?,
            @Query("treat_id") treat_id: Int?
    ): Call<List<SymptomsModel?>?>?
}