package kh.edu.istad.platform.business.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.ProductId;
import kh.edu.istad.platform.business.entity.Business;
import kh.edu.istad.platform.business.entity.Product;
import kh.edu.istad.platform.business.persistence.document.BusinessDocument;
import kh.edu.istad.platform.business.persistence.document.ProductDocument;
import kh.edu.istad.platform.business.valueobject.BusinessContact;
import kh.edu.istad.platform.business.valueobject.BusinessName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BusinessDocumentMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "legalName", source = "businessName.legalName")
    @Mapping(target = "displayName", source = "businessName.displayName")
    @Mapping(target = "email", source = "businessContact.email")
    @Mapping(target = "phoneNumber", source = "businessContact.phoneNumber")
    BusinessDocument toDocument(Business business);

    @Mapping(target = "businessId", source = "id")
    @Mapping(target = "businessName", expression = "java(new BusinessName(document.getLegalName(), document.getDisplayName()))")
    @Mapping(target = "businessContact", expression = "java(new BusinessContact(document.getEmail(), document.getPhoneNumber()))")
    @Mapping(target = "orderApproval", ignore = true)
    @Mapping(target = "orderDetail", ignore = true)
    Business toDomain(BusinessDocument document);

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "price", source = "price.amount")
    ProductDocument toProductDocument(Product product);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "price", source = "price")
    Product toProductDomain(ProductDocument document);

    default BusinessId mapBusinessId(UUID value) { return value != null ? new BusinessId(value) : null; }
    default ProductId mapProductId(UUID value) { return value != null ? new ProductId(value) : null; }
    default Money mapMoney(BigDecimal value) { return value != null ? new Money(value) : null; }
}