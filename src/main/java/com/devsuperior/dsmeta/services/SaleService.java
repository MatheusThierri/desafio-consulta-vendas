package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleMinReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleMinReportDTO> report(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate endDate = validEndDate(maxDate);
		LocalDate initDate = validInitDate(minDate, endDate);

		return repository.searchSales(initDate, endDate, name, pageable).map(x -> new SaleMinReportDTO(x));
	}

	public List<SaleSummaryProjection> summary(String minDate, String maxDate) {
		LocalDate endDate = validEndDate(maxDate);
		LocalDate initDate = validInitDate(minDate, endDate);

		return repository.summary(initDate, endDate);
	}

	private static LocalDate validInitDate(String minDate, LocalDate endDate) {
		LocalDate initDate;

		if(minDate != null && !minDate.isBlank()) {
			initDate = LocalDate.parse(minDate);
		} else {
			initDate = endDate.minusYears(1L);
		}

		return initDate;
	}

	private static LocalDate validEndDate(String maxDate) {
		LocalDate endDate;

		if(maxDate != null && !maxDate.isBlank()) {
			endDate = LocalDate.parse(maxDate);
		} else {
			endDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}

		return endDate;
	}
}
