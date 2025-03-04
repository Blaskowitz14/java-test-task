package ru.blaskowitz.java.test.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
   private Long id;
   private BigDecimal initialBalance;
   private BigDecimal balance;
}
