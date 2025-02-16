export interface BarChartEntry {
    [key: string]: string | number
    month: string
}

export interface BarChartSeries {
    name: string
    color: string
}

export interface Chart {
    result: BarChartEntry[],
    series: BarChartSeries[]
}
