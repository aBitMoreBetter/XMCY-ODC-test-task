package com.epam.xmcy.testtask.datasource.csv.model;

import com.opencsv.bean.CsvBindByName;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CsvCryptoInfo {

	@CsvBindByName
	private long timestamp;
	@CsvBindByName
	private String symbol;
	@CsvBindByName
	private BigDecimal price;

}
