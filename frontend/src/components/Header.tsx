import '../style/header.css'
import '../App.css'

export function Header () {
    return (
        <div className="header">
            <div className="container">
                <div className="header-logo-container">
                    <img src="/public/company.svg" alt="EcoFit Logo"/>
                </div>
            </div>
        </div>
    )
}
