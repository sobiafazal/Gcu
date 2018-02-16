package university.gardencity.gcu.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutModel {

    @SerializedName("about")
    @Expose
    public String about;
    @SerializedName("history")
    @Expose
    public String history;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("link_action")
    @Expose
    public String linkAction;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkAction() {
        return linkAction;
    }

    public void setLinkAction(String linkAction) {
        this.linkAction = linkAction;
    }

    @Override
    public String toString() {
        return "AboutModel{" +
                "about='" + about + '\'' +
                ", history='" + history + '\'' +
                ", link='" + link + '\'' +
                ", linkAction='" + linkAction + '\'' +
                '}';
    }
}
