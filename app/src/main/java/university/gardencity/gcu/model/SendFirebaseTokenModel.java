package university.gardencity.gcu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SendFirebaseTokenModel {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("fb_token")
    @Expose
    public String fbToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }
}
