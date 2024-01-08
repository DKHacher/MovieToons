package Words.GUI.Controller;


import Words.BE.Category;
import Words.GUI.Model.CategoryModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CategoryNewController {

    @FXML
    private TextField txtNewCat;
    private CategoryModel categoryModel;

    public void setManager(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }


    @FXML
    private void handleSave() {
        saveCategory();
    }

    @FXML
    private void handleClose() {
        closeWindow();
    }



    private void saveCategory() {
        try {
            String title = txtNewCat.getText();

            Category newCategory = new Category(-1, title);
            categoryModel.createNewCategory(newCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void closeWindow() {
        Platform.exit();
    }

}
