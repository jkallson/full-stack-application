import {Button, Card, Grid, Text, ThemeIcon} from "@mantine/core";
import '../../../style/informationCard.css'
import '../../../style/utils.css'
import {ReactNode} from "react";
import {useNavigate} from "react-router";

interface InformationCardProps {
    title: string,
    amount: string,
    icon: ReactNode
}

export function InformationCard({ title, amount, icon }: InformationCardProps) {
    let navigate = useNavigate();

    const openDetails = (): void => {
        navigate('/dashboard/details')
    }

    return (
        <Card shadow="sm" padding="lg" radius="md" withBorder>
            <Grid>
                <Grid.Col span={8}>
                    <Text size="md" fw="400">{title}</Text>
                    <Text size="xl" fw="700">{amount}</Text>
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
        </Card>
    )
}
