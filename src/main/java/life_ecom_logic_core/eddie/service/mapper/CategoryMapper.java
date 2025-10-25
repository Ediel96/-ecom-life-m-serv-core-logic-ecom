package life_ecom_logic_core.eddie.service.mapper;


import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.domain.CategoryEntity;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static CategoryEntity toEntity(CategoryCreate src) {
        CategoryEntity e = new CategoryEntity();
        e.setKey(src.getKey());
        e.setName(src.getName());
        e.setColorFill(src.getColorFill());
        e.setColorBg(src.getColorBg());
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
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        if (e.getTransactionType() != null) {
            dto.setTransactionType(TransactionType.fromValue(String.valueOf(e.getTransactionType())));
        }
        return dto;
    }

    public static void updateEntity(CategoryUpdate e, CategoryEntity src) {
        if (src.getName() != null) e.setName(src.getName());
        if (src.getTransactionType() != null) e.setTransactionType(TransactionType.valueOf(src.getTransactionType().name().toLowerCase()));
    }

}