package Words.DAL.db;

import Words.BE.Category;
import Words.DAL.ICategoryDataAccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO_DB implements ICategoryDataAccess{

    private DatabaseConnector databaseConnector;

    public CategoryDAO_DB() throws Exception {
        databaseConnector = new DatabaseConnector();
    }

    @Override
    public List<Category> getAllCategories() throws Exception {
        ArrayList<Category> allCategories = new ArrayList<>();

        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement())
        {
            String sql =
                    """
                    SELECT Category.id, Category.name
                    FROM Category
                    """;

            ResultSet rs = stmt.executeQuery(sql);
            // Loop through rows from the database result set

            while (rs.next()) {
                int id = rs.getInt("id");
                String catType = rs.getString("name");

                Category category = new Category(id, catType);
                allCategories.add(category);
            }}
        return allCategories;
    }

    @Override
    public Category createCategory(Category category) throws Exception {
        // SQL command
        String sql = "INSERT INTO Category (name) VALUES (?);";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Bind parameters
            stmt.setString(1, category.getCatType());

            // Run the specified SQL statement
            stmt.executeUpdate();

            // Get the generated ID from the DB
            ResultSet rs = stmt.getGeneratedKeys();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            Category createdCategory = new Category(id, category.getCatType());

            return createdCategory;

        } catch (SQLException ex) {
            // create entry in log file
            ex.printStackTrace();
            throw new Exception("Could not insert Category.", ex);
        }
    }

    @Override
    public void updateCategory(Category category) throws Exception {
        // SQL command
        String sql = "UPDATE Category SET name = ? WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind parameters
            stmt.setString(1, category.getCatType());
            stmt.setInt(2, category.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not update Category.", ex);
        }
    }

    @Override
    public void deleteCategory(Category category) throws Exception {
        // SQL command to delete a movie by its ID
        String sql = "DELETE FROM Category WHERE id = ?;";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Bind the ID parameter
            stmt.setInt(1, category.getId());

            // Execute the delete SQL statement
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not delete category.", ex);
        }
    }
}
