package com.epam.xmcy.testtask.datasource.csv.mapper;

import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import com.epam.xmcy.testtask.datasource.csv.model.CsvCryptoInfo;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface CsvCryptoInfoMapper {

	List<CryptoInfo> toCryptoInfo(List<CsvCryptoInfo> csvCryptoInfoList);

}