import {AuthHeader} from "./components/AuthHeader.tsx";
import {Login} from "./components/Login.tsx";

export function AuthLayout() {
    return (
        <>
            <AuthHeader></AuthHeader>
            <Login></Login>
        </>
    )
}
