package com.crm.config;

import com.crm.entity.*;
import com.crm.enums.*;
import com.crm.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            logger.info("Database already seeded. Skipping...");
            return;
        }

        logger.info("Seeding database with sample data...");

        // --- Users ---
        User admin = userRepository.save(User.builder()
                .username("admin")
                .email("admin@crm.com")
                .password(passwordEncoder.encode("admin123"))
                .firstName("System")
                .lastName("Admin")
                .phone("+1-555-0100")
                .role(Role.ROLE_ADMIN)
                .active(true)
                .build());

        User manager = userRepository.save(User.builder()
                .username("jmanager")
                .email("jane.manager@crm.com")
                .password(passwordEncoder.encode("manager123"))
                .firstName("Jane")
                .lastName("Smith")
                .phone("+1-555-0101")
                .role(Role.ROLE_MANAGER)
                .active(true)
                .build());

        User salesRep1 = userRepository.save(User.builder()
                .username("bsalesrep")
                .email("bob.sales@crm.com")
                .password(passwordEncoder.encode("sales123"))
                .firstName("Bob")
                .lastName("Johnson")
                .phone("+1-555-0102")
                .role(Role.ROLE_SALES_REP)
                .active(true)
                .build());

        User salesRep2 = userRepository.save(User.builder()
                .username("asalesrep")
                .email("alice.sales@crm.com")
                .password(passwordEncoder.encode("sales123"))
                .firstName("Alice")
                .lastName("Williams")
                .phone("+1-555-0103")
                .role(Role.ROLE_SALES_REP)
                .active(true)
                .build());

        // --- Customers ---
        Customer cust1 = customerRepository.save(Customer.builder()
                .firstName("Michael")
                .lastName("Chen")
                .email("michael.chen@techcorp.com")
                .phone("+1-555-1001")
                .company("TechCorp Solutions")
                .jobTitle("CTO")
                .industry("Technology")
                .address("123 Innovation Drive")
                .city("San Francisco")
                .state("CA")
                .country("USA")
                .zipCode("94105")
                .status(CustomerStatus.ACTIVE)
                .source("Website")
                .notes("Key enterprise customer, interested in premium plan")
                .assignedTo(salesRep1)
                .build());

        Customer cust2 = customerRepository.save(Customer.builder()
                .firstName("Sarah")
                .lastName("Davis")
                .email("sarah.davis@globalretail.com")
                .phone("+1-555-1002")
                .company("Global Retail Inc.")
                .jobTitle("VP of Operations")
                .industry("Retail")
                .address("456 Commerce Blvd")
                .city("New York")
                .state("NY")
                .country("USA")
                .zipCode("10001")
                .status(CustomerStatus.PROSPECT)
                .source("Referral")
                .notes("Referred by TechCorp, exploring CRM solutions")
                .assignedTo(salesRep1)
                .build());

        Customer cust3 = customerRepository.save(Customer.builder()
                .firstName("David")
                .lastName("Park")
                .email("david.park@healthplus.com")
                .phone("+1-555-1003")
                .company("HealthPlus Systems")
                .jobTitle("Director of IT")
                .industry("Healthcare")
                .address("789 Medical Center Way")
                .city("Boston")
                .state("MA")
                .country("USA")
                .zipCode("02101")
                .status(CustomerStatus.LEAD)
                .source("Trade Show")
                .assignedTo(salesRep2)
                .build());

        Customer cust4 = customerRepository.save(Customer.builder()
                .firstName("Emily")
                .lastName("Rodriguez")
                .email("emily.r@financegroup.com")
                .phone("+1-555-1004")
                .company("Finance Group LLC")
                .jobTitle("CFO")
                .industry("Finance")
                .address("321 Wall Street")
                .city("New York")
                .state("NY")
                .country("USA")
                .zipCode("10005")
                .status(CustomerStatus.ACTIVE)
                .source("Cold Call")
                .notes("Long-term customer, recently upgraded plan")
                .assignedTo(salesRep2)
                .build());

        Customer cust5 = customerRepository.save(Customer.builder()
                .firstName("James")
                .lastName("Wilson")
                .email("jwilson@edutech.org")
                .phone("+1-555-1005")
                .company("EduTech Foundation")
                .jobTitle("Executive Director")
                .industry("Education")
                .address("555 University Ave")
                .city("Austin")
                .state("TX")
                .country("USA")
                .zipCode("73301")
                .status(CustomerStatus.INACTIVE)
                .source("LinkedIn")
                .assignedTo(manager)
                .build());

        // --- Contacts ---
        contactRepository.save(Contact.builder()
                .firstName("Lisa")
                .lastName("Chen")
                .email("lisa.chen@techcorp.com")
                .phone("+1-555-2001")
                .jobTitle("Procurement Manager")
                .department("Procurement")
                .primary(true)
                .customer(cust1)
                .build());

        contactRepository.save(Contact.builder()
                .firstName("Tom")
                .lastName("Baker")
                .email("tom.baker@techcorp.com")
                .phone("+1-555-2002")
                .jobTitle("IT Manager")
                .department("IT")
                .primary(false)
                .customer(cust1)
                .build());

        contactRepository.save(Contact.builder()
                .firstName("Amanda")
                .lastName("Foster")
                .email("amanda.f@globalretail.com")
                .phone("+1-555-2003")
                .jobTitle("Purchasing Director")
                .department("Purchasing")
                .primary(true)
                .customer(cust2)
                .build());

        contactRepository.save(Contact.builder()
                .firstName("Kevin")
                .lastName("Park")
                .email("kevin.park@healthplus.com")
                .phone("+1-555-2004")
                .jobTitle("Systems Analyst")
                .department("IT")
                .primary(true)
                .customer(cust3)
                .build());

        // --- Deals ---
        dealRepository.save(Deal.builder()
                .title("TechCorp Enterprise License")
                .description("Annual enterprise license renewal with premium support")
                .value(new BigDecimal("150000.00"))
                .stage(DealStage.NEGOTIATION)
                .probability(75)
                .expectedCloseDate(LocalDate.now().plusMonths(1))
                .source("Upsell")
                .customer(cust1)
                .assignedTo(salesRep1)
                .build());

        dealRepository.save(Deal.builder()
                .title("Global Retail CRM Migration")
                .description("Full CRM migration from legacy system")
                .value(new BigDecimal("250000.00"))
                .stage(DealStage.PROPOSAL)
                .probability(50)
                .expectedCloseDate(LocalDate.now().plusMonths(3))
                .source("Referral")
                .customer(cust2)
                .assignedTo(salesRep1)
                .build());

        dealRepository.save(Deal.builder()
                .title("HealthPlus Initial Setup")
                .description("CRM platform initial deployment and training")
                .value(new BigDecimal("75000.00"))
                .stage(DealStage.QUALIFICATION)
                .probability(20)
                .expectedCloseDate(LocalDate.now().plusMonths(6))
                .source("Trade Show")
                .customer(cust3)
                .assignedTo(salesRep2)
                .build());

        dealRepository.save(Deal.builder()
                .title("Finance Group Platform Upgrade")
                .description("Platform upgrade from Standard to Enterprise tier")
                .value(new BigDecimal("95000.00"))
                .stage(DealStage.CLOSED_WON)
                .probability(100)
                .expectedCloseDate(LocalDate.now().minusWeeks(2))
                .actualCloseDate(LocalDate.now().minusDays(5))
                .source("Existing Customer")
                .customer(cust4)
                .assignedTo(salesRep2)
                .build());

        dealRepository.save(Deal.builder()
                .title("EduTech Annual Subscription")
                .description("First year subscription with onboarding")
                .value(new BigDecimal("35000.00"))
                .stage(DealStage.CLOSED_LOST)
                .probability(0)
                .expectedCloseDate(LocalDate.now().minusMonths(1))
                .actualCloseDate(LocalDate.now().minusWeeks(3))
                .source("LinkedIn")
                .notes("Lost to competitor - pricing concern")
                .customer(cust5)
                .assignedTo(manager)
                .build());

        // --- Activities ---
        activityRepository.save(Activity.builder()
                .type(ActivityType.CALL)
                .subject("Initial discovery call with TechCorp")
                .description("Discussed requirements and timeline for enterprise license renewal")
                .activityDate(LocalDateTime.now().minusDays(7))
                .durationMinutes(45)
                .outcome("Positive - moving to negotiation phase")
                .customer(cust1)
                .performedBy(salesRep1)
                .build());

        activityRepository.save(Activity.builder()
                .type(ActivityType.EMAIL)
                .subject("Sent proposal to Global Retail")
                .description("Delivered migration proposal with detailed timeline and pricing")
                .activityDate(LocalDateTime.now().minusDays(3))
                .durationMinutes(15)
                .outcome("Awaiting response")
                .customer(cust2)
                .performedBy(salesRep1)
                .build());

        activityRepository.save(Activity.builder()
                .type(ActivityType.MEETING)
                .subject("Product demo for HealthPlus")
                .description("Virtual product demonstration covering core CRM features")
                .activityDate(LocalDateTime.now().minusDays(1))
                .durationMinutes(60)
                .outcome("Interested - requested detailed pricing")
                .customer(cust3)
                .performedBy(salesRep2)
                .build());

        activityRepository.save(Activity.builder()
                .type(ActivityType.NOTE)
                .subject("Finance Group upgrade completed")
                .description("Successfully completed platform upgrade. Customer is satisfied.")
                .activityDate(LocalDateTime.now().minusDays(5))
                .customer(cust4)
                .performedBy(salesRep2)
                .build());

        activityRepository.save(Activity.builder()
                .type(ActivityType.CALL)
                .subject("Follow-up with EduTech")
                .description("Attempted follow-up about renewal - budget constraints this year")
                .activityDate(LocalDateTime.now().minusDays(14))
                .durationMinutes(20)
                .outcome("No budget currently - revisit next quarter")
                .customer(cust5)
                .performedBy(manager)
                .build());

        // --- Tasks ---
        taskRepository.save(Task.builder()
                .title("Prepare contract for TechCorp renewal")
                .description("Draft enterprise license renewal contract with updated terms")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .dueDate(LocalDate.now().plusDays(5))
                .customer(cust1)
                .assignedTo(salesRep1)
                .build());

        taskRepository.save(Task.builder()
                .title("Schedule demo for Global Retail stakeholders")
                .description("Coordinate demo session with VP and IT team")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .dueDate(LocalDate.now().plusDays(10))
                .customer(cust2)
                .assignedTo(salesRep1)
                .build());

        taskRepository.save(Task.builder()
                .title("Send pricing proposal to HealthPlus")
                .description("Create customized pricing proposal based on demo feedback")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .dueDate(LocalDate.now().plusDays(3))
                .customer(cust3)
                .assignedTo(salesRep2)
                .build());

        taskRepository.save(Task.builder()
                .title("Collect feedback from Finance Group")
                .description("Send post-upgrade satisfaction survey")
                .status(TaskStatus.COMPLETED)
                .priority(TaskPriority.LOW)
                .dueDate(LocalDate.now().minusDays(2))
                .completedDate(LocalDate.now().minusDays(1))
                .customer(cust4)
                .assignedTo(salesRep2)
                .build());

        taskRepository.save(Task.builder()
                .title("Quarterly review of inactive accounts")
                .description("Review all inactive customers for re-engagement opportunities")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .dueDate(LocalDate.now().minusDays(5))
                .assignedTo(manager)
                .build());

        logger.info(
                "Database seeding complete! Created {} users, {} customers, {} contacts, {} deals, {} activities, {} tasks",
                userRepository.count(), customerRepository.count(), contactRepository.count(),
                dealRepository.count(), activityRepository.count(), taskRepository.count());
    }
}
