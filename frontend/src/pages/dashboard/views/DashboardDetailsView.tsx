import {Button, Card, Grid, Select, Text} from "@mantine/core";
import {BarChart} from "@mantine/charts";
import {data} from "../data.ts";
import '../../../style/utils.css'
import {useNavigate} from "react-router";
import {MdOutlineSearch} from "react-icons/md";


export function DashboardDetailsView() {
    let navigate = useNavigate();

    const goToDashboard = () => {
        navigate('/dashboard')
    }

    return (
        <Grid>
            <Grid.Col span={{base: 12}} className="justify-end">
                <Button onClick={goToDashboard}>Tagasi</Button>
            </Grid.Col>
            <Grid.Col span={{base: 12}}>
                <Card shadow="sm" padding="lg" radius="md" withBorder>
                    <Select
                        label="Aadress"
                        placeholder="Palun valige aadress"
                        data={['React', 'Angular', 'Vue', 'Svelte']}
                        defaultValue="React"
                        leftSectionPointerEvents="none"
                        leftSection={<MdOutlineSearch size={16}/>}
                        allowDeselect={false}
                        comboboxProps={{transitionProps: {transition: 'pop', duration: 100}}}
                    />
                </Card>
            </Grid.Col>
            <Grid.Col span={{base: 12}}>
                <Grid>
                    <Grid.Col span={{base: 12}}>
                        <Card shadow="sm" padding="lg" radius="md" withBorder>
                            <Text mb="md" pl="md">
                                Elektrikasutus:
                            </Text>

                            <BarChart
                                h={180}
                                data={data}
                                dataKey="month"
                                series={[{name: 'Smartphones', color: 'violet.6'}]}
                                barChartProps={{syncId: 'tech'}}
                            />

                            <Text mb="md" pl="md" mt="xl">
                                Rahaline kulu:
                            </Text>

                            <BarChart
                                h={180}
                                data={data}
                                dataKey="month"
                                barChartProps={{syncId: 'tech'}}
                                series={[{name: 'Smartphones', color: 'teal.6'}]}
                            />
                        </Card>
                    </Grid.Col>
                </Grid>
            </Grid.Col>
        </Grid>
    )
}
