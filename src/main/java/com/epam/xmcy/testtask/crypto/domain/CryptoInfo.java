package com.epam.xmcy.testtask.crypto.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoInfo {

	private long timestamp;
	private final String symbol;
	private BigDecimal price;
}
