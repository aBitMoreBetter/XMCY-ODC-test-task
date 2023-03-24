package com.epam.xmcy.testtask.crypto.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoInfoWithNormalizedData {

	private long timestamp;
	private final String symbol;
	private BigDecimal price;
	private BigDecimal normalizedPrice;
}
