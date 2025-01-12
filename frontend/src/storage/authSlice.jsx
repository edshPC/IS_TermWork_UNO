import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    username: "",
    token: null,
    gameUUID: null,
    privateUUID: null,
};

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {
        loginSuccess(state, action) {
            state.username = action.payload.username;
            state.token = action.payload.token;
        },
        logout(state) {
            state.username = "";
            state.token = null;
            state.gameUUID = null;
            state.privateUUID = null;
        },
        joinGameSuccess(state, action) {
            state.gameUUID = action.payload.gameUUID;
            state.privateUUID = action.payload.privateUUID;
        },
    },
});

export const { loginSuccess, logout, joinGameSuccess } = authSlice.actions;
export default authSlice.reducer;

