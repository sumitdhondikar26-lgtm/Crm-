package com.crm.service;

import com.crm.dto.CustomerDTO;
import com.crm.entity.Customer;
import com.crm.entity.User;
import com.crm.enums.CustomerStatus;
import com.crm.exception.BadRequestException;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.CustomerRepository;
import com.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(this::toDTO);
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return toDTO(customer);
    }

    public Page<CustomerDTO> searchCustomers(String search, Pageable pageable) {
        return customerRepository.searchCustomers(search, pageable).map(this::toDTO);
    }

    public Page<CustomerDTO> getCustomersByStatus(String status, Pageable pageable) {
        CustomerStatus customerStatus = CustomerStatus.valueOf(status.toUpperCase());
        return customerRepository.findByStatus(customerStatus, pageable).map(this::toDTO);
    }

    public Page<CustomerDTO> getCustomersByAssignee(Long userId, Pageable pageable) {
        return customerRepository.findByAssignedToId(userId, pageable).map(this::toDTO);
    }

    public CustomerDTO createCustomer(CustomerDTO dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Customer with email " + dto.getEmail() + " already exists");
        }

        Customer customer = toEntity(dto);
        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getAssignedToId()));
            customer.setAssignedTo(user);
        }

        customer = customerRepository.save(customer);
        return toDTO(customer);
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setCompany(dto.getCompany());
        customer.setJobTitle(dto.getJobTitle());
        customer.setIndustry(dto.getIndustry());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setState(dto.getState());
        customer.setCountry(dto.getCountry());
        customer.setZipCode(dto.getZipCode());
        customer.setSource(dto.getSource());
        customer.setNotes(dto.getNotes());

        if (dto.getStatus() != null) {
            customer.setStatus(CustomerStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getAssignedToId()));
            customer.setAssignedTo(user);
        }

        customer = customerRepository.save(customer);
        return toDTO(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        customerRepository.delete(customer);
    }

    private CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .company(customer.getCompany())
                .jobTitle(customer.getJobTitle())
                .industry(customer.getIndustry())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .country(customer.getCountry())
                .zipCode(customer.getZipCode())
                .status(customer.getStatus().name())
                .source(customer.getSource())
                .notes(customer.getNotes())
                .assignedToId(customer.getAssignedTo() != null ? customer.getAssignedTo().getId() : null)
                .assignedToName(customer.getAssignedTo() != null ? customer.getAssignedTo().getFullName() : null)
                .contactCount(customer.getContacts() != null ? customer.getContacts().size() : 0)
                .dealCount(customer.getDeals() != null ? customer.getDeals().size() : 0)
                .createdAt(customer.getCreatedAt() != null ? customer.getCreatedAt().toString() : null)
                .updatedAt(customer.getUpdatedAt() != null ? customer.getUpdatedAt().toString() : null)
                .build();
    }

    private Customer toEntity(CustomerDTO dto) {
        return Customer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .company(dto.getCompany())
                .jobTitle(dto.getJobTitle())
                .industry(dto.getIndustry())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .zipCode(dto.getZipCode())
                .status(dto.getStatus() != null ? CustomerStatus.valueOf(dto.getStatus().toUpperCase())
                        : CustomerStatus.LEAD)
                .source(dto.getSource())
                .notes(dto.getNotes())
                .build();
    }
}
