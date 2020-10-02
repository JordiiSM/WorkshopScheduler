
package Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workshop {

    @SerializedName("acronym")
    @Expose
    private String acronym;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("timetable")
    @Expose
    private List<Timetable> timetable = null;
    @SerializedName("rgbColor")
    @Expose
    private List<Integer> rgbColor = null;

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<Timetable> getTimetable() {
        return timetable;
    }

    public void setTimetable(List<Timetable> timetable) {
        this.timetable = timetable;
    }

    public List<Integer> getRgbColor() {
        return rgbColor;
    }

    public void setRgbColor(List<Integer> rgbColor) {
        this.rgbColor = rgbColor;
    }

}
