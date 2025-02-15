import './App.css'
import {createTheme, MantineProvider} from "@mantine/core";
import '@mantine/core/styles.css';
import '@mantine/charts/styles.css';
import {BrowserRouter, Navigate, Route, Routes} from "react-router";
import {AuthLayout} from "./pages/auth/AuthLayout.tsx";
import {DashboardLayout} from "./pages/dashboard/DashboardLayout.tsx";
import {DashboardView} from "./pages/dashboard/views/DashboardView.tsx";
import {DashboardDetailsView} from "./pages/dashboard/views/DashboardDetailsView.tsx";

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
            <BrowserRouter>
                <Routes>
                    <Route path="login" element={<AuthLayout/>}/>
                    <Route path="/" element={<Navigate to="/dashboard" replace />}/>
                    <Route path="dashboard" element={<DashboardLayout/>}>
                        <Route index element={<DashboardView/>} />
                        <Route path="details" element={<DashboardDetailsView/>} />
                    </Route>
                </Routes>
            </BrowserRouter>
        </MantineProvider>
    )
}

export default App
