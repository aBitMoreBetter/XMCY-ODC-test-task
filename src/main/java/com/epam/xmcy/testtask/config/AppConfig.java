package com.epam.xmcy.testtask.config;

import com.epam.xmcy.testtask.crypto.domain.CryptoInfo;
import com.epam.xmcy.testtask.datasource.CryptoInfoReceiver;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
@ConfigurationProperties
public class AppConfig {

	private final CryptoInfoReceiver cryptoInfoReceiver;

	@Getter
	@Setter
	private String cryptoValuesFolder;
	@Getter
	private Map<String, List<CryptoInfo>> cryptoInfoMap;


	@EventListener(ApplicationReadyEvent.class)
	private void setCryptoInfoMap() {
		cryptoInfoMap = Collections.unmodifiableMap(cryptoInfoReceiver.getCryptoInfoList(cryptoValuesFolder));
	}
}
