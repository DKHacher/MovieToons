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

    public void createNewCategory(Category newCategory) throws Exception {
        Category c = categoryManager.createCategory(newCategory);
        categoriesToBeViewed.add(c);
    }

    public void deleteCategory(Category selectedCategory) throws Exception {
        categoryManager.deleteCategory(selectedCategory);
        categoriesToBeViewed.remove(selectedCategory);
    }

    public void updateCategory(Category category, String newCatTitle) throws Exception {
        category.setCatType(newCatTitle);
        categoryManager.updateCategory(category);
        loadCategories();
    }

}
