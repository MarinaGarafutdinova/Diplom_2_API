package objects;

import java.util.List;

public class Ingredients {
    private List<objects.Ingredient> data;
    private String success;

    public Ingredients(List<objects.Ingredient> data, String success) {
        this.data = data;
        this.success = success;
    }

    public List<objects.Ingredient> getData() {

        return data;
    }

    public void setData(List<objects.Ingredient> data) {

        this.data = data;
    }

    public String getSuccess() {

        return success;
    }

    public void setSuccess(String success) {

        this.success = success;
    }
}
