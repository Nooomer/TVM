package com.tvmedicine

import com.tvmedicine.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**Implementation of api methods and their parameters. Any method expects the name of the api method to be passed in the form method_name.php*/
interface API {
    @GET("api/{method}")
    /**Auth methods.
     * @param methodName it method name which need use. Type - [String]
     * @param phoneNumber it phone number which use like login. Type - [String]
     * @param password it password for auth. Type - [String]
     *@return [AuthModel] */
    fun auth(
        @Path("method") methodName: String?,
        @Query("phone_number") phoneNumber: String?,
        @Query("password") password: String
    ): Call<List<AuthModel?>?>?
    @GET("api/{method}")
            /**Get all patient methods.
             * @return [PatientModel]*/
    fun getAllPatient(
            @Path("method") methodName: String?
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
            /**Get patient by ID methods
             * @param id Patient id information about which need get. Type - [String]
             * @return [PatientModel]*/
    fun getPatientFromId(
        @Path("method") methodName: String?,
        @Query("id") id:Int?
    ): Call<List<PatientModel?>?>?
    @GET("api/{method}")
            /**Get all treatment methods.
             * @return [TreatmentModel]*/
    fun getAllTreatment(
        @Path("method") methodName: String?,
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get treatment by ID methods.
             *  @param id Treatment id information about which need get. Type - [String]
             *  @return [TreatmentModel]*/
    fun getTreatmentFromId(
        @Path("method") methodName: String?,
        @Query("id") id:Int
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get treatment for User by phone number.
             * @param phoneNumber it Param use for get Treatment for user
             * @return [TreatmentModel]*/
    fun getTreatmentByUser(
        @Path("method") methodName: String?,
        @Query("phone_number") phoneNumber: String?
    ): Call<List<TreatmentModel?>?>?
    @GET("api/{method}")
            /**Get all doctors methods.
             * @return [DoctorModel]*/
    fun getAllDoctor(
        @Path("method") methodName: String?,
    ): Call<List<DoctorModel?>?>?
    @GET("api/{method}")
            /**Get doctor by ID methods.
             * @param id Get Doctor info y ID
             * @return [DoctorModel]*/
    fun getDoctorFromId(
        @Path("method") methodName: String?,
        @Query("id") id:Int?
    ): Call<List<DoctorModel?>?>?
    @GET("api/{method}")
            /**Get symptoms methods.
             * @return [SymptomsModel]*/
    fun getAllSymptoms(
        @Path("method") methodName: String?,
    ): Call<List<SymptomsModel?>?>?
    @GET("api/{method}")
            /**Add new treatment method.
             * @param phoneNumber use for get user ID
             * @param startDate Date of start treatment
             * @param symptomsId User choice him symptoms
             * @param soundServerLinkId User record him sound*
             * @return [AuthModel]*/
    fun addTreatment(
        @Path("method") methodName: String?,
        @Query("phone_number") phoneNumber: String?,
        @Query("start_date") startDate:String?,
        @Query("symptoms_id") symptomsId:Int?,
        @Query("sound_server_link_id") soundServerLinkId:Int?
    ): Call<List<AuthModel?>?>?
    /**Add conclusion methods.
     * @param treatId Treatment ID where need add conclusion
     * @param concText Conclusion text for treatment
     * @param phoneNumber Phone number for get patient ID
     * @return [AuthModel] */
    @GET("api/{method}")
    fun addConclusion(
        @Path("method") methodName: String?,
        @Query("treat_id") treatId: Int?,
        @Query("conc_text") concText: String?,
        @Query("phone_number") phoneNumber: String?
    ): Call<List<AuthModel?>?>?
    /**Delete treatment methods.
     * @param treatId Treatment ID which need delete
     * @return [AuthModel]*/
    @GET("api/{method}")
    fun deleteTreatment(
        @Path("method") methodName: String?,
        @Query("treat_id") treatId: Int?,
        @Query("phone_number") phoneNumber: String?
    ): Call<List<AuthModel?>?>?
    @GET("api/{method}")
            /**Get symptoms methods.
             * @return [SymptomsModel]*/
    fun getSymptomsForUser(
        @Path("method") methodName: String?,
        @Query("treat_id") treatId: Int?
    ): Call<List<SymptomsModel?>?>?
}