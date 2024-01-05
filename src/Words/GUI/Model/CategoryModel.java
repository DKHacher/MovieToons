package Words.GUI.Model;

import Words.BE.Category;
import Words.BLL.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CategoryModel {

    private ObservableList<Category> categoriesToBeViewed;
    private CategoryManager categoryManager;

    public CategoryModel() throws Exception {
        categoryManager = new CategoryManager();
        categoriesToBeViewed = FXCollections.observableArrayList();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    public void loadCategories() {
        try {
            categoriesToBeViewed.clear();
            List<Category> allCategories = categoryManager.getAllCategories();
            categoriesToBeViewed.addAll(allCategories);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Category> getObservableCategories() {
        return categoriesToBeViewed;
    }

}
