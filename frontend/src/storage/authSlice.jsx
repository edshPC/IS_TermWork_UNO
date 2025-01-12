import { createSlice } from '@reduxjs/toolkit';

const authSlice = createSlice({
    name: 'auth',
    initialState: {
        username: null,
        token: null,
    },
    reducers: {
        loginSuccess: (state, action) => {
            state.username = action.payload.username; // Устанавливаем username
            state.token = action.payload.token; // Устанавливаем токен
        },
        logout: (state) => {
            state.username = null;
            state.token = null;
        },
    },
});

export const { loginSuccess, logout } = authSlice.actions;

export default authSlice.reducer;
