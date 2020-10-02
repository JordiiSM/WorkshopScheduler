package Model;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Workshops {

    @SerializedName("workshops")
    @Expose
    private List<Workshop> workshops = null;
    @SerializedName("compatibilityMatrix")
    @Expose
    private List<List<Integer>> compatibilityMatrix = null;

    public List<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<Workshop> workshops) {
        this.workshops = workshops;
    }

    public List<List<Integer>> getCompatibilityMatrix() {
        return compatibilityMatrix;
    }

    public void setCompatibilityMatrix(List<List<Integer>> compatibilityMatrix) {
        this.compatibilityMatrix = compatibilityMatrix;
    }

}
