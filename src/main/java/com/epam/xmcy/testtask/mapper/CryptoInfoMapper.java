package com.epam.xmcy.testtask.mapper;

import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import com.epam.xmcy.testtask.crypto.domain.CryptoInfoWithNormalizedData;
import org.mapstruct.Mapper;

@Mapper
public interface CryptoInfoMapper {

	CryptoInfo toCryptoInfo(CryptoInfoWithNormalizedData cryptoInfoWithNormalizedData);

}