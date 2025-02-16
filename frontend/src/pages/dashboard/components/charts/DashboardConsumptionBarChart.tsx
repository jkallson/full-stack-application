import {BarChart} from "@mantine/charts";
import {BarChartEntry} from "../../ts/BarChartInterfaces.ts";

export interface DashboardConsumptionBarChartProps {
    data: BarChartEntry[]
    series: any
}

export function DashboardConsumptionBarChart ({ data, series }: DashboardConsumptionBarChartProps) {
    return (
        <BarChart
            h={300}
            data={data}
            withLegend={true}
            dataKey="month"
            valueFormatter={(value) => value + 'kwH'}
            type="stacked"
            series={series}
        />
    )
}
