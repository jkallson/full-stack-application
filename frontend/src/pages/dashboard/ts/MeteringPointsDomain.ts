import {MeteringPoint, MeteringPointConsumption} from "../../../repositories/MeteringPointsRepository.ts";
import {BarChartEntry, BarChartSeries, Chart} from "./BarChartInterfaces.ts";

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

    constructor(meteringPoints: MeteringPoint[]) {
        this.meteringPoints = meteringPoints
    }

    public addresses(): string[] {
        return this.meteringPoints.map(it => it.address)
    }

    public totalConsumption(): number {
        return this.meteringPoints
            .flatMap(it => it.consumptions)
            .reduce((sum, it) => sum + it.totalConsumption, 0)
    }

    public totalPrice(): string {
        return this.meteringPoints
            .flatMap(it => it.consumptions)
            .reduce((sum: number, it: MeteringPointConsumption): number => sum + it.consumptionCost.costPerKwh, 0)
            .toFixed(2)
    }

    public totalPriceFor(address: string): string {
        const meteringPoint = this.meteringPoints.find(it => it.address === address)

        if (meteringPoint === undefined) {
            return ""
        }

        return meteringPoint.consumptions
            .reduce((sum: number, val: MeteringPointConsumption): number => sum + val.consumptionCost.costPerKwh, 0)
            .toFixed(2)
    }

    public totalUsageFor(address: string): number {
        const meteringPoint = this.meteringPoints.find(it => it.address === address)

        if (meteringPoint === undefined) {
            return 0
        }

        return meteringPoint.consumptions.reduce((sum: number, val: MeteringPointConsumption): number => sum + val.totalConsumption, 0)
    }

    public averageMonthlyConsumptionFor(address: string): string {
        const meteringPoint = this.meteringPoints.find(it => it.address === address)

        if (meteringPoint === undefined) {
            return ""
        }

        const consumption: number = meteringPoint.consumptions
            .reduce((sum: number, val: MeteringPointConsumption): number => sum + val.totalConsumption, 0)

        return (consumption / meteringPoint.consumptions.length).toFixed(2)
    }

    public averageMonthlyPriceFor(address: string): string {
        const meteringPoint = this.meteringPoints.find(it => it.address === address)

        if (meteringPoint === undefined) {
            return ""
        }

        const totalPrice: number = meteringPoint.consumptions
            .reduce((sum: number, val: MeteringPointConsumption): number => sum + val.consumptionCost.costPerKwh, 0)

        return (totalPrice / meteringPoint.consumptions.length).toFixed(2)
    }

    public consumptionChartFor(address: string): Chart {
        const meteringPoint: MeteringPoint | undefined = this.meteringPoints.find(it => it.address === address);
        if (!meteringPoint) return {result: [], series: []};

        return this.generateGraphData([meteringPoint]);
    }

    public totalConsumptionChart(): Chart {
        return this.generateGraphData(this.meteringPoints);
    }

    public priceChartFor(address: string): Chart {
        const meteringPoint: MeteringPoint | undefined = this.meteringPoints.find(it => it.address === address);
        if (!meteringPoint) return {result: [], series: []};

        return this.generatePriceData([meteringPoint], 'blue.6');
    }

    public totalPriceChart(): Chart {
        return this.generatePriceData(this.meteringPoints);
    }

    private generateGraphData(meteringPoints: MeteringPoint[], color?: string): Chart {
        let result: BarChartEntry[] = [];

        Object.entries(Months).forEach(([name, month]) => {
            if (isNaN(Number(name))) {
                const aggregated: BarChartEntry = {month: name};

                meteringPoints.forEach((meteringPoint: MeteringPoint) => {
                    const consumptionForMonth: MeteringPointConsumption | undefined = meteringPoint.consumptions.find(c => c.month === month);

                    if (consumptionForMonth) {
                        aggregated[meteringPoint.address] = consumptionForMonth.totalConsumption;
                    }
                });

                result.push(aggregated);
            }
        });


        const series: BarChartSeries[] = meteringPoints.map((mp, index) => ({
            name: mp.address,
            color: color ? color : index % 2 ? 'blue.6' : 'teal.6'
        }));

        return {result, series};
    }

    private generatePriceData(meteringPoints: MeteringPoint[], color?: string): Chart {
        let result: BarChartEntry[] = [];

        Object.entries(Months).forEach(([name, month]) => {
            if (isNaN(Number(name))) {
                const aggregated: BarChartEntry = {month: name};

                meteringPoints.forEach((meteringPoint: MeteringPoint) => {
                    const consumptionForMonth: MeteringPointConsumption | undefined = meteringPoint.consumptions.find(c => c.month === month);

                    if (consumptionForMonth) {
                        aggregated[meteringPoint.address] = consumptionForMonth.consumptionCost.costPerKwh;
                    }
                });

                result.push(aggregated);
            }
        });

        const series: BarChartSeries[] = meteringPoints.map((mp, index) => ({
            name: mp.address,
            color: color ? color : index % 2 ? 'blue.6' : 'teal.6'
        }));

        return {result, series};
    }
}
