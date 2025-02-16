import {AxiosResponse} from "axios";
import axiosInstance from "../configurations/axios.ts";

interface LoginRequest {
    username: string,
    password: string
}

export interface LoginResponse {
    token: string
    fullName: string
    username: string
}

export class LoginRepository {
    static login (request: LoginRequest): Promise<LoginResponse> {
        return axiosInstance.post('/authentication/login', request)
            .then((response: AxiosResponse<LoginResponse>) => response.data)
    }
}
