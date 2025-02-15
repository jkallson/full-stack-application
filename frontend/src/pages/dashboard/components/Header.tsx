import '../../../style/header.css'
import '../../../App.css'
import {Button, Menu} from "@mantine/core";
import { MdOutlinePerson, MdKeyboardArrowDown, MdLogout } from "react-icons/md";
import {useNavigate} from "react-router";


export function Header () {
    let navigate = useNavigate();

    const toDashboard = (): void => {
        navigate('/dashboard')
    }

    const logout = (): void => {
        navigate('/login')
    }

    return (
        <div className="header">
            <div className="container">
                <div className="header-logo-container">
                    <img className="header-image" src="/src/assets/company.svg" alt="GreenFlow Logo" onClick={toDashboard}/>
                    <Menu withinPortal position="bottom-end" shadow="sm" transitionProps={{ transition: 'pop', duration: 100 }}>
                        <Menu.Target>
                                <Button
                                    variant="outline"
                                    leftSection={<MdOutlinePerson size={25} />}
                                    rightSection={<MdKeyboardArrowDown size={20} />}
                                >
                                    Mari Mets
                                </Button>
                        </Menu.Target>

                        <Menu.Dropdown>
                            <Menu.Item leftSection={<MdLogout size={14} />} onClick={logout}>
                                Logi välja
                            </Menu.Item>
                        </Menu.Dropdown>
                    </Menu>
                </div>
            </div>
        </div>
    )
}
