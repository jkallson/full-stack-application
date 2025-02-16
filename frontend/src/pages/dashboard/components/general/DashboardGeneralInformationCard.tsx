import {Button, Card, Grid, Loader, Text, ThemeIcon} from "@mantine/core";
import {ReactNode} from "react";
import {useNavigate} from "react-router";

interface InformationCardProps {
    title: string,
    amount: string,
    loading: boolean
    icon: ReactNode
}

export function DashboardGeneralInformationCard({title, amount, loading, icon}: InformationCardProps) {
    let navigate = useNavigate();

    const openDetails = (): void => {
        navigate('/dashboard/details')
    }

    return (
        <Card shadow="sm" padding="lg" radius="md" withBorder>
            {
                loading
                    ?
                    <Grid h={126} className="flex align-center">
                        <Grid.Col span={12} className="flex justify-center">
                            <Loader size={50}></Loader>
                        </Grid.Col>
                    </Grid>
                    :
                    <Grid>
                        <Grid.Col span={8}>
                            <Text size="md" fw="400">
                                {title}
                            </Text>
                            <Text size="xl" fw="700">
                                { amount }
                            </Text>
                        </Grid.Col>
                        <Grid.Col span={4} className="justify-end">
                            <ThemeIcon radius="md" size="xl">
                                {icon}
                            </ThemeIcon>
                        </Grid.Col>
                        <Grid.Col span={12}>
                            <Button mt="md" radius="md" onClick={openDetails}>
                                Vaata lähemalt
                            </Button>
                        </Grid.Col>
                    </Grid>

            }
        </Card>
    )
}
