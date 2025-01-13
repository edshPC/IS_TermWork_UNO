import { createSlice } from '@reduxjs/toolkit';

const gameSlice = createSlice({
    name: 'game',
    initialState: {
        roomId: null,
        gameUUID: null,
        privateUUID: null,
    },
    reducers: {
        joinRoom: (state, action) => {
            state.roomId = action.payload.roomId;
        },
        joinGame: (state, action) => {
            state.roomId = action.payload.roomId;
            state.gameUUID = action.payload.gameUUID;
            state.privateUUID = action.payload.privateUUID;
            localStorage.setItem('roomId', state.roomId);
        },
        leaveGame: (state) => {
            state.roomId = null;
            state.gameUUID = null;
            state.privateUUID = null;
            localStorage.removeItem('roomId');
        },
    },
});

export const { joinRoom, joinGame, leaveGame } = gameSlice.actions;

export default gameSlice.reducer;
