package Words.BE;

public class Category {

    private String catType;
    private int id;

    public Category(int id, String catType) {
        this.id = id;
        this.catType = catType;
    }


    public String getCatType() {
        return catType;
    }


    public int getId() {
        return id;
    }

    public void setCatType(String newCatType) {
        this.catType =  newCatType;
    }

    @Override
    public String toString() {
        return catType;
    }

}
