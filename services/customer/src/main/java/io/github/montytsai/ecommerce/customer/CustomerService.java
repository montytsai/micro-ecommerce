package io.github.montytsai.ecommerce.customer;

import io.github.montytsai.ecommerce.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public List<CustomerResponse> findAllCustomers() {
        log.info("Fetching all active customers.");
        return repository.findAll().stream()
                .map(mapper::fromCustomer)
                .toList();
    }

    public boolean existsCustomerById(String id) {
        log.info("Checking if customer exists with id: {}", id);
        return repository.existsById(id);
    }

    public CustomerResponse findCustomerById(String id) {
        log.info("Fetching customer with id: {}", id);
        return repository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer not found with id: %s", id)));
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        log.info("Creating a new customer");
        var customer = repository.save(mapper.toCustomer(request));
        return mapper.fromCustomer(customer);
    }

    public CustomerResponse updateCustomer(String id, CustomerRequest request) {
        log.info("Performing full update for customer: {}", id);
        if (!repository.existsById(id)) {
            throw new CustomerNotFoundException(String.format("Customer not found with id: %s", id));
        }

        var updatedCustomer = mapper.toCustomer(request);
        updatedCustomer.setId(id);
        return mapper.fromCustomer(repository.save(updatedCustomer));
    }

    public CustomerResponse patchCustomer(String id, CustomerRequest request) {
        log.info("Performing partial update (PATCH) for customer: {}", id);
        var existingCustomer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer not found with id: %s", id)));

        mapper.patchCustomer(request, existingCustomer);
        return mapper.fromCustomer(repository.save(existingCustomer));
    }

    public void deleteCustomerById(String id) {
        log.info("Deleting customer with id: {}", id);
        if (!repository.existsById(id)) {
            throw new CustomerNotFoundException(String.format("Customer not found with id: %s", id));
        }
        repository.deleteById(id);
    }

}
