package kh.edu.istad.platform.customer.persistance.adapter;

import kh.edu.istad.platform.customer.entity.Customer;
import kh.edu.istad.platform.customer.persistance.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistance.enity.CustomerEntity;
import kh.edu.istad.platform.customer.persistance.mapper.CustomerDocumentMapper;
import kh.edu.istad.platform.customer.persistance.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "persistence.db-type", havingValue = "mongo")
@RequiredArgsConstructor
public class CustomerMongoRepositoryAdapter implements CustomerRepository{

    private final CustomerMongoRepository mongoRepositoryAdapter;
    private final CustomerDocumentMapper customerDocumentMapper;

    @Override
    public Customer save(Customer customer) {

        // Domain -> Document
        CustomerDocument customerDocument = customerDocumentMapper.toDocument(customer);

        // Save to Mongo
        CustomerDocument savedDocument = mongoRepositoryAdapter.save(customerDocument).block();

        return customerDocumentMapper.toDomain(savedDocument);
    }
}
