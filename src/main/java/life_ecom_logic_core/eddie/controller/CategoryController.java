package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.CategoriesApiDelegate;
import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Controller delegate for Category-related API operations.
 * Implements the CategoriesApiDelegate interface generated from OpenAPI spec.
 *
 * @author eddie
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryController implements CategoriesApiDelegate {

    private final CategoryService categoryService;

    /**
     * Retrieves all categories, optionally filtered by transaction type.
     *
     * @param transactionType the transaction type filter (optional)
     * @return list of categories
     */
    @Override
    public ResponseEntity<List<Category>> categoriesGet(@Nullable TransactionType transactionType) {
        log.debug("Fetching categories with transactionType: {}", transactionType);
        return ResponseEntity.ok(categoryService.list(transactionType));
    }

    /**
     * Deletes a category by ID.
     *
     * @param id the category ID to delete
     * @return 200 if deleted, 404 if not found
     */
    @Override
    public ResponseEntity<Void> categoriesIdDelete(Integer id) {
        log.info("Deleting category with id: {}", id);
        return categoryService.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Retrieves a specific category by ID.
     *
     * @param id the category ID
     * @return the category if found, 404 otherwise
     */
    @Override
    public ResponseEntity<Category> categoriesIdGet(Integer id) {
        log.debug("Fetching category with id: {}", id);
        return categoryService.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Updates an existing category.
     *
     * @param id the category ID to update
     * @param categoryUpdate the update data
     * @return the updated category if found, 404 otherwise
     */
    @Override
    public ResponseEntity<Category> categoriesIdPut(Integer id, CategoryUpdate categoryUpdate) {
        log.info("Updating category with id: {}", id);
        return categoryService.update(id, categoryUpdate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Creates a new category.
     *
     * @param categoryCreate the category creation data
     * @return the created category with 201 status
     */
    @Override
    public ResponseEntity<Category> categoriesPost(CategoryCreate categoryCreate) {
        log.info("Creating category with name: {}", categoryCreate.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(categoryCreate));
    }

}
