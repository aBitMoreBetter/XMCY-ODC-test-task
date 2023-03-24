package com.epam.xmcy.testtask.crypto.controller;


import com.epam.xmcy.testtask.crypto.domain.CryptoInfoWithNormalizedData;
import com.epam.xmcy.testtask.crypto.model.CryptoInfoGeneral;
import com.epam.xmcy.testtask.crypto.service.CryptoRecommendationService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/crypto/info/")
public class CryptoRecommendationController {

	private final CryptoRecommendationService recommendationService;


	@GetMapping("normalized")
	public ResponseEntity<List<CryptoInfoWithNormalizedData>> getNormalizedCryptoInfoList() {
		return ResponseEntity.ok(recommendationService.getNormalizedCryptoInfoList());
	}

	@GetMapping("{symbol}")
	public ResponseEntity<CryptoInfoGeneral> getGeneralCryptoInfo(@PathVariable("symbol") String symbol) {
		return ResponseEntity.ok(recommendationService.getGeneralCryptoInfo(symbol));
	}


	@GetMapping("/date/{date}")
	public ResponseEntity<CryptoInfoWithNormalizedData> getCryptoInfoByDate(@PathVariable("date") String date) {
		return ResponseEntity.ok(recommendationService.getCryptoInfoByDate(LocalDate.parse(date)));
	}

}
