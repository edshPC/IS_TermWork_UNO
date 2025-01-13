import { createSlice } from '@reduxjs/toolkit';
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

const initialState = {
    username: localStorage.getItem('username') || null,
    token: localStorage.getItem('token') || null,
};

const authSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        loginSuccess: (state, action) => {
            state.username = action.payload.username;
            state.token = action.payload.token;
            localStorage.setItem('username', action.payload.username);
            localStorage.setItem('token', action.payload.token);
        },
        logout: (state) => {
            state.username = null;
            state.token = null;
            localStorage.removeItem('username');
            localStorage.removeItem('token');
        },
    },
});

export const { loginSuccess, logout } = authSlice.actions;

export default authSlice.reducer;

export function useAuthCheck() {
    const token = useSelector(state => state.auth.token);
    const navigate = useNavigate();
    useEffect(() => {
        token || navigate('/');
    }, [token, navigate]);
}
