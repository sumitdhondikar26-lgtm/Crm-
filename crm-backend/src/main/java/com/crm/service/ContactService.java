package com.crm.service;

import com.crm.dto.ContactDTO;
import com.crm.entity.Contact;
import com.crm.entity.Customer;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.ContactRepository;
import com.crm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Page<ContactDTO> getContactsByCustomer(Long customerId, Pageable pageable) {
        return contactRepository.findByCustomerId(customerId, pageable).map(this::toDTO);
    }

    public ContactDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        return toDTO(contact);
    }

    public ContactDTO createContact(ContactDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", dto.getCustomerId()));

        Contact contact = toEntity(dto);
        contact.setCustomer(customer);
        contact = contactRepository.save(contact);
        return toDTO(contact);
    }

    public ContactDTO updateContact(Long id, ContactDTO dto) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));

        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setEmail(dto.getEmail());
        contact.setPhone(dto.getPhone());
        contact.setMobilePhone(dto.getMobilePhone());
        contact.setJobTitle(dto.getJobTitle());
        contact.setDepartment(dto.getDepartment());
        contact.setPrimary(dto.isPrimary());
        contact.setNotes(dto.getNotes());

        contact = contactRepository.save(contact);
        return toDTO(contact);
    }

    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        contactRepository.delete(contact);
    }

    private ContactDTO toDTO(Contact contact) {
        return ContactDTO.builder()
                .id(contact.getId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .mobilePhone(contact.getMobilePhone())
                .jobTitle(contact.getJobTitle())
                .department(contact.getDepartment())
                .primary(contact.isPrimary())
                .notes(contact.getNotes())
                .customerId(contact.getCustomer().getId())
                .customerName(contact.getCustomer().getFullName())
                .createdAt(contact.getCreatedAt() != null ? contact.getCreatedAt().toString() : null)
                .updatedAt(contact.getUpdatedAt() != null ? contact.getUpdatedAt().toString() : null)
                .build();
    }

    private Contact toEntity(ContactDTO dto) {
        return Contact.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .mobilePhone(dto.getMobilePhone())
                .jobTitle(dto.getJobTitle())
                .department(dto.getDepartment())
                .primary(dto.isPrimary())
                .notes(dto.getNotes())
                .build();
    }
}
