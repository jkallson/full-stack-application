import {useState} from "react";
import {Button, Container, Paper, PasswordInput, Stack, TextInput, Grid} from "@mantine/core";
import {MdKeyboardArrowRight} from "react-icons/md";
import '../../../App.css'
import {isNotEmpty, useForm} from "@mantine/form";
import {useNavigate} from "react-router";

export function Login() {
    const [isLoading, setIsLoading] = useState<boolean>(false)
    let navigate = useNavigate();
    const form = useForm({
        mode: 'uncontrolled',
        initialValues: {username: '', password: ''},
        validate: {
            username: isNotEmpty('Väli peab olema täidetud'),
            password: isNotEmpty('Väli peab olema täidetud'),
        },
    });

    function handleSubmit(values: { username: string, password: string }) {
        console.log(values)
        setIsLoading(true)
        navigate('/')
    }

    return (
        <div className="container">
            <Container fluid={true} p={0}>
                <Paper withBorder shadow="md" p={25} radius="md">
                    <Grid>
                        <Grid.Col span={12}>
                            <form onSubmit={form.onSubmit(handleSubmit)}>
                                <div className="hide-on-small">
                                    <div>
                                        <h2>Tere tulemast!</h2>
                                    </div>
                                    <div>
                                        <p>Teenusesse sisselogimiseks palun sisestage oma kasutajanimi ning
                                            parool</p>
                                    </div>
                                </div>
                                <Stack>
                                    <TextInput
                                        {...form.getInputProps('username')}
                                        key={form.key('username')}
                                        label="Kasutajanimi"
                                        placeholder="Sisestage kasutajanimi"
                                        name="username"
                                        size="md"
                                    />
                                    <PasswordInput
                                        {...form.getInputProps('password')}
                                        key={form.key('password')}
                                        label="Parool"
                                        placeholder="Sisestage parool"
                                        name="password"
                                        size="md"
                                    />
                                </Stack>
                                <Button size="md"
                                        type="submit"
                                        mt={25}
                                        loading={isLoading}
                                        rightSection={<MdKeyboardArrowRight size={25}/>}
                                >
                                    Logi sisse
                                </Button>
                            </form>
                        </Grid.Col>
                    </Grid>
                </Paper>
            </Container>
        </div>
    );
}
