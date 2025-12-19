package com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web;

import com.cotrafa.creditapproval.loan.domain.model.LoanReport;
import com.cotrafa.creditapproval.loan.domain.port.in.GetLoanReportUseCase;
import com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.dto.LoanReportResponse;
import com.cotrafa.creditapproval.loan.infrastructure.adapter.in.web.mapper.LoanReportWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {
    private final GetLoanReportUseCase reportUseCase;
    private final LoanReportWebMapper webMapper;

    @GetMapping("/report/total-approved")
    public ResponseEntity<LoanReportResponse> getTotalApproved() {
        LoanReport report = reportUseCase.getApprovedLoansReport();
        return ResponseEntity.ok(webMapper.toResponse(report));
    }
}
