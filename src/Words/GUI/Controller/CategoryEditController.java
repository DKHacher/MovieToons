package Words.GUI.Controller;

import Words.BE.Category;
import Words.GUI.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
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
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSave(ActionEvent actionEvent) {
        String newCatTitle = txtNewCatTitle.getText();

        if (categoryModel != null && selectedCategory != null) {
            try {
                categoryModel.updateCategory(selectedCategory, newCatTitle);

                Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        if (selectedCategory != null) {
            txtNewCatTitle.setText(selectedCategory.getCatType());
        }
    }

}
