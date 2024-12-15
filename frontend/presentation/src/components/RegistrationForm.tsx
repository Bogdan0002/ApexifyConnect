import { useState } from "react";
import { uploadProfilePicture, registerContentCreator, registerCompany } from "../api/UserService";

const RegistrationForm = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    role: "CONTENT_CREATOR",
    profilePicture: null,
    bio: "",
    companyName: "",
    businessLicense: "",
  });
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e: any) => {
    const file = e.target.files[0];
    if (file && file.size > 2 * 1024 * 1024) {
      setMessage("File size exceeds 2MB!");
      return;
    }
    if (file && !file.type.startsWith("image/")) {
      setMessage("Only image files are allowed!");
      return;
    }
    setFormData({ ...formData, profilePicture: file });
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
      // Upload profile picture if present
      let profilePictureUrl = "";
      if (formData.profilePicture) {
        profilePictureUrl = await uploadProfilePicture(formData.profilePicture as File);
      }

      // Register the user based on the role
      if (formData.role === "CONTENT_CREATOR") {
        const creatorData = {
          email: formData.email,
          password: formData.password,
          bio: formData.bio,
          profilePicture: profilePictureUrl,
        };
        await registerContentCreator(creatorData);
      } else if (formData.role === "COMPANY") {
        const companyData = {
          email: formData.email,
          password: formData.password,
          companyName: formData.companyName,
          businessLicense: formData.businessLicense,
        };
        await registerCompany(companyData);
      }

      setMessage("Registered successfully!");
    } catch (error: any) {
      setMessage(error.response?.data?.message || "Registration failed");
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
        onChange={(e) => setFormData({ ...formData, role: e.target.value })}
      >
        <option value="CONTENT_CREATOR">Content Creator</option>
        <option value="COMPANY">Company</option>
      </select>

      {formData.role === "CONTENT_CREATOR" && (
        <>
          <textarea
            placeholder="Bio"
            onChange={(e) => setFormData({ ...formData, bio: e.target.value })}
          />
          <input type="file" onChange={handleFileChange} />
        </>
      )}

      {formData.role === "COMPANY" && (
        <>
          <input
            type="text"
            placeholder="Company Name"
            required
            onChange={(e) =>
              setFormData({ ...formData, companyName: e.target.value })
            }
          />
          <input
            type="text"
            placeholder="Business License"
            required
            onChange={(e) =>
              setFormData({ ...formData, businessLicense: e.target.value })
            }
          />
        </>
      )}

      <button type="submit" disabled={loading}>
        {loading ? "Registering..." : "Register"}
      </button>
      {message && <p>{message}</p>}
    </form>
  );
};

export default RegistrationForm;
