import {MeteringPoint} from "../repositories/MeteringPointsRepository.ts";
import {MeteringPointsDomain} from "../pages/dashboard/ts/MeteringPointsDomain.ts";
import {describe, it, expect, beforeAll} from 'vitest'

const mockMeteringPoints: MeteringPoint[] = [
    {
        address: "Home",
        consumptions: [
            {
                year: 2024,
                month: 1,
                totalConsumption: 100,
                consumptionCost: {
                    costPerKwh: 15,
                    costPerKwhWithVat: 20
                },
                energyPrice: {
                    centsPerKwh: 12.654,
                    centsPerKwhWithVat: 15.437947,
                    eurPerMwh: 126.5405,
                    eurPerMwhWithVat: 154.379472,
                    fromDateTime: "2024-01-01T00:00:00Z",
                    toDateTime: "2024-01-31T23:59:59.999999999Z"
                },
                entries: []
            },
            {
                year: 2024,
                month: 2,
                totalConsumption: 100,
                consumptionCost: {
                    costPerKwh: 15,
                    costPerKwhWithVat: 20
                },
                energyPrice: {
                    centsPerKwh: 12.654,
                    centsPerKwhWithVat: 15.437947,
                    eurPerMwh: 126.5405,
                    eurPerMwhWithVat: 154.379472,
                    fromDateTime: "2024-01-01T00:00:00Z",
                    toDateTime: "2024-01-31T23:59:59.999999999Z"
                },
                entries: []
            },
        ],
    },
    {
        address: "Home2",
        consumptions: [
            {
                year: 2024,
                month: 5,
                totalConsumption: 100,
                consumptionCost: {
                    costPerKwh: 15,
                    costPerKwhWithVat: 20
                },
                energyPrice: {
                    centsPerKwh: 12.654,
                    centsPerKwhWithVat: 15.437947,
                    eurPerMwh: 126.5405,
                    eurPerMwhWithVat: 154.379472,
                    fromDateTime: "2024-01-01T00:00:00Z",
                    toDateTime: "2024-01-31T23:59:59.999999999Z"
                },
                entries: []
            }
        ],
    }
];

describe("MeteringPointsDomain", () => {
    let domain: MeteringPointsDomain;

    beforeAll(() => {
        domain = new MeteringPointsDomain(mockMeteringPoints);
    });

    it("Calculate sum of total energy consumption over all metering points", () => {
        expect(domain.totalConsumption()).toBe(300);
    });

    it("Calculate sum of total energy price over all metering points", () => {
        expect(domain.totalPrice()).toBe("45.00");
    });

    it("Return total consumption for given metering point", () => {
        expect(domain.totalConsumptionFor("Home")).toBe(200);
    });

    it("Return price for given metering point", () => {
        expect(domain.totalPriceFor("Home2")).toBe("15.00");
    });

    it("Return 0 if address is not found", () => {
        expect(domain.totalConsumptionFor("Unknown")).toBe(0);
    });
});
