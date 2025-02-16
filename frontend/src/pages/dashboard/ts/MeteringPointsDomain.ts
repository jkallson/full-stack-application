import {MeteringPoint} from "../../../repositories/MeteringPointsRepository.ts";

export class MeteringPointsDomain {
    private readonly meteringPoints: MeteringPoint[] = []

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

}
