import {Card, Container, Grid, Tabs} from "@mantine/core";
import {MdElectricBolt, MdEuroSymbol} from "react-icons/md";
import {useEffect, useMemo, useState} from "react";
import {MeteringPoint, MeteringPointsRepository} from "../../../repositories/MeteringPointsRepository.ts";
import {useAuth} from "../../../context/AuthContext.tsx";
import {MeteringPointsDomain} from "../ts/MeteringPointsDomain.ts";
import {DashboardConsumptionBarChart} from "../components/charts/DashboardConsumptionBarChart.tsx";
import {DashboardPriceBarChart} from "../components/charts/DashboardPriceBarChart.tsx";
import {DashboardGeneralInformationCard} from "../components/general/DashboardGeneralInformationCard.tsx";
import {Chart} from "../ts/BarChartInterfaces.ts";

export function DashboardView() {
    const { user } = useAuth();
    const [loading, setLoading] = useState<boolean>(true);
    const [meteringPoints, setMeteringPoints] = useState<MeteringPointsDomain>();

    useEffect(() => {
        MeteringPointsRepository.getMeteringPoints()
            .then((response: MeteringPoint[]) => {
                setMeteringPoints(new MeteringPointsDomain(response))
                setLoading(false)
            })
    }, [])

    const totalConsumptionAmount: number = useMemo(() => meteringPoints?.totalConsumption() ?? 0, [meteringPoints])
    const totalPriceAmount: string = useMemo(() => meteringPoints?.totalPrice() ?? "", [meteringPoints])

    const totalConsumptionChart: Chart = useMemo(() => meteringPoints?.totalConsumptionChart() ?? { result: [], series: [] }, [meteringPoints])
    const totalPriceChart: Chart = useMemo(() => meteringPoints?.totalPriceChart() ?? { result: [], series: [] }, [meteringPoints])

    return (
        <Container fluid={true} p={0} pb={20}>
            <h2>
                Tere, { user }
            </h2>
            <Grid>
                <Grid.Col span={{base: 12, xs: 6}}>
                    <DashboardGeneralInformationCard title="Aastane elektrikasutus"
                                                     amount={totalConsumptionAmount + " kwH"}
                                                     loading={loading}
                                                     icon={<MdElectricBolt size={25}/>}
                    ></DashboardGeneralInformationCard>
                </Grid.Col>
                <Grid.Col span={{base: 12, xs: 6}}>
                    <DashboardGeneralInformationCard title="Aastane rahakulu"
                                                     amount={totalPriceAmount + " €"}
                                                     loading={loading}
                                                     icon={<MdEuroSymbol size={25}/>}
                    ></DashboardGeneralInformationCard>
                </Grid.Col>
            </Grid>
            <Grid>
                <Grid.Col span={12} mt={20}>
                    <Tabs defaultValue="first" variant="pills" radius={0} keepMounted={false}>
                        <Tabs.List>
                            <Card className="tab" withBorder>
                                <Tabs.Tab value="first" leftSection={<MdElectricBolt size={14}/>}>
                                    Aastane elektrikasutus
                                </Tabs.Tab>
                                <Tabs.Tab value="second" leftSection={<MdEuroSymbol size={14}/>}>
                                    Aastane rahakulu
                                </Tabs.Tab>
                            </Card>
                        </Tabs.List>
                        <Tabs.Panel value="first" p={0} m={0}>
                            <Card className="tab-panel" shadow="sm" withBorder>
                                <DashboardConsumptionBarChart
                                    data={totalConsumptionChart.result}
                                    series={totalConsumptionChart.series}
                                    loading={loading}
                                ></DashboardConsumptionBarChart>
                            </Card>
                        </Tabs.Panel>
                        <Tabs.Panel value="second" p={0} m={0}>
                            <Card className="tab-panel" shadow="sm" radius="md" withBorder>
                                <DashboardPriceBarChart
                                    data={totalPriceChart.result}
                                    series={totalPriceChart.series}
                                    loading={loading}
                                ></DashboardPriceBarChart>
                            </Card>
                        </Tabs.Panel>
                    </Tabs>
                </Grid.Col>
            </Grid>
        </Container>
    )
}
