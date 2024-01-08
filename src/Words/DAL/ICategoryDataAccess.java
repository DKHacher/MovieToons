package Words.DAL;

import Words.BE.Category;

import java.util.List;

public interface ICategoryDataAccess {
    public List<Category> getAllCategories() throws Exception;

    public Category createCategory(Category category) throws Exception;

    public void updateCategory(Category category) throws Exception;

    public void deleteCategory(Category category) throws Exception;
}
