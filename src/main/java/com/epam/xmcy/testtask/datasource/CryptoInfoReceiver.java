package com.epam.xmcy.testtask.datasource;

import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import java.util.List;
import java.util.Map;

public interface CryptoInfoReceiver {

	Map<String, List<CryptoInfo>> getCryptoInfoList(String cryptoValuesFolder);
}
