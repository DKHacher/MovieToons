package Words.GUI.Controller;

import Words.BE.Category;
import Words.GUI.Model.CategoryModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CategoryEditController {

    @FXML
    private TextField txtNewCatTitle;
    private CategoryModel categoryModel;
    private Category selectedCategory;

    public void setManager(CategoryModel model) {
        this.categoryModel = model;
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }

    @FXML
    private void handleSave() {
        saveCategory();
    }

    private void saveCategory() {
        String newCatTitle = txtNewCatTitle.getText();

        if (categoryModel != null && selectedCategory != null) {
            try {
                categoryModel.updateCategory(selectedCategory, newCatTitle);
                Platform.exit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void closeWindow() {
        Platform.exit();
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        if (selectedCategory != null) {
            txtNewCatTitle.setText(selectedCategory.getCatType());
        }
    }

}
