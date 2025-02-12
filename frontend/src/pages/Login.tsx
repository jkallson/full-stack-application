import {ChangeEvent, FormEvent, useState} from "react";
import {Button, Container, Paper, PasswordInput, Stack, TextInput, Grid} from "@mantine/core";
import {MdKeyboardArrowRight} from "react-icons/md";
import '../App.css'


interface LoginForm {
    username: string;
    password: string;
}

export function Login() {
    const [form, setForm] = useState<LoginForm>({username: "", password: ""});

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setForm({...form, [e.target.name]: e.target.value});
    };

    const handleSubmit = (e: FormEvent) => {
        console.log(e)
    };

    return (
        <Container fluid={true} p={0}>
            <Paper withBorder shadow="md" p={25} radius="md">
                <Grid>
                    <Grid.Col span={12}>
                        <form onSubmit={handleSubmit}>
                            <div className="hide-on-small">
                                <div>
                                    <h2>Tere tulemast!</h2>
                                </div>
                                <div>
                                    <p>Teenusesse sisselogimiseks palun sisestage oma kasutajanimi ning parool</p>
                                </div>
                            </div>
                            <Stack>
                                <TextInput
                                    label="Kasutajanimi"
                                    placeholder="Sisestage kasutajanimi"
                                    name="username"
                                    size="md"
                                    value={form.username}
                                    onChange={handleChange}
                                    required
                                />
                                <PasswordInput
                                    label="Parool"
                                    placeholder="Sisestage parool"
                                    name="password"
                                    size="md"
                                    value={form.password}
                                    onChange={handleChange}
                                    required
                                />
                            </Stack>
                            <Button size="md" type="submit" mt={25} rightSection={<MdKeyboardArrowRight size={25}/>}>
                                Logi sisse
                            </Button>
                        </form>
                    </Grid.Col>
                </Grid>
            </Paper>
        </Container>
    );
}
