package com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.mapper;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto.CreateCustomerDTO;
import com.cotrafa.creditapproval.customer.infrastructure.adapter.in.web.dto.CustomerResponse;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = CentralMapperConfig.class)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Customer toDomain(CreateCustomerDTO dto);

    CustomerResponse toResponse(Customer domain);
}
