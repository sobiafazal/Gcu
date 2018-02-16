package university.gardencity.gcu.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EventsModel {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("event_title")
    @Expose
    private String eventTitle;
    @SerializedName("event_dis")
    @Expose
    private String eventDis;
    @SerializedName("display")
    @Expose
    private Boolean display;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("event_past")
    @Expose
    private Boolean eventPast;
    @SerializedName("link")
    @Expose
    private String link;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDis() {
        return eventDis;
    }

    public void setEventDis(String eventDis) {
        this.eventDis = eventDis;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Boolean getEventPast() {
        return eventPast;
    }

    public void setEventPast(Boolean eventPast) {
        this.eventPast = eventPast;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "EventsModel{" +
                "date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", eventTitle='" + eventTitle + '\'' +
                ", eventDis='" + eventDis + '\'' +
                ", display=" + display +
                ", img='" + img + '\'' +
                ", eventPast=" + eventPast +
                ", link='" + link + '\'' +
                '}';
    }
}
