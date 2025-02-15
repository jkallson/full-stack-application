import React, { createContext, useContext, useEffect, useState } from "react";
import {LoginResponse} from "../repositories/LoginRepository.ts";

interface AuthContextType {
    token: string | null;
    user: string | null;
    login: (loginResponse: LoginResponse) => void;
    logout: () => void;
    isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
    const [user, setUser] = useState<string | null>(localStorage.getItem("user"));

    useEffect(() => {
        if (token) {
            localStorage.setItem("token", token);
        } else {
            localStorage.removeItem("token");
        }
    }, [token]);

    useEffect(() => {
        if (user) {
            localStorage.setItem("user", user);
        } else {
            localStorage.removeItem("user");
        }
    }, [user]);

    const login = (loginResponse: LoginResponse): void => {
        handleLogin(loginResponse)
    };

    const logout = () => {
        handleLogout()
    };

    const handleLogin = (loginResponse: LoginResponse): void => {
        setToken(loginResponse.token)
        setUser(loginResponse.fullName)
    }

    const handleLogout = (): void => {
        setToken(null)
        setUser(null)
    }

    return (
        <AuthContext.Provider value={{ token, user, login, logout, isAuthenticated: !!token }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}
