package com.cotrafa.creditapproval.loanrequest.domain.port.in;

import java.util.UUID;

public interface GetAiAdviceUseCase {
    String execute(UUID requestId);
}