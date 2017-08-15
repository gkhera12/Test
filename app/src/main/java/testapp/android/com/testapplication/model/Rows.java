package testapp.android.com.testapplication.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model created from the Json Request
 * */
public class Rows {
    @SerializedName("rows")
    @Expose
    private List<News> rows = null;

    public List<News> getRows() {
        return rows;
    }

    public void setRows(List<News> rows) {
        this.rows = rows;
    }
}
