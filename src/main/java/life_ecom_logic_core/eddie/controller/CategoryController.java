package life_ecom_logic_core.eddie.controller;

import com.backend.organize_life.api.CategoriesApi;
import com.backend.organize_life.api.CategoriesApiDelegate;
import com.backend.organize_life.model.Category;
import com.backend.organize_life.model.CategoryCreate;
import com.backend.organize_life.model.CategoryUpdate;
import com.backend.organize_life.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryController implements CategoriesApiDelegate {

    @Override
    public ResponseEntity<List<Category>> categoriesGet(@Nullable TransactionType transactionType) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(Integer id) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Category> categoriesIdGet(Integer id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Category> categoriesIdPut(Integer id, CategoryUpdate categoryUpdate) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<Category> categoriesPost(CategoryCreate categoryCreate) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
