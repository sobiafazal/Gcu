package university.gardencity.gcu.managers;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import university.gardencity.gcu.constants.UrlConstants;

public class ServiceGenerator {
    private static Retrofit retrofit;
    private static Retrofit.Builder restBuilder;
    private static NetworkRequestManager requestManager;

    private static NetworkRequestManager getRestService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);
        httpClient.retryOnConnectionFailure(true);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(0, TimeUnit.SECONDS);

        restBuilder = new Retrofit.Builder()
                .baseUrl(UrlConstants.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = restBuilder.build();
        requestManager = retrofit.create(NetworkRequestManager.class);

        return requestManager;
    }

    public static NetworkRequestManager getRestWebService() {
        return getRestService();
    }
}
