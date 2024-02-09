package de.hs.da.hskleinanzeigen.repositories;

import de.hs.da.hskleinanzeigen.entities.Notepad;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface NotepadRepository extends CrudRepository<Notepad, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Notepad n WHERE n.user.id = :userId AND n.advertisement.id = :advertisementId")
    void deleteByUserIdAndAdvertisementId(@Param("userId") Long userId, @Param("advertisementId") Long advertisementId);
}
