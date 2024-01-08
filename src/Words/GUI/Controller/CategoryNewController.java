package Words.GUI.Controller;


import Words.BE.Category;
import Words.GUI.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CategoryNewController {

    @FXML
    private TextField txtNewCat;
    private CategoryModel categoryModel;

    public void setManager(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }


    @FXML
    private void handleSave(ActionEvent actionEvent) {
        try {
            String title = txtNewCat.getText();

            Category newCategory = new Category(-1, title);
            categoryModel.createNewCategory(newCategory);

            Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.control.Button) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


}
