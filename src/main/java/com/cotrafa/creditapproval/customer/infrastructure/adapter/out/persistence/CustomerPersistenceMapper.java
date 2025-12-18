package com.cotrafa.creditapproval.customer.infrastructure.adapter.out.persistence;

import com.cotrafa.creditapproval.customer.domain.model.Customer;
import com.cotrafa.creditapproval.shared.infrastructure.mapper.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface CustomerPersistenceMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "identificationType.id", source = "identificationTypeId")
    CustomerJpaEntity toEntity(Customer domain);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "identificationTypeId", source = "identificationType.id")
    Customer toDomain(CustomerJpaEntity entity);
}