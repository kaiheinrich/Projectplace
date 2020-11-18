import React, {useState} from "react";

const initialCredentials = {
    username: "",
    password: ""
}

export default function LoginPage() {

    const [credentials, setCredentials] = useState(initialCredentials);

    return(
        <>
            <header title="Login"/>
            <main>
                <form onSubmit={handleSubmit}>
                    <label>
                        <input
                            name="username"
                            value={credentials.username}
                            onChange={handleChange}
                            type="text"
                        />
                    </label>
                    <label>
                        <input
                            name="password"
                            value={credentials.password}
                            onChange={handleChange}
                            type="password"
                        />
                    </label>
                    <button>Login</button>
                </form>
            </main>
        </>
    );

    function handleSubmit(event) {
        event.preventDefault();
    }

    function handleChange(event) {
        setCredentials({...credentials, [event.target.name]: event.target.value})
    }
}