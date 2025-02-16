import '../../../App.css'
import {Button, Menu} from "@mantine/core";
import { MdOutlinePerson, MdKeyboardArrowDown, MdLogout } from "react-icons/md";
import {useNavigate} from "react-router";
import {useAuth} from "../../../context/AuthContext.tsx";
import {notifications} from "@mantine/notifications";


export function Header () {
    let navigate = useNavigate();
    const { logout, user } = useAuth();

    const toDashboard = (): void => {
        navigate('/dashboard')
    }

    const onLogoutClicked = (): void => {
        notifications.show({
            title: 'Välja logitud',
            message: 'Olete edukalt välja logitud!',
        })
        logout()
        navigate('/login')
    }

    return (
        <div className="header">
            <div className="container">
                <div className="header-logo-container">
                    <img className="header-image" src="/company.svg" alt="GreenFlow Logo" onClick={toDashboard}/>
                    <Menu withinPortal position="bottom-end" shadow="sm" transitionProps={{ transition: 'pop', duration: 100 }}>
                        <Menu.Target>
                                <Button
                                    variant="outline"
                                    leftSection={<MdOutlinePerson size={25} />}
                                    rightSection={<MdKeyboardArrowDown size={20} />}
                                >
                                    <span className="hide-on-small">{ user }</span>
                                </Button>
                        </Menu.Target>

                        <Menu.Dropdown>
                            <Menu.Item leftSection={<MdLogout size={14} />} onClick={onLogoutClicked}>
                                Logi välja
                            </Menu.Item>
                        </Menu.Dropdown>
                    </Menu>
                </div>
            </div>
        </div>
    )
}
