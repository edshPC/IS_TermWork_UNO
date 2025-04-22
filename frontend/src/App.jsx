import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { Provider } from "react-redux";
import store from "./storage/store";
import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import MainPage from "./pages/MainPage";
import GameApp from "./game/GameApp";
import CreateRoomPage from './pages/CreateRoomPage';
import JoinRoomPage from './pages/JoinRoomPage';
import StatisticsPage from './pages/StatisticsPage';
import AchievementsPage from './pages/AchievementsPage'

export const origin = '';

export default function App() {
    return (
        <Provider store={store}>
            <Router>
                <Routes>
                    <Route path="/" element={<LandingPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/main" element={<MainPage />} />
                    <Route path="/game" element={<GameApp />} />
                    <Route path="/create-room" element={<CreateRoomPage />} />
                    <Route path="/join-room" element={<JoinRoomPage />} />
                    <Route path="/statistics" element={<StatisticsPage />} />
                    <Route path="/achievements" element={<AchievementsPage />} />
                </Routes>
            </Router>
        </Provider>
    );
}
