package university.gardencity.gcu.managers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import university.gardencity.gcu.constants.UrlConstants;
import university.gardencity.gcu.home.model.AboutModel;
import university.gardencity.gcu.home.model.CourseModel;
import university.gardencity.gcu.home.model.EventsModel;
import university.gardencity.gcu.model.GenericResponse;
import university.gardencity.gcu.model.SendFirebaseTokenModel;

public interface NetworkRequestManager {

    @Headers({"Content-Type: application/json;charset=utf-8"})
    @GET(UrlConstants.GET_DATA)
    Call<List<EventsModel>> getNews(@Query("id") int id);

    @Headers({"Content-Type: application/json;charset=utf-8"})
    @GET(UrlConstants.GET_DATA)
    Call<AboutModel> getAbout(@Query("id") int id);

    @Headers({"Content-Type: text/plain;charset=utf-8", "Accept: text/plain"})
    @GET(UrlConstants.GET_DATA)
    Call<AboutModel> getPlacementContent(@Query("id") int id);

    @POST(UrlConstants.SAVE_FIREBASE_TOKEN)
    Call<GenericResponse> saveToken(@Body SendFirebaseTokenModel jsonBody);

    @GET(UrlConstants.COURSE_LIST_API)
    Call<ArrayList<CourseModel>> getCourses();

    @FormUrlEncoded
    @POST(UrlConstants.SAVE_ENQUIRY_FORM)
    Call<GenericResponse> saveEnquiry(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("phone") String phone,
                                      @Field("city") String city);

}
