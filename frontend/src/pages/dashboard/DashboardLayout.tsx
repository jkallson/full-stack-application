import {Header} from "./components/Header.tsx";
import '../../style/dashboardLayout.css'
import {Navigate, Outlet} from "react-router";
import {useAuth} from "../../context/AuthContext.tsx";

export function DashboardLayout() {
    const { isAuthenticated } = useAuth();
    return (
        isAuthenticated ?
        <>
            <Header></Header>
            <div className="container">
                <Outlet></Outlet>
            </div>
        </> : <Navigate to="/login" />
    )
}
