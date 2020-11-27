const USER_CREDENTIALS = "USER_CREDENTIALS";
const ACCESS_TOKEN = "ACCESS_TOKEN";

export const loadTokenFromLocalStorage = () =>
    localStorage.getItem(ACCESS_TOKEN);

export const saveTokenToLocalStorage = (token) =>
    localStorage.setItem(ACCESS_TOKEN, token);

export const loadUserCredentialsFromLocalStorage = () => {

    const userCredentialsString = localStorage.getItem(USER_CREDENTIALS);

    try {
        return JSON.parse(userCredentialsString);
    } catch (exception) {
        console.error(exception);
    }
}

export const saveUserCredentialsToLocalStorage = (userCredentials) =>
    localStorage.setItem(USER_CREDENTIALS, JSON.stringify(userCredentials));

export const deleteTokenFromLocalStorage = () => {
    localStorage.removeItem(ACCESS_TOKEN);
}
