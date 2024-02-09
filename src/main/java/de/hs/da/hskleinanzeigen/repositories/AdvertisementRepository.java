package de.hs.da.hskleinanzeigen.repositories;

import de.hs.da.hskleinanzeigen.entities.Advertisement;
import de.hs.da.hskleinanzeigen.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    @Query("SELECT a FROM Advertisement a " +
            "WHERE (:type is null or a.type = :type)" +
            "AND (:categoryId is null or a.category.id = :categoryId)" +
            "AND a.price BETWEEN :priceFrom AND :priceTo"
    )
    Page<Advertisement> findByTypeAndCategoryAndPriceBetween(@Param("type") Type type, @Param("categoryId") Integer categoryId, @Param("priceFrom") int priceFrom, @Param("priceTo") int priceTo, Pageable pageable);
}
