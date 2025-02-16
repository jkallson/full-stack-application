import {ReactNode} from "react";
import {Card, Grid, Text, ThemeIcon} from "@mantine/core";

interface CardProps {
    title: string,
    text: string,
    icon: ReactNode
}

export function DashboardDetailsInformationCard ({title, text, icon}: CardProps) {
    return (
        <Card shadow="sm" padding="lg" radius="md" withBorder>
            <Grid>
                <Grid.Col span={8}>
                    <Text size="sm" fw="400">
                        { title }
                    </Text>
                    <Text size="xl" fw="700">
                        {text}
                    </Text>
                </Grid.Col>
                <Grid.Col span={4} className="justify-end align-center">
                    <ThemeIcon radius="md" size="lg">
                        { icon }
                    </ThemeIcon>
                </Grid.Col>
            </Grid>
        </Card>
    )
}
