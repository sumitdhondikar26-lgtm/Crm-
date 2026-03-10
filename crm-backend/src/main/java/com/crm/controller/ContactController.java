package com.crm.controller;

import com.crm.dto.ContactDTO;
import com.crm.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<ContactDTO>> getContactsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contactService.getContactsByCustomer(customerId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContactById(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@Valid @RequestBody ContactDTO contactDTO) {
        return new ResponseEntity<>(contactService.createContact(contactDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.updateContact(id, contactDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}
