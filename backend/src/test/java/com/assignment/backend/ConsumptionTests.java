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

    private MeteringPointEntity testMeteringPoint;
    private List<EnergyPriceDto> mockEnergyPrices;

    @BeforeEach
    void setUp() {
        CustomerEntity testCustomer = new CustomerEntity();
        testMeteringPoint = spy(new MeteringPointEntity());
        testCustomer.setUsername("testUser");
        testMeteringPoint.setAddress("Test Address");

        mockEnergyPrices = List.of(
                new EnergyPriceDto(10.0, 15.0, 100.0, 120.0,
                        ZonedDateTime.parse("2024-01-01T00:00:00Z"),
                        ZonedDateTime.parse("2024-01-31T23:59:59.999999999Z")),

                new EnergyPriceDto(5.0, 10.0, 85.0, 102.0,
                        ZonedDateTime.parse("2024-02-01T00:00:00Z"),
                        ZonedDateTime.parse("2024-02-29T23:59:59.999999999Z"))
        );

        when(customerRepository.findByUsername("testUser")).thenReturn(Optional.of(testCustomer));
        when(meteringPointRepository.findAllByCustomer(testCustomer)).thenReturn(List.of(testMeteringPoint));
        when(energyPriceService.getEnergyPrices()).thenReturn(mockEnergyPrices);
    }

    @DisplayName("Aggregate each month consumption correctly")
    @Test
    void test01() {
        List<ConsumptionEntity> mockConsumptions = List.of(
                new ConsumptionEntity(200, "kWh", ZonedDateTime.of(2024, 1, 4, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint),
                new ConsumptionEntity(150, "kWh", ZonedDateTime.of(2024, 1, 5, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint),
                new ConsumptionEntity(300, "kWh", ZonedDateTime.of(2024, 2, 6, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint),
                new ConsumptionEntity(400, "kWh", ZonedDateTime.of(2024, 2, 6, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint)
        );

        when(testMeteringPoint.getConsumptions()).thenReturn(mockConsumptions);
        List<MeteringPointConsumption> result = consumptionService.getMeteringPointsConsumption("testUser");

        // since we only have one metering point assert that it exists
        assertNotNull(result);
        assertEquals(1, result.size());

        MeteringPointConsumption meteringPointConsumption = result.getFirst();
        assertEquals("Test Address", meteringPointConsumption.address());

        List<MonthlyConsumption> monthlyConsumptions = meteringPointConsumption.consumptions();
        assertEquals(2, monthlyConsumptions.size());

        // verify that January consumption is correct
        MonthlyConsumption januaryConsumption = monthlyConsumptions.stream()
                .filter(mc -> mc.month() == 1)
                .findFirst().orElse(null);
        assertNotNull(januaryConsumption);
        assertEquals(350, januaryConsumption.totalConsumption()); // 200 + 150
        assertEquals(mockEnergyPrices.getFirst(), januaryConsumption.energyPrice());

        // verify that February consumption is correct
        MonthlyConsumption febConsumption = monthlyConsumptions.stream()
                .filter(mc -> mc.month() == 2)
                .findFirst().orElse(null);
        assertNotNull(febConsumption);
        assertEquals(700, febConsumption.totalConsumption());
        assertEquals(mockEnergyPrices.get(1), febConsumption.energyPrice());
    }

    @DisplayName("Validate that consumption costs are calculated correctly")
    @Test
    void test02() {
        List<ConsumptionEntity> mockConsumptions = List.of(
                new ConsumptionEntity(200, "kWh", ZonedDateTime.of(2024, 1, 4, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint),
                new ConsumptionEntity(150, "kWh", ZonedDateTime.of(2024, 1, 5, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint),
                new ConsumptionEntity(500, "kWh", ZonedDateTime.of(2024, 2, 6, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint),
                new ConsumptionEntity(1000, "kWh", ZonedDateTime.of(2024, 2, 6, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint)
        );

        when(testMeteringPoint.getConsumptions()).thenReturn(mockConsumptions);
        List<MeteringPointConsumption> result = consumptionService.getMeteringPointsConsumption("testUser");

        MeteringPointConsumption meteringPointConsumption = result.getFirst();
        List<MonthlyConsumption> monthlyConsumptions = meteringPointConsumption.consumptions();

        // verify that January consumption cost is correct
        MonthlyConsumption januaryConsumption = monthlyConsumptions.stream()
                .filter(mc -> mc.month() == 1)
                .findFirst().orElse(null);
        assertNotNull(januaryConsumption);
        assertEquals(35.0, januaryConsumption.consumptionCost().costPerKwh());
        assertEquals(52.5, januaryConsumption.consumptionCost().costPerKwhWithVat());

        // verify that February consumption cost is correct
        MonthlyConsumption febConsumption = monthlyConsumptions.stream()
                .filter(mc -> mc.month() == 2)
                .findFirst().orElse(null);

        assertNotNull(febConsumption);
        assertEquals(75, febConsumption.consumptionCost().costPerKwh()); // (5/100) * 1500
        assertEquals(150, febConsumption.consumptionCost().costPerKwhWithVat()); // (10/100) * 1500
    }

    @Test
    @DisplayName("Handle missing energy prices gracefully")
    void test03() {
        List<ConsumptionEntity> mockConsumptions = List.of(
                new ConsumptionEntity(400, "kWh", ZonedDateTime.of(2024, 3, 6, 12, 0, 0, 0, ZoneId.systemDefault()), testMeteringPoint)
        );

        when(testMeteringPoint.getConsumptions()).thenReturn(mockConsumptions);

        List<MeteringPointConsumption> result = consumptionService.getMeteringPointsConsumption("testUser");

        assertNotNull(result);
        assertEquals(1, result.size());

        MeteringPointConsumption meteringPointConsumption = result.getFirst();
        assertEquals("Test Address", meteringPointConsumption.address());

        List<MonthlyConsumption> monthlyConsumptions = meteringPointConsumption.consumptions();
        assertEquals(1, monthlyConsumptions.size());

        // verify that consumption is correct
        MonthlyConsumption marchConsumption = monthlyConsumptions.getFirst();
        assertNotNull(marchConsumption);
        assertEquals(400, marchConsumption.totalConsumption()); // 200 + 150
        assertNull(marchConsumption.energyPrice());
    }
}
