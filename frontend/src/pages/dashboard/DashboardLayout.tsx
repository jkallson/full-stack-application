import {Header} from "./components/Header.tsx";
import '../../style/dashboardLayout.css'
import {Outlet} from "react-router";

export function DashboardLayout() {
    return (
        <>
            <Header></Header>
            <div className="container">
                <Outlet></Outlet>
            </div>
        </>
    )
}
