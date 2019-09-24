package it.acsi.cycling.races.service.util;

import java.time.LocalDate;
import java.time.Period;

public class CommonUtil {

    public static Integer getAgeFromBirthDate(LocalDate birthDate) {

        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

}
