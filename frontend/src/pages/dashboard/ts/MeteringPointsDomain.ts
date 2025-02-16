import {MeteringPoint} from "../../../repositories/MeteringPointsRepository.ts";

enum Months {
    "Jan" = 1,
    "Feb" = 2,
    "Mar" = 3,
    "Apr" = 4,
    "May" = 5,
    "Jun" = 6,
    "Jul" = 7,
    "Aug" = 8,
    "Sep" = 9,
    "Oct" = 10,
    "Nov" = 11,
    "Dec" = 12,
}

export class MeteringPointsDomain {
    private readonly meteringPoints: MeteringPoint[] = []
    private readonly monthNames: string[] = ["Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

    constructor(meteringPoints: MeteringPoint[]) {
        this.meteringPoints = meteringPoints
    }

    public getTotalAmount(): number {
        return this.meteringPoints
            .flatMap(it => it.consumptions)
            .map(it => it.totalConsumption)
            .reduce((sum, val) => sum + val)
    }

    public getTotalPrice(): string {
        return this.meteringPoints
            .flatMap(it => it.consumptions)
            .map(it => it.totalConsumption * (it.energyPrice.centsPerKwh / 100))
            .reduce((sum, val) => sum + val)
            .toFixed(2)
    }

    public generatePerMonthConsumption (): any {
        let result: any[] = []
        this.monthNames.forEach((name: string) => {
            const aggregated: { [key: string]: number } & { month?: string } = {};
            aggregated.month = name
            this.meteringPoints.forEach(meteringPoint => {
                const consumptionForMonth = meteringPoint.consumptions.find(it => it.month === Months[name])

                if (consumptionForMonth === undefined) {
                    return
                }
                aggregated[meteringPoint.address] = consumptionForMonth.totalConsumption
            })
            result.push(aggregated)
        });

        const series = this.meteringPoints.map((it, index) => ({
            name: it.address,
            color: index % 2 ? 'blue.6' : 'teal.6'
        }))

        return {
            result: result,
            series: series
        }
    }

    public generatePerMonthPrices (): any {
        let result: any[] = []
        this.monthNames.forEach((name: string) => {
            const aggregated: { [key: string]: number } & { month?: string } = {};
            aggregated.month = name
            this.meteringPoints.forEach(meteringPoint => {
                const consumptionForMonth = meteringPoint.consumptions.find(it => it.month === Months[name])

                if (consumptionForMonth === undefined) {
                    return
                }
                aggregated[meteringPoint.address] = (consumptionForMonth.totalConsumption * (consumptionForMonth.energyPrice.centsPerKwh / 100)).toFixed(2)
            })
            result.push(aggregated)
        });

        const series = this.meteringPoints.map((it, index) => ({
            name: it.address,
            color: index % 2 ? 'blue.6' : 'teal.6'
        }))

        return {
            result: result,
            series: series
        }
    }
}
