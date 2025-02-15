import axios, {AxiosResponse} from "axios";

const resource = 'http://localhost:3000'

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
        return axios.post(`${resource}/authentication/login`, request)
            .then((response: AxiosResponse<LoginResponse>) => response.data)
    }
}
