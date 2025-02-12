import './App.css'
import {createTheme, MantineProvider} from "@mantine/core";
import '@mantine/core/styles.css';
import {Login} from "./pages/Login.tsx";
import {Header} from "./components/Header.tsx";

const theme = createTheme({
    primaryColor: "customGreen",
    colors: {
        customGreen: [
            "#e6f5e6",
            "#c2e6c2",
            "#99d699",
            "#70c570",
            "#4db24d",
            "#33B233",
            "#2a912a",
            "#207020",
            "#175017",
            "#0e300e",
        ],
    },
});

function App() {
    return (
        <MantineProvider theme={theme}>
            <Header></Header>
            <div className="container">
                <Login></Login>
            </div>
        </MantineProvider>
    )
}

export default App
