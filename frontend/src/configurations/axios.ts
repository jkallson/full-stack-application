import axios, {AxiosInstance} from "axios";

const addToken = (config: any) => {
    const token: string | null = localStorage.getItem('token')

    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }

    return config
}

const handleError = (error: any) => {
    if ([401, 403].includes(error.response.status)) {
        localStorage.removeItem("token")
        localStorage.removeItem("user")

        if (window.location.pathname !== "/login") {
            window.location.href = "/login";
        }
    }

    return Promise.reject(error)
}

const axiosInstance: AxiosInstance = axios.create({
    baseURL: "http://localhost:3000"
});

axiosInstance.interceptors.request.use(addToken)
axiosInstance.interceptors.response.use((response) => response, handleError)

export default axiosInstance
