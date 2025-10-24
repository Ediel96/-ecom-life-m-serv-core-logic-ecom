package life_ecom_logic_core.eddie.service;

import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> list(@Nullable TransactionType transactionType);

    Optional<Category> get(Integer id);

    Category create(CategoryCreate categoryCreate);

    Optional<Category> update(Integer id, CategoryUpdate categoryUpdate);

    boolean delete(Integer id);

}
