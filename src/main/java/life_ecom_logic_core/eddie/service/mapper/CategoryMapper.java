package life_ecom_logic_core.eddie.service.mapper;


import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.CategoryEntity;

import java.util.Optional;

public final class CategoryMapper {

    private static final String TYPE_NAME = "person";

    private CategoryMapper() {}

    public static CategoryEntity toEntity(CategoryCreate src) {
        CategoryEntity e = new CategoryEntity();
        e.setKey(src.getKey());
        e.setName(src.getName());
        e.setColorFill(src.getColorFill());
        e.setColorBg(src.getColorBg());
        e.setIcon(src.getIcon());
        e.setType(TYPE_NAME);
        e.setTransactionType(life_ecom_logic_core.eddie.domain.TransactionTypeEnum.valueOf(src.getTransactionType().name().toLowerCase()));
        e.setCreatedAt(java.time.OffsetDateTime.now());
        e.setUpdatedAt(java.time.OffsetDateTime.now());
        return e;
    }

    public static Category toDto(CategoryEntity e) {
        Category dto = new Category();
        dto.setKey(e.getKey());
        dto.setName(e.getName());
        dto.setColorFill(e.getColorFill());
        dto.setColorBg(e.getColorBg());
        dto.setIcon(e.getIcon());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        if (e.getTransactionType() != null) {
            dto.setTransactionType(TransactionType.fromValue(String.valueOf(e.getTransactionType())));
        }
        return dto;
    }

    public static Category toDto(Optional<CategoryEntity> e) {
        Category dto = new Category();
        CategoryEntity entity = e.orElseThrow(() -> new IllegalArgumentException("Category not found"));
        dto.setKey(entity.getKey());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());
        dto.setColorFill(entity.getColorFill());
        dto.setColorBg(entity.getColorBg());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        if (entity.getTransactionType() != null) {
            dto.setTransactionType(TransactionType.fromValue(String.valueOf(entity.getTransactionType())));
        }
        return dto;
    }

    public static CategoryEntity updateEntity(CategoryUpdate update, CategoryEntity entity) {
        if (update.getKey() != null) entity.setKey(update.getKey());
        if (update.getName() != null) entity.setName(update.getName());
        if (update.getColorFill() != null) entity.setColorFill(update.getColorFill());
        if (update.getColorBg() != null) entity.setColorBg(update.getColorBg());
        if (update.getIcon() != null) entity.setIcon(update.getIcon());
        if (update.getTransactionType() != null) {
            entity.setTransactionType(
                    life_ecom_logic_core.eddie.domain.TransactionTypeEnum.valueOf(
                            update.getTransactionType().name().toLowerCase()
                    )
            );
        }
        entity.setUpdatedAt(java.time.OffsetDateTime.now());
        return entity;
    }

}