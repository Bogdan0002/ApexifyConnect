import { useState } from "react";
import { loginUser } from "../api/UserService";

const LoginForm = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    role: "content-creator", // default role
  });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      const response = await loginUser(formData.email, formData.password, formData.role);
      setMessage("Login successful!");
      // Handle successful login (e.g., store token, redirect, etc.)
    } catch (error: any) {
      setMessage(error.response?.data?.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="email"
        placeholder="Email"
        required
        onChange={(e) => setFormData({ ...formData, email: e.target.value })}
      />
      <input
        type="password"
        placeholder="Password"
        required
        onChange={(e) => setFormData({ ...formData, password: e.target.value })}
      />
      <select
        required
        onChange={(e) => setFormData({ ...formData, role: e.target.value })}
      >
        <option value="content-creator">Content Creator</option>
        <option value="company">Company</option>
      </select>
      <button type="submit" disabled={loading}>
        {loading ? "Logging in..." : "Login"}
      </button>
      {message && <p>{message}</p>}
    </form>
  );
};

export default LoginForm;