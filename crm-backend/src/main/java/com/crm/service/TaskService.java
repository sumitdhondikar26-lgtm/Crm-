package com.crm.service;

import com.crm.dto.TaskDTO;
import com.crm.entity.Customer;
import com.crm.entity.Task;
import com.crm.entity.User;
import com.crm.enums.TaskPriority;
import com.crm.enums.TaskStatus;
import com.crm.exception.ResourceNotFoundException;
import com.crm.repository.CustomerRepository;
import com.crm.repository.TaskRepository;
import com.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<TaskDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::toDTO);
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        return toDTO(task);
    }

    public Page<TaskDTO> getTasksByCustomer(Long customerId, Pageable pageable) {
        return taskRepository.findByCustomerId(customerId, pageable).map(this::toDTO);
    }

    public Page<TaskDTO> getTasksByAssignee(Long userId, Pageable pageable) {
        return taskRepository.findByAssignedToId(userId, pageable).map(this::toDTO);
    }

    public Page<TaskDTO> getTasksByStatus(String status, Pageable pageable) {
        TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
        return taskRepository.findByStatus(taskStatus, pageable).map(this::toDTO);
    }

    public TaskDTO createTask(TaskDTO dto) {
        Task task = toEntity(dto);

        if (dto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", dto.getCustomerId()));
            task.setCustomer(customer);
        }
        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getAssignedToId()));
            task.setAssignedTo(user);
        }

        task = taskRepository.save(task);
        return toDTO(task);
    }

    public TaskDTO updateTask(Long id, TaskDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());

        if (dto.getStatus() != null) {
            TaskStatus newStatus = TaskStatus.valueOf(dto.getStatus().toUpperCase());
            task.setStatus(newStatus);
            if (newStatus == TaskStatus.COMPLETED) {
                task.setCompletedDate(LocalDate.now());
            }
        }
        if (dto.getPriority() != null) {
            task.setPriority(TaskPriority.valueOf(dto.getPriority().toUpperCase()));
        }
        if (dto.getDueDate() != null) {
            task.setDueDate(LocalDate.parse(dto.getDueDate()));
        }
        if (dto.getAssignedToId() != null) {
            User user = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", dto.getAssignedToId()));
            task.setAssignedTo(user);
        }

        task = taskRepository.save(task);
        return toDTO(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        taskRepository.delete(task);
    }

    private TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .dueDate(task.getDueDate() != null ? task.getDueDate().toString() : null)
                .completedDate(task.getCompletedDate() != null ? task.getCompletedDate().toString() : null)
                .customerId(task.getCustomer() != null ? task.getCustomer().getId() : null)
                .customerName(task.getCustomer() != null ? task.getCustomer().getFullName() : null)
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedToName(task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : null)
                .createdAt(task.getCreatedAt() != null ? task.getCreatedAt().toString() : null)
                .updatedAt(task.getUpdatedAt() != null ? task.getUpdatedAt().toString() : null)
                .build();
    }

    private Task toEntity(TaskDTO dto) {
        Task.TaskBuilder builder = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? TaskStatus.valueOf(dto.getStatus().toUpperCase()) : TaskStatus.TODO)
                .priority(dto.getPriority() != null ? TaskPriority.valueOf(dto.getPriority().toUpperCase())
                        : TaskPriority.MEDIUM);

        if (dto.getDueDate() != null) {
            builder.dueDate(LocalDate.parse(dto.getDueDate()));
        }

        return builder.build();
    }
}
