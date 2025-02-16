import {Card, Container, Grid, Tabs} from "@mantine/core";
import {InformationCard} from "../components/InformationCard.tsx";
import {MdElectricBolt, MdEuroSymbol} from "react-icons/md";
import {BarChart} from "@mantine/charts";
import {data, data2} from "../data.ts";
import '../../../style/dashboardLayout.css'
import {useEffect, useState} from "react";
import {MeteringPoint, MeteringPointsRepository} from "../../../repositories/MeteringPointsRepository.ts";
import {useAuth} from "../../../context/AuthContext.tsx";
import {MeteringPointsDomain} from "../ts/MeteringPointsDomain.ts";
import {useDisclosure} from "@mantine/hooks";

export function DashboardView() {
    const { user } = useAuth();
    const [visible, { toggle }] = useDisclosure(true);
    const [meteringPoints, setMeteringPoints] = useState<MeteringPointsDomain>();


    useEffect(() => {
        MeteringPointsRepository.getMeteringPoints()
            .then((response: MeteringPoint[]) => {
                setMeteringPoints(new MeteringPointsDomain(response))
                toggle()
            })
    }, [])

    const totalAmountConsumption = (): number | undefined => {
        return meteringPoints?.getTotalAmount()
    }

    const totalAmountPrice = (): string | undefined => {
        return meteringPoints?.getTotalPrice()
    }

    return (
        <Container fluid={true} p={0} pb={20}>
            <h2>
                Tere, { user }
            </h2>
            <Grid>
                <Grid.Col span={{base: 12, xs: 6}}>
                    <InformationCard title="Aastane elektrikasutus"
                                     amount={totalAmountConsumption() + " kwH"}
                                     loading={visible}
                                     icon={<MdElectricBolt size={25}/>}
                    ></InformationCard>
                </Grid.Col>
                <Grid.Col span={{base: 12, xs: 6}}>
                    <InformationCard title="Aastane rahakulu"
                                     amount={totalAmountPrice() + " €"}
                                     loading={visible}
                                     icon={<MdEuroSymbol size={25}/>}
                    ></InformationCard>
                </Grid.Col>
            </Grid>
            <Grid>
                <Grid.Col span={12} mt={20}>
                    <Tabs defaultValue="first" variant="pills" radius={0} keepMounted={false}>
                        <Tabs.List>
                            <Card className="tab" withBorder>
                                <Tabs.Tab value="first" leftSection={<MdElectricBolt size={14}/>}>Aastane
                                    elektrikasutus</Tabs.Tab>
                                <Tabs.Tab value="second" leftSection={<MdEuroSymbol size={14}/>}>Aastane
                                    rahakulu</Tabs.Tab>
                            </Card>
                        </Tabs.List>
                        <Tabs.Panel value="first" p={0} m={0}>
                            <Card className="tab-panel" shadow="sm" withBorder>
                                <BarChart
                                    h={300}
                                    data={data2}
                                    withLegend={true}
                                    dataKey="month"
                                    type="stacked"
                                    series={[
                                        {name: 'Smartphones', color: 'violet.6'},
                                        {name: 'Laptops', color: 'blue.6'},
                                        {name: 'Tablets', color: 'teal.6'},
                                    ]}
                                />
                            </Card>
                        </Tabs.Panel>
                        <Tabs.Panel value="second" p={0} m={0}>
                            <Card shadow="sm" className="tab-panel" padding="lg" radius="md" withBorder>
                                <BarChart
                                    h={300}
                                    data={data}
                                    dataKey="month"
                                    type="stacked"
                                    withLegend={true}
                                    series={[
                                        {name: 'Smartphones', color: 'violet.6'},
                                        {name: 'Laptops', color: 'blue.6'},
                                        {name: 'Tablets', color: 'teal.6'},
                                    ]}
                                />
                            </Card>
                        </Tabs.Panel>
                    </Tabs>
                </Grid.Col>
            </Grid>
        </Container>
    )
}
