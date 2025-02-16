import {Button, Card, Grid, Select, Text} from "@mantine/core";
import {useNavigate} from "react-router";
import {MdBarChart, MdElectricBolt, MdEuroSymbol, MdMapsHomeWork, MdOutlineSearch} from "react-icons/md";
import {useEffect, useMemo, useState} from "react";
import {MeteringPointsDomain} from "../ts/MeteringPointsDomain.ts";
import {MeteringPoint, MeteringPointsRepository} from "../../../repositories/MeteringPointsRepository.ts";
import {DashboardPriceBarChart} from "../components/charts/DashboardPriceBarChart.tsx";
import {DashboardConsumptionBarChart} from "../components/charts/DashboardConsumptionBarChart.tsx";
import {DashboardDetailsInformationCard} from "../components/details/DashboardDetailsInformationCard.tsx";
import {Chart} from "../ts/BarChartInterfaces.ts";


export function DashboardDetailsView() {
    let navigate = useNavigate();
    const [selectedAddress, setSelectedAddress] = useState<string | null>('');
    const [loading, setLoading] = useState<boolean>(true);
    const [meteringPoints, setMeteringPoints] = useState<MeteringPointsDomain | null>(null);

    useEffect(() => {
        MeteringPointsRepository.getMeteringPoints()
            .then((response: MeteringPoint[]) => {
                setMeteringPoints(new MeteringPointsDomain(response))
                setSelectedAddress(response[0].address)
                setLoading(false)
            })
    }, [])

    const goToDashboard = (): void => { navigate('/dashboard') }
    const addresses: string[] = useMemo(() => meteringPoints?.addresses() ?? [], [meteringPoints]);
    const totalUsage: number = useMemo(() => meteringPoints?.totalConsumptionFor(selectedAddress!) ?? "", [meteringPoints, selectedAddress]) as number;
    const totalPrice: string = useMemo(() => meteringPoints?.totalPriceFor(selectedAddress!) ?? "", [meteringPoints, selectedAddress]);
    const averageUsage: string = useMemo(() => meteringPoints?.averageMonthlyConsumptionFor(selectedAddress!) ?? "", [meteringPoints, selectedAddress]);
    const averagePrice: string = useMemo(() => meteringPoints?.averageMonthlyPriceFor(selectedAddress!) ?? "", [meteringPoints, selectedAddress]);

    const perMonthConsumptionChart: Chart = useMemo(() => meteringPoints?.consumptionChartFor(selectedAddress!) ?? { result: [], series: [] }, [meteringPoints, selectedAddress]);
    const perMonthPriceChart: Chart = useMemo(() => meteringPoints?.priceChartFor(selectedAddress!) ?? { result: [], series: [] }, [meteringPoints, selectedAddress]);

    return (
        <Grid>
            <Grid.Col span={12} className="justify-end">
                <Button onClick={goToDashboard}>Tagasi</Button>
            </Grid.Col>
            <Grid.Col span={12}>
                <Card shadow="sm" padding="lg" radius="md" withBorder>
                    <Select
                        label="Aadress"
                        placeholder="Palun valige aadress"
                        data={addresses}
                        value={selectedAddress}
                        onChange={setSelectedAddress}
                        leftSectionPointerEvents="none"
                        leftSection={<MdOutlineSearch size={16}/>}
                        allowDeselect={false}
                        comboboxProps={{transitionProps: {transition: 'pop', duration: 100}}}
                    />
                </Card>
            </Grid.Col>
            <Grid.Col span={12}>
                <Grid>
                    <Grid.Col span={{base: 12, xs: 6, md: 3}}>
                        <DashboardDetailsInformationCard
                            title="Keskmine kuutasu"
                            text={averagePrice + " €"}
                            icon={<MdEuroSymbol size={20}/>}
                            loading={loading}
                        ></DashboardDetailsInformationCard>
                    </Grid.Col>
                    <Grid.Col span={{base: 12, xs: 6, md: 3}}>
                        <DashboardDetailsInformationCard
                            title="Keskmine kasutus"
                            text={averageUsage + " kwH"}
                            icon={<MdElectricBolt size={20}/>}
                            loading={loading}
                        ></DashboardDetailsInformationCard>
                    </Grid.Col>
                    <Grid.Col span={{base: 12, xs: 6, md: 3}}>
                        <DashboardDetailsInformationCard
                            title="Tasu kokku"
                            text={totalPrice + " €"}
                            icon={<MdBarChart size={20}/>}
                            loading={loading}
                        ></DashboardDetailsInformationCard>
                    </Grid.Col>
                    <Grid.Col span={{base: 12, xs: 6, md: 3}}>
                        <DashboardDetailsInformationCard
                            title="Kasutus kokku"
                            text={totalUsage + " kwH"}
                            icon={<MdMapsHomeWork size={20}/>}
                            loading={loading}
                        ></DashboardDetailsInformationCard>
                    </Grid.Col>
                </Grid>
            </Grid.Col>
            <Grid.Col span={12}>
                <Card shadow="sm" padding="lg" radius="md" withBorder>
                    <Text mb="md" pl="md">
                        Elektrikasutus:
                    </Text>
                    <DashboardConsumptionBarChart
                        data={perMonthConsumptionChart.result}
                        series={perMonthConsumptionChart.series}
                        loading={loading}
                    ></DashboardConsumptionBarChart>
                </Card>
            </Grid.Col>
            <Grid.Col span={12} mb={10}>
                <Card shadow="sm" padding="lg" radius="md" withBorder>
                    <Text mb="md" pl="md">
                        Rahaline kulu:
                    </Text>
                    <DashboardPriceBarChart
                        data={perMonthPriceChart.result}
                        series={perMonthPriceChart.series}
                        loading={loading}
                    ></DashboardPriceBarChart>
                </Card>
            </Grid.Col>
        </Grid>
    )
}
