import {AxiosResponse} from "axios";
import axiosInstance from "../configurations/axios.ts";

export interface ConsumptionDto {
    amount: number
    unit: string
    time: string
}

export interface EnergyPrice {
    centsPerKwh: number
    centsPerKwhWithVat: number
    eurPerMwh: number
    eurPerMwhWithVat: number
    fromDateTime: string
    toDateTime: string
}

export interface MeteringPointConsumption {
    year: number
    month: number
    totalConsumption: number
    energyPrice: EnergyPrice
    entries: ConsumptionDto[]
}

export interface MeteringPoint {
    address: string
    consumptions: MeteringPointConsumption[]
}

export class MeteringPointsRepository {
    static getMeteringPoints (): Promise<MeteringPoint[]> {
        return axiosInstance.get(`/consumption/metering-points`)
            .then((response: AxiosResponse) => response.data)
    }
}
