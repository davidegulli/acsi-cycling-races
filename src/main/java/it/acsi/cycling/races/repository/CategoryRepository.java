package it.acsi.cycling.races.repository;

import it.acsi.cycling.races.domain.Category;
import it.acsi.cycling.races.domain.enumeration.GenderType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where gender = :genderType and minAge >= :age and :age < maxAge order by minAge")
    Optional<Category> findByGenderAndAge(@Param("genderType") GenderType genderType, @Param("age") Integer age);

}
