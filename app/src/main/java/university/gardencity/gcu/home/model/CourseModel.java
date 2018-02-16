package university.gardencity.gcu.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CourseModel {
    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("course_name")
    @Expose
    public String courseName;
    @SerializedName("course_abbr")
    @Expose
    public String courseAbbr;
    @SerializedName("clg")
    @Expose
    public String clg;
    @SerializedName("isug")
    @Expose
    public String isug;
    @SerializedName("dis_status")
    @Expose
    public String disStatus;
    @SerializedName("status")
    @Expose
    public String status;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseAbbr() {
        return courseAbbr;
    }

    public void setCourseAbbr(String courseAbbr) {
        this.courseAbbr = courseAbbr;
    }

    public String getClg() {
        return clg;
    }

    public void setClg(String clg) {
        this.clg = clg;
    }

    public String getIsug() {
        return isug;
    }

    public void setIsug(String isug) {
        this.isug = isug;
    }

    public String getDisStatus() {
        return disStatus;
    }

    public void setDisStatus(String disStatus) {
        this.disStatus = disStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CourseModel{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseAbbr='" + courseAbbr + '\'' +
                ", clg='" + clg + '\'' +
                ", isug='" + isug + '\'' +
                ", disStatus='" + disStatus + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
