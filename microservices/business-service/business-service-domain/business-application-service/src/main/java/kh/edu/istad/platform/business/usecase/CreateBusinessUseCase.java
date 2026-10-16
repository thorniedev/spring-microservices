package kh.edu.istad.platform.business.usecase;

import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.dto.CreateBusinessCommand;
import kh.edu.istad.platform.business.dto.CreateBusinessResult;
import kh.edu.istad.platform.business.entity.Business;
import kh.edu.istad.platform.business.entity.Product;
import kh.edu.istad.platform.business.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.business.port.out.BusinessRepository;
import kh.edu.istad.platform.business.service.BusinessDomainService;
import kh.edu.istad.platform.business.valueobject.BusinessContact;
import kh.edu.istad.platform.business.valueobject.BusinessName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateBusinessUseCase {

    private final BusinessDomainService businessDomainService;
    private final BusinessRepository businessRepository;

    public Mono<CreateBusinessResult> execute(CreateBusinessCommand command) {
        log.info("Executing reactive CreateBusinessUseCase for: {}", command.displayName());

        return Mono.fromCallable(() -> {
            List<Product> products = command.products() != null ? command.products().stream()
                    .map(dto -> new Product(
                            new ProductId(dto.id() != null ? dto.id() : UUID.randomUUID()),
                            dto.name(),
                            new Money(dto.price()),
                            dto.quantity(),
                            dto.available()
                    )).toList() : new ArrayList<>();

            Business business = Business.builder()
                    .businessName(new BusinessName(command.legalName(), command.displayName()))
                    .businessContact(new BusinessContact(command.email(), command.phoneNumber()))
                    .products(products)
                    .build();

            BusinessRegisteredEvent event = businessDomainService.validateAndInitiateBusiness(business);
            return event.business();
        })
        .flatMap(businessRepository::save)
        .map(savedBusiness -> {
            log.info("Business registered reactively with id: {}", savedBusiness.getId().getValue());
            return new CreateBusinessResult(
                    savedBusiness.getId().getValue(),
                    savedBusiness.getBusinessName().legalName(),
                    savedBusiness.getBusinessName().displayName(),
                    savedBusiness.isActive(),
                    "Business registered successfully!"
            );
        });
    }
}