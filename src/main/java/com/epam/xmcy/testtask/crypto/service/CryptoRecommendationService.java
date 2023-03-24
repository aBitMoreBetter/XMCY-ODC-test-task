package com.epam.xmcy.testtask.crypto.service;

import com.epam.xmcy.testtask.config.AppConfig;
import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import com.epam.xmcy.testtask.crypto.domain.CryptoInfoWithNormalizedData;
import com.epam.xmcy.testtask.crypto.model.CryptoInfoGeneral;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoRecommendationService {

	private static final long NANOS_IN_MILLISECOND = 1000L;
	private final AppConfig appConfig;
	private List<CryptoInfoWithNormalizedData> reversedSortedNormalizedAllCryptoInfoList = null;
	private final Map<String, CryptoInfoGeneral> cryptoInfoGeneralMap = new HashMap<>();

	public Map<String, List<CryptoInfo>> getCryptoInfoMap() {
		return appConfig.getCryptoInfoMap();
	}

	public List<CryptoInfoWithNormalizedData> getNormalizedCryptoInfoList() {
		if (reversedSortedNormalizedAllCryptoInfoList == null) {
			reversedSortedNormalizedAllCryptoInfoList =
				getNormalizedCryptoInfoMap(getCryptoInfoMap()).values().stream()
					.flatMap(Collection::stream)
					.sorted(Comparator.comparing(CryptoInfoWithNormalizedData::getNormalizedPrice).reversed())
					.collect(Collectors.toList());
		}

		return reversedSortedNormalizedAllCryptoInfoList;
	}

	public CryptoInfoGeneral getGeneralCryptoInfo(String symbol) {
		if (cryptoInfoGeneralMap.containsKey(symbol)) {
			return cryptoInfoGeneralMap.get(symbol);
		}

		List<CryptoInfo> cryptoInfoList = getCryptoInfoMap().get(symbol);

		CryptoInfo oldest = cryptoInfoList.stream().min(Comparator.comparing(CryptoInfo::getTimestamp)).orElseThrow();
		CryptoInfo newest = cryptoInfoList.stream().max(Comparator.comparing(CryptoInfo::getTimestamp)).orElseThrow();

		CryptoInfo min = cryptoInfoList.stream().min(Comparator.comparing(CryptoInfo::getPrice)).orElseThrow();
		CryptoInfo max = cryptoInfoList.stream().max(Comparator.comparing(CryptoInfo::getPrice)).orElseThrow();

		CryptoInfoGeneral cryptoInfoGeneral = new CryptoInfoGeneral(oldest, newest, min, max);
		cryptoInfoGeneralMap.put(symbol, cryptoInfoGeneral);

		return cryptoInfoGeneral;

	}

	public CryptoInfoWithNormalizedData getCryptoInfoByDate(LocalDate date) {
		LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);

		long startTimestamp = Timestamp.valueOf(localDateTime).getTime();
		long endTimestamp = Timestamp.valueOf(localDateTime.plusDays(1).minusNanos(NANOS_IN_MILLISECOND)).getTime();

		Predicate<CryptoInfoWithNormalizedData> moreThanTimestamp = i -> i.getTimestamp() > startTimestamp;
		Predicate<CryptoInfoWithNormalizedData> lessThanTimestamp = i -> i.getTimestamp() < endTimestamp;

		List<CryptoInfoWithNormalizedData> normalizedCryptoInfoList = getNormalizedCryptoInfoList();
		return normalizedCryptoInfoList.stream()
			.filter(moreThanTimestamp)
			.filter(lessThanTimestamp)
			.max(Comparator.comparing(CryptoInfoWithNormalizedData::getNormalizedPrice))
			.orElseThrow();

	}

	private Map<String, List<CryptoInfoWithNormalizedData>> getNormalizedCryptoInfoMap(
		Map<String, List<CryptoInfo>> cryptoInfoMap) {
		Map<String, List<CryptoInfoWithNormalizedData>> normalizedMap = new HashMap<>();
		cryptoInfoMap.forEach((key, value) -> normalizedMap.put(key, getNormalizedCryptoInfoList(value)));
		return normalizedMap;
	}

	private List<CryptoInfoWithNormalizedData> getNormalizedCryptoInfoList(List<CryptoInfo> cryptoInfoMap) {
		BigDecimal min;
		BigDecimal max;
		BigDecimal diff;

		min = cryptoInfoMap.stream().min(Comparator.comparing(CryptoInfo::getPrice)).orElseThrow().getPrice();
		max = cryptoInfoMap.stream().max(Comparator.comparing(CryptoInfo::getPrice)).orElseThrow().getPrice();

		diff = max.subtract(min);

		List<CryptoInfoWithNormalizedData> normalizedList = new ArrayList<>();

		for (CryptoInfo cryptoInfo : cryptoInfoMap) {
			normalizedList.add(new CryptoInfoWithNormalizedData(cryptoInfo.getTimestamp(), cryptoInfo.getSymbol(),
				cryptoInfo.getPrice(), getNormalizedValue(min, diff, cryptoInfo)));
		}

		return normalizedList;

	}

	private static BigDecimal getNormalizedValue(BigDecimal min, BigDecimal diff, CryptoInfo cryptoInfo) {
		return cryptoInfo.getPrice().setScale(10, RoundingMode.HALF_UP).subtract(min)
			.divide(diff, RoundingMode.HALF_UP);
	}

}
