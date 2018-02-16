package university.gardencity.gcu.services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataService implements Callback<String> {

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {

    }
}
