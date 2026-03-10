package com.crm.service;

import com.crm.dto.DealDTO;
import com.crm.entity.Customer;
import com.crm.entity.Deal;
import com.crm.entity.User;
import com.crm.enums.DealStage;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.CustomerRepository;
import com.crm.repository.DealRepository;
import com.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class DealService {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<DealDTO> getAllDeals(Pageable pageable) {
        return dealRepository.findAll(pageable).map(this::toDTO);
    }

    public DealDTO getDealById(Long id) {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", id));
        return toDTO(deal);
    }

    public Page<DealDTO> getDealsByCustomer(Long customerId, Pageable pageable) {
        return dealRepository.findByCustomerId(customerId, pageable).map(this::toDTO);
    }

    public Page<DealDTO> getDealsByStage(String stage, Pageable pageable) {
        DealStage dealStage = DealStage.valueOf(stage.toUpperCase());
        return dealRepository.findByStage(dealStage, pageable).map(this::toDTO);
    }

    public Page<DealDTO> getDealsByAssignee(Long userId, Pageable pageable) {
        return dealRepository.findByAssignedToId(userId, pageable).map(this::toDTO);
    }

    public Page<DealDTO> searchDeals(String search, Pageable pageable) {
        return dealRepository.searchDeals(search, pageable).map(this::toDTO);
    }

    public DealDTO createDeal(DealDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", dto.getCustomerId()));

        Deal deal = toEntity(dto);
        deal.setCustomer(customer);

        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getAssignedToId()));
            deal.setAssignedTo(user);
        }

        deal = dealRepository.save(deal);
        return toDTO(deal);
    }

    public DealDTO updateDeal(Long id, DealDTO dto) {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", id));

        deal.setTitle(dto.getTitle());
        deal.setDescription(dto.getDescription());
        deal.setValue(dto.getValue());
        deal.setSource(dto.getSource());
        deal.setNotes(dto.getNotes());

        if (dto.getStage() != null) {
            DealStage newStage = DealStage.valueOf(dto.getStage().toUpperCase());
            deal.setStage(newStage);
            if (newStage == DealStage.CLOSED_WON || newStage == DealStage.CLOSED_LOST) {
                deal.setActualCloseDate(LocalDate.now());
            }
        }
        if (dto.getProbability() != null) {
            deal.setProbability(dto.getProbability());
        }
        if (dto.getExpectedCloseDate() != null) {
            deal.setExpectedCloseDate(LocalDate.parse(dto.getExpectedCloseDate()));
        }
        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getAssignedToId()));
            deal.setAssignedTo(user);
        }

        deal = dealRepository.save(deal);
        return toDTO(deal);
    }

    public void deleteDeal(Long id) {
        Deal deal = dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal", "id", id));
        dealRepository.delete(deal);
    }

    private DealDTO toDTO(Deal deal) {
        return DealDTO.builder()
                .id(deal.getId())
                .title(deal.getTitle())
                .description(deal.getDescription())
                .value(deal.getValue())
                .stage(deal.getStage().name())
                .probability(deal.getProbability())
                .expectedCloseDate(deal.getExpectedCloseDate() != null ? deal.getExpectedCloseDate().toString() : null)
                .actualCloseDate(deal.getActualCloseDate() != null ? deal.getActualCloseDate().toString() : null)
                .source(deal.getSource())
                .notes(deal.getNotes())
                .customerId(deal.getCustomer().getId())
                .customerName(deal.getCustomer().getFullName())
                .assignedToId(deal.getAssignedTo() != null ? deal.getAssignedTo().getId() : null)
                .assignedToName(deal.getAssignedTo() != null ? deal.getAssignedTo().getFullName() : null)
                .createdAt(deal.getCreatedAt() != null ? deal.getCreatedAt().toString() : null)
                .updatedAt(deal.getUpdatedAt() != null ? deal.getUpdatedAt().toString() : null)
                .build();
    }

    private Deal toEntity(DealDTO dto) {
        Deal.DealBuilder builder = Deal.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .value(dto.getValue())
                .stage(dto.getStage() != null ? DealStage.valueOf(dto.getStage().toUpperCase())
                        : DealStage.QUALIFICATION)
                .probability(dto.getProbability() != null ? dto.getProbability() : 10)
                .source(dto.getSource())
                .notes(dto.getNotes());

        if (dto.getExpectedCloseDate() != null) {
            builder.expectedCloseDate(LocalDate.parse(dto.getExpectedCloseDate()));
        }

        return builder.build();
    }
}
