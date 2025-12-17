package com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface CustomerPersistenceMapper {
    Customer toDomain(CustomerJpaEntity entity);
    CustomerJpaEntity toEntity(Customer domain);
}