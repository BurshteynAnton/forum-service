package telran.java53.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DatePeriodDto {
    LocalDate dateFrom;
    LocalDate dateTo;
}
