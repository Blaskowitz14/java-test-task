package ru.blaskowitz.java.test.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    @NotNull(message = "Recipient userId (to) is required")
    private Long to;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Transfer amount must be at least 0.01")
    @Digits(integer = 14, fraction = 2, message = "Amount cannot exceed 14 digits in integer part and 2 digits in fraction")
    private BigDecimal amount;
}