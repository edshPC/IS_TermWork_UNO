import { createSlice } from '@reduxjs/toolkit';

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

