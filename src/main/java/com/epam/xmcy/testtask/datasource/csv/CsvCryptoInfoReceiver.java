package com.epam.xmcy.testtask.datasource.csv;

import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import com.epam.xmcy.testtask.datasource.CryptoInfoReceiver;
import com.epam.xmcy.testtask.datasource.csv.mapper.CsvCryptoInfoMapper;
import com.epam.xmcy.testtask.datasource.csv.model.CsvCryptoInfo;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Log4j2
@Component
@RequiredArgsConstructor
public class CsvCryptoInfoReceiver implements CryptoInfoReceiver {

	private final CsvCryptoInfoMapper csvCryptoInfoMapper;

	@Override
	public Map<String, List<CryptoInfo>> getCryptoInfoList(String cryptoValuesFolder) {
		Map<String, List<CryptoInfo>> cryptoInfoMap = new HashMap<>();
		try {
			File directory = ResourceUtils.getFile("classpath:" + cryptoValuesFolder);

			for (File file : Objects.requireNonNull(directory.listFiles())) {
				List<CryptoInfo> cryptoInfoList = csvCryptoInfoMapper.toCryptoInfo(getCsvCryptoInfoList(file));
				cryptoInfoMap.put(cryptoInfoList.get(0).getSymbol(), cryptoInfoList);
			}

		} catch (IOException e) {
			log.error(e.getMessage(), e);
			System.exit(1);
		}
		return cryptoInfoMap;
	}

	private static List<CsvCryptoInfo> getCsvCryptoInfoList(File file) throws FileNotFoundException {
		CsvToBean<CsvCryptoInfo> csvToBean = new CsvToBeanBuilder(new FileReader(file))
			.withType(CsvCryptoInfo.class)
			.build();

		return csvToBean.parse();
	}
}
