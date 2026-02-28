package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.CategoryEntity;
import life_ecom_logic_core.eddie.repository.CategoryRepository;
import life_ecom_logic_core.eddie.service.CategoryService;
import life_ecom_logic_core.eddie.service.mapper.CategoryMapper;
import life_ecom_logic_core.eddie.service.mapper.TransactionEnumMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for Category operations.
 * Handles CRUD operations for transaction categories.
 *
 * @author eddie
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionEnumMapper transactionEnumMapper = new TransactionEnumMapper();

    /**
     * Lists all categories, optionally filtered by transaction type.
     *
     * @param transactionType optional filter by transaction type
     * @return list of categories
     */
    @Override
    public List<Category> list(@Nullable TransactionType transactionType) {
        log.debug("Fetching categories with transactionType: {}", transactionType);

        List<CategoryEntity> entities = transactionType != null
                ? categoryRepository.findCategoryByTransactionsType(
                        transactionEnumMapper.mapToEntityEnum(transactionType))
                : categoryRepository.findAll();

        return entities.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific category by ID.
     *
     * @param id the category ID
     * @return optional containing the category if found
     */
    @Override
    public Optional<Category> get(Integer id) {
        log.debug("Fetching category with id: {}", id);
        return categoryRepository.findById(id)
                .map(CategoryMapper::toDto);
    }

    /**
     * Creates a new category.
     *
     * @param categoryCreate the category creation data
     * @return the created category
     */
    @Override
    public Category create(CategoryCreate categoryCreate) {
        log.info("Creating category with name: {}", categoryCreate.getName());
        CategoryEntity entity = CategoryMapper.toEntity(categoryCreate);
        CategoryEntity savedEntity = categoryRepository.save(entity);
        return CategoryMapper.toDto(savedEntity);
    }

    /**
     * Updates an existing category.
     *
     * @param id the category ID to update
     * @param categoryUpdate the update data
     * @return optional containing the updated category if found
     */
    @Override
    public Optional<Category> update(Integer id, CategoryUpdate categoryUpdate) {
        log.info("Updating category with id: {}", id);
        return categoryRepository.findById(id)
                .map(entity -> {
                    CategoryEntity updatedEntity = CategoryMapper.updateEntity(categoryUpdate, entity);
                    return CategoryMapper.toDto(categoryRepository.save(updatedEntity));
                });
    }

    /**
     * Deletes a category by ID.
     *
     * @param id the category ID to delete
     * @return true if deleted, false if not found
     */
    @Override
    public boolean delete(Integer id) {
        log.info("Deleting category with id: {}", id);
        if (!categoryRepository.existsById(id)) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }

}
