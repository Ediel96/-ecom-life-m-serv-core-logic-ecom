package life_ecom_logic_core.eddie.service.Impl;

import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.CategoryEntity;
import life_ecom_logic_core.eddie.domain.TransactionTypeEnum;
import life_ecom_logic_core.eddie.repository.CategoryRepository;
import life_ecom_logic_core.eddie.service.CategoryService;
import life_ecom_logic_core.eddie.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImp  implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> list(@Nullable TransactionType transactionType) {
        List<CategoryEntity> entities = transactionType != null
                ? categoryRepository.findCategoryByTransactionsType(mapToEntityEnum(transactionType))
                : categoryRepository.findAll();

        return entities.stream()
                .map(CategoryMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }
    @Override
    public Optional<Category> get(Integer id) {
        Optional<CategoryEntity> entity = categoryRepository.findById(id);
        return Optional.of(CategoryMapper.toDto(entity));
    }

    @Override
    public Category create(CategoryCreate categoryCreate) {
        CategoryEntity entity = CategoryMapper.toEntity(categoryCreate);
        categoryRepository.save(entity);
        return CategoryMapper.toDto(entity);
    }

    @Override
    public Optional<Category> update(Integer id, CategoryUpdate categoryUpdate) {
        return categoryRepository.findById(id).map(e -> {
            CategoryEntity update =  CategoryMapper.updateEntity(categoryUpdate, e);
            return CategoryMapper.toDto(categoryRepository.save(update));
        });
    }

    @Override
    public boolean delete(Integer id) {
        if (!categoryRepository.existsById(id)) return false;
        categoryRepository.deleteById(id);
        return true;
    }

    private TransactionTypeEnum mapToEntityEnum(TransactionType transactionType) {
        return switch (transactionType) {
            case INCOME -> TransactionTypeEnum.income;
            case EXPENSE -> TransactionTypeEnum.expense;
        };
    }

}
