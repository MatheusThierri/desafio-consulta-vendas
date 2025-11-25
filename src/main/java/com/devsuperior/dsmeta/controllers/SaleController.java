package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.dto.SaleMinReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.services.SaleService;

import java.util.List;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

	@Autowired
	private SaleService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
		SaleMinDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/report")
	public ResponseEntity<Page<SaleMinReportDTO>> getReport(@RequestParam(name = "minDate", required = false) String minDate,
															@RequestParam(name = "maxDate", required = false) String maxDate,
															@RequestParam(name = "name", defaultValue = "") String name,
															Pageable pageable){
		return ResponseEntity.ok().body(service.report(minDate, maxDate, name, pageable));
	}

	@GetMapping(value = "/summary")
	public ResponseEntity<List<SaleSummaryProjection>> getSummary(@RequestParam(name = "minDate", required = false) String minDate,
																  @RequestParam(name = "maxDate", required = false) String maxDate) {
		return ResponseEntity.ok().body(service.summary(minDate, maxDate));
	}
}
