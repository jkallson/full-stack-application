import {BarChart} from "@mantine/charts";
import {BarChartEntry} from "../../ts/BarChartInterfaces.ts";

export interface DashboardPriceBarChartProps {
    data: BarChartEntry[]
    series: any
}

export function DashboardPriceBarChart ({ data, series }: DashboardPriceBarChartProps) {
    return (
        <BarChart
            h={300}
            data={data}
            withLegend={true}
            dataKey="month"
            valueFormatter={(value) => value + ' €'}
            type="stacked"
            series={series}
        />
    )
}
