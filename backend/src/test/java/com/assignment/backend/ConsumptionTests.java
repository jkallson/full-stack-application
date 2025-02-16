package com.assignment.backend;

import com.assignment.backend.dtos.EnergyPriceDto;
import com.assignment.backend.entities.ConsumptionEntity;
import com.assignment.backend.entities.CustomerEntity;
import com.assignment.backend.entities.MeteringPointEntity;
import com.assignment.backend.logic.EnergyPriceService;
import com.assignment.backend.logic.consumption.ConsumptionService;
import com.assignment.backend.repositories.CustomerRepository;
import com.assignment.backend.repositories.MeteringPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.assignment.backend.logic.consumption.ConsumptionService.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsumptionTests {
    @Mock
    private MeteringPointRepository meteringPointRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EnergyPriceService energyPriceService;

    @InjectMocks
    private ConsumptionService consumptionService;

    private static final String TEST_USER = "testUser";
    private static final String TEST_ADDRESS = "Test Address";

    private MeteringPointEntity testMeteringPoint;
    private List<EnergyPriceDto> mockEnergyPrices;

    @BeforeEach
    void setUp() {
        CustomerEntity testCustomer = new CustomerEntity();
        testMeteringPoint = spy(new MeteringPointEntity());
        testCustomer.setUsername(TEST_USER);
        testMeteringPoint.setAddress(TEST_ADDRESS);

        mockEnergyPrices = List.of(
                new EnergyPriceDto(10.0, 15.0, 100.0, 120.0,
                        ZonedDateTime.parse("2024-01-01T00:00:00Z"),
                        ZonedDateTime.parse("2024-01-31T23:59:59.999999999Z")),

                new EnergyPriceDto(5.0, 10.0, 85.0, 102.0,
                        ZonedDateTime.parse("2024-02-01T00:00:00Z"),
                        ZonedDateTime.parse("2024-02-29T23:59:59.999999999Z"))
        );

        when(customerRepository.findByUsername(TEST_USER)).thenReturn(Optional.of(testCustomer));
        when(meteringPointRepository.findAllByCustomer(testCustomer)).thenReturn(List.of(testMeteringPoint));
        when(energyPriceService.getEnergyPrices(TEST_USER)).thenReturn(mockEnergyPrices);
    }

    @DisplayName("Aggregate each month consumption correctly")
    @Test
    void test01() {
        List<ConsumptionEntity> mockConsumptions = List.of(
                createConsumption(200, 1, 4),
                createConsumption(150, 1, 5),
                createConsumption(300, 2, 6),
                createConsumption(400, 2, 8)
        );

        when(testMeteringPoint.getConsumptions()).thenReturn(mockConsumptions);
        List<MeteringPointConsumption> result = consumptionService.getMeteringPointsConsumption(TEST_USER);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(TEST_ADDRESS, result.getFirst().address()),
                () -> assertEquals(2, result.getFirst().consumptions().size())
        );

        MonthlyConsumption janConsumption = result.getFirst().consumptions().stream()
                .filter(mc -> mc.month() == 1)
                .findFirst().orElse(null);

        MonthlyConsumption febConsumption = result.getFirst().consumptions().stream()
                .filter(mc -> mc.month() == 2)
                .findFirst().orElse(null);

        assertAll(
                () -> assertNotNull(janConsumption),
                () -> assertEquals(350, janConsumption.totalConsumption()), // 200 + 150
                () -> assertEquals(mockEnergyPrices.getFirst(), janConsumption.energyPrice()),

                () -> assertNotNull(febConsumption),
                () -> assertEquals(700, febConsumption.totalConsumption()), // 300 + 400
                () -> assertEquals(mockEnergyPrices.get(1), febConsumption.energyPrice())
        );
    }

    @DisplayName("Validate that consumption costs are calculated correctly")
    @Test
    void test02() {
        List<ConsumptionEntity> mockConsumptions = List.of(
                createConsumption(200, 1, 4),
                createConsumption(150, 1, 5),
                createConsumption(500, 2, 6),
                createConsumption(1000, 2, 7)
        );

        when(testMeteringPoint.getConsumptions()).thenReturn(mockConsumptions);
        List<MeteringPointConsumption> result = consumptionService.getMeteringPointsConsumption(TEST_USER);

        MonthlyConsumption januaryConsumption = result.getFirst().consumptions().stream()
                .filter(mc -> mc.month() == 1)
                .findFirst().orElse(null);

        MonthlyConsumption febConsumption = result.getFirst().consumptions().stream()
                .filter(mc -> mc.month() == 2)
                .findFirst().orElse(null);

        assertAll(
                () -> assertNotNull(januaryConsumption),
                () -> assertEquals(35.0, januaryConsumption.consumptionCost().costPerKwh()),
                () -> assertEquals(52.5, januaryConsumption.consumptionCost().costPerKwhWithVat()),

                () -> assertNotNull(febConsumption),
                () -> assertEquals(75, febConsumption.consumptionCost().costPerKwh()), // (5/100) * 1500
                () -> assertEquals(150, febConsumption.consumptionCost().costPerKwhWithVat()) // (10/100) * 1500
        );
    }

    @Test
    @DisplayName("Handle missing energy prices gracefully")
    void test03() {
        List<ConsumptionEntity> mockConsumptions = List.of(
                createConsumption(400, 3, 6)
        );

        when(testMeteringPoint.getConsumptions()).thenReturn(mockConsumptions);

        List<MeteringPointConsumption> result = consumptionService.getMeteringPointsConsumption(TEST_USER);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(TEST_ADDRESS, result.getFirst().address()),
                () -> assertEquals(1, result.getFirst().consumptions().size())
        );

        // verify that consumption is correct
        MonthlyConsumption marchConsumption = result.getFirst().consumptions().getFirst();

        assertAll(
                () -> assertNotNull(marchConsumption),
                () -> assertEquals(400, marchConsumption.totalConsumption()),
                () -> assertNull(marchConsumption.energyPrice())
        );
    }

    private ConsumptionEntity createConsumption (Integer amount, Integer month, Integer day) {
        return new ConsumptionEntity(amount, "kWh", ZonedDateTime.of(2024, month, day, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint);
    }
}
