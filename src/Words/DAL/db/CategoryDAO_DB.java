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
        try (Connection conn = databaseConnector.getConnection()) {
            // Check for missing indexes
            int newIndex = findMissingIndex(conn);

            // SQL command
            String sql = "INSERT INTO Category (id, name) VALUES (?, ?);";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Bind the parameters
                stmt.setInt(1, newIndex);
                stmt.setString(2, category.getCatType());

                // Run the specified SQL statement
                stmt.executeUpdate();
                Category createdCategory = new Category(newIndex, category.getCatType());
                return createdCategory;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not insert Category.", ex);
        }
    }

    private int findMissingIndex(Connection conn) throws SQLException {
        // SQL command to find missing index
        String findMissingIndexSql = "SELECT MIN(t1.id + 1) AS missing_index " +
                "FROM Category t1 " +
                "LEFT JOIN Category t2 ON t1.id + 1 = t2.id " +
                "WHERE t2.id IS NULL";

        try (PreparedStatement stmt = conn.prepareStatement(findMissingIndexSql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int missingIndex = rs.getInt("missing_index");
                return missingIndex;
            } else {
                // No missing index, get the next available index
                String getMaxIndexSql = "SELECT MAX(id) + 1 AS next_index FROM Category";
                try (PreparedStatement getMaxIndexStmt = conn.prepareStatement(getMaxIndexSql)) {
                    ResultSet maxIndexRs = getMaxIndexStmt.executeQuery();

                    if (maxIndexRs.next()) {
                        int nextIndex = maxIndexRs.getInt("next_index");
                        return nextIndex;
                    }
                }
            }
        }
        // Default to -1 if something goes wrong
        return -1;
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
