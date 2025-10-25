package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.CategoriesApi;
import com.backend.organize_life.api.CategoriesApiDelegate;
import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import life_ecom_logic_core.eddie.service.Impl.CategoryServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class CategoryController implements CategoriesApiDelegate {

    @Autowired
    private CategoryServiceImp categoryService;

    @Override
    public ResponseEntity<List<Category>> categoriesGet(@Nullable TransactionType transactionType) {
        return ResponseEntity.ok(categoryService.list(transactionType));
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(Integer id) {
        return categoryService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Category> categoriesIdGet(Integer id) {
        Category category = categoryService.get(id).orElse(null);
        return category == null
                ? ResponseEntity.ok(category)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Category> categoriesIdPut(Integer id, CategoryUpdate categoryUpdate) {
        return categoryService.update(id, categoryUpdate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Override
    public ResponseEntity<Category> categoriesPost(CategoryCreate categoryCreate) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.create(categoryCreate));
    }

}
