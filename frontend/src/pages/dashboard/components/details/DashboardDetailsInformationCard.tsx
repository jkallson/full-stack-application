import {ReactNode} from "react";
import {Card, Grid, Loader, Text, ThemeIcon} from "@mantine/core";

interface CardProps {
    title: string,
    text: string,
    icon: ReactNode
    loading: boolean
}

export function DashboardDetailsInformationCard({title, text, icon, loading}: CardProps) {
    return (
        <Card shadow="sm" padding="lg" radius="md" withBorder>
            {
                loading
                    ? <Grid h={54} className="flex align-center">
                        <Grid.Col span={12} className="flex justify-center">
                            <Loader size={35}></Loader>
                        </Grid.Col>
                    </Grid>
                    :
                    <Grid>
                        <Grid.Col span={8}>
                            <Text size="sm" fw="400">
                                {title}
                            </Text>
                            <Text size="xl" fw="700">
                                {text}
                            </Text>
                        </Grid.Col>
                        <Grid.Col span={4} className="justify-end align-center">
                            <ThemeIcon radius="md" size="lg">
                                {icon}
                            </ThemeIcon>
                        </Grid.Col>
                    </Grid>
            }
        </Card>
    )
}
