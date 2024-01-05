package Words.BLL;

import Words.BE.Category;
import Words.DAL.ICategoryDataAccess;
import Words.DAL.db.CategoryDAO_DB;

import java.util.List;

public class CategoryManager implements ICategoryDataAccess {
    private ICategoryDataAccess categoryDAO;

    public CategoryManager() throws Exception {
        categoryDAO = new CategoryDAO_DB();
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        return categoryDAO.getAllCategories();
    }

    @Override
    public Category createCategory(Category newCategory) throws Exception {
        return categoryDAO.createCategory(newCategory);
    }

    @Override
    public void updateCategory(Category category) throws Exception {
        categoryDAO.updateCategory(category);
    }

    @Override
    public void deleteCategory(Category selectedCategory) throws Exception {
        categoryDAO.deleteCategory(selectedCategory);
    }
}
