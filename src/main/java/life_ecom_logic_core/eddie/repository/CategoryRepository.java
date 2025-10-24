package life_ecom_logic_core.eddie.repository;

import life_ecom_logic_core.eddie.domain.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository  extends JpaRepository<CategoryEntity, Integer> {

    List<CategoryEntity> findAll();

    CategoryEntity findCategoryById(Integer id);

    @Query("SELECT c FROM CategoryEntity c WHERE c.transactionType = ?1")
    List<CategoryEntity> findCategoryByTransactionsType(String key);

}
