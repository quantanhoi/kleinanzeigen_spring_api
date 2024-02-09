package de.hs.da.hskleinanzeigen.repositories;

import de.hs.da.hskleinanzeigen.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Optional<Category> findOneByName(String name);
}
