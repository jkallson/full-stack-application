import {BarChart} from "@mantine/charts";
import {BarChartEntry, BarChartSeries} from "../../ts/BarChartInterfaces.ts";
import {Loader} from "@mantine/core";

export interface DashboardConsumptionBarChartProps {
    data: BarChartEntry[]
    series: BarChartSeries[],
    loading: boolean
}

export function DashboardConsumptionBarChart({data, series, loading}: DashboardConsumptionBarChartProps) {
    return (
        <>
            {loading
                ? <Loader h={300} className={"flex full-width align-center justify-center"}></Loader>
                : <BarChart
                    h={300}
                    data={data}
                    withLegend={true}
                    dataKey="month"
                    valueFormatter={(value) => value + 'kwH'}
                    type="stacked"
                    series={series}
                />
            }
        </>
    )
}
