import { BrowserRouter as Router, Route, Routes, Link } from "react-router-dom";
import RegistrationForm from "./components/RegistrationForm";
import LoginForm from "./components/LoginForm";
import Home from "./components/Home";
import Profile from "./components/Profile";
import OpportunityBoard from "./components/OpportunityBoard";
import CampaignManagement from "./components/CampaignManagement";
import MessagingSystem from "./components/MessagingSystem";

function App() {
  return (
    <Router>
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/register">Register</Link>
          </li>
          <li>
            <Link to="/login">Login</Link>
          </li>
          <li>
            <Link to="/profile">Profile</Link>
          </li>
          <li>
            <Link to="/opportunity-board">Opportunity Board</Link>
          </li>
          <li>
            <Link to="/campaign-management">Campaign Management</Link>
          </li>
          <li>
            <Link to="/messaging">Messaging System</Link>
          </li>
        </ul>
      </nav>
      <Routes>
        <Route path="/register" element={<RegistrationForm />} />
        <Route path="/login" element={<LoginForm />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/opportunity-board" element={<OpportunityBoard />} />
        <Route path="/campaign-management" element={<CampaignManagement />} />
        <Route path="/messaging" element={<MessagingSystem />} />
        <Route path="/" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;