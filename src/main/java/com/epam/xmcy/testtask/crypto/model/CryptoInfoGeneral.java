package com.epam.xmcy.testtask.crypto.model;

import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CryptoInfoGeneral {

	private CryptoInfo oldest;
	private CryptoInfo newest;
	private CryptoInfo min;
	private CryptoInfo max;

}
