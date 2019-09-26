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

    @Query(value = "select * from category c " +
                   "where gender = :genderType " +
                   "and min_age >= :age " +
                   "and :age < max_age " +
                   "order by min_age " +
                   "limit 1",
           nativeQuery = true)
    Optional<Category> findByGenderAndAge(@Param("genderType") String genderType, @Param("age") Integer age);

}
