package com.epam.xmcy.testtask.crypto.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.epam.xmcy.testtask.config.AppConfig;
import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import com.epam.xmcy.testtask.crypto.domain.CryptoInfoWithNormalizedData;
import com.epam.xmcy.testtask.crypto.model.CryptoInfoGeneral;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

@ExtendWith(MockitoExtension.class)
class CryptoRecommendationServiceTest {

	@Mock
	private AppConfig appConfig;

	@InjectMocks
	private CryptoRecommendationService unit;


	@Test
	void getCryptoInfoMap() {
		Map<String, List<CryptoInfo>> cryptoInfoMap = getCryptoInfoTestMap();
		when(appConfig.getCryptoInfoMap()).thenReturn(cryptoInfoMap);

		Map<String, List<CryptoInfo>> map = unit.getCryptoInfoMap();

		assertThat(map).hasSize(2);
	}

	@Test
	void getNormalizedCryptoInfoList() {
		Map<String, List<CryptoInfo>> cryptoInfoMap = getCryptoInfoTestMap();
		when(appConfig.getCryptoInfoMap()).thenReturn(cryptoInfoMap);

		List<CryptoInfoWithNormalizedData> cryptoInfoList = unit.getNormalizedCryptoInfoList();

		assertThat(cryptoInfoList).hasSize(7);
		assertThat(cryptoInfoList.get(2).getNormalizedPrice()).isEqualTo(new BigDecimal("0.5179545247"));
	}

	@Test
	void getGeneralCryptoInfo() {
		Map<String, List<CryptoInfo>> cryptoInfoMap = getCryptoInfoTestMap();

		when(appConfig.getCryptoInfoMap()).thenReturn(cryptoInfoMap);

		CryptoInfoGeneral generalCryptoInfo = unit.getGeneralCryptoInfo("BTC");
		CryptoInfo oldest = new CryptoInfo(1641081600000L, "BTC", new BigDecimal("43250.56"));
		CryptoInfo newest = new CryptoInfo(1642025000000L, "BTC", new BigDecimal("45555.55"));
		CryptoInfo min = new CryptoInfo(1641081600000L, "BTC", new BigDecimal("43250.56"));
		CryptoInfo max = new CryptoInfo(1642025000000L, "BTC", new BigDecimal("45555.55"));

		assertThat(generalCryptoInfo).isEqualTo(new CryptoInfoGeneral(oldest, newest, min, max));
	}

	@Test
	void getCryptoInfoByDate() {
		Map<String, List<CryptoInfo>> cryptoInfoMap = getCryptoInfoTestMap();
		when(appConfig.getCryptoInfoMap()).thenReturn(cryptoInfoMap);

		CryptoInfoWithNormalizedData cryptoInfoByDate = unit.getCryptoInfoByDate(LocalDate.parse("2022-01-02"));
		assertEquals(new BigDecimal("0.0841652241"), cryptoInfoByDate.getNormalizedPrice());
	}

	private static Map<String, List<CryptoInfo>> getCryptoInfoTestMap() {
		Map<String, List<CryptoInfo>> cryptoInfoMap = new HashMap<>();

		cryptoInfoMap.put("BTC",
			List.of(
				new CryptoInfo(1641081600000L, "BTC", new BigDecimal("43250.56")),
				new CryptoInfo(1641082600000L, "BTC", new BigDecimal("43444.56")),
				new CryptoInfo(1642024900000L, "BTC", new BigDecimal("44444.44")),
				new CryptoInfo(1642025000000L, "BTC", new BigDecimal("45555.55"))
			));

		cryptoInfoMap.put("ETH",
			List.of(
				new CryptoInfo(1642024800000L, "ETH", new BigDecimal("111.11")),
				new CryptoInfo(1643024900000L, "ETH", new BigDecimal("222.22")),
				new CryptoInfo(1644025000000L, "ETH", new BigDecimal("333.33"))
			));
		return cryptoInfoMap;
	}
}