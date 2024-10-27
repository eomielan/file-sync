import React, { useState } from "react";
import axios from "axios";
import styled from "styled-components";

const Card = styled.div`
  max-width: 400px;
  margin-top: 50px;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  background-color: #ffffff;
`;

const Title = styled.h2`
  color: #333;
  font-size: 1.8em;
  margin-bottom: 15px;
`;

const Input = styled.input`
  width: 100%;
  box-sizing: border-box;
  padding: 12px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1em;
  &:focus {
    border-color: #007bff;
  }
`;

const Button = styled.button`
  width: 100%;
  padding: 12px;
  background-color: #007bff;
  color: white;
  font-size: 1.1em;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #0056b3;
  }
`;

function SenderPage() {
  const [file, setFile] = useState(null);
  const [receiverHostname, setReceiverHostname] = useState("");
  const [receiverPort, setReceiverPort] = useState("");
  const [bytesToTransfer, setBytesToTransfer] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("file", file);
    formData.append("receiverHostname", receiverHostname);
    formData.append("receiverPort", receiverPort);
    formData.append("bytesToTransfer", bytesToTransfer);

    try {
      await axios.post("/file-transfer/upload", formData);
      alert("File sent successfully!");
    } catch (error) {
      alert("Error sending file.");
    }
  };

  return (
    <Card>
      <Title>Sender Page</Title>
      <form onSubmit={handleSubmit}>
        <input
          type="file"
          onChange={(e) => setFile(e.target.files[0])}
          required
        />
        <Input
          type="text"
          placeholder="Receiver Hostname"
          value={receiverHostname}
          onChange={(e) => setReceiverHostname(e.target.value)}
          required
        />
        <Input
          type="number"
          placeholder="Receiver Port"
          value={receiverPort}
          onChange={(e) => setReceiverPort(e.target.value)}
          required
        />
        <Input
          type="number"
          placeholder="Bytes to Transfer"
          value={bytesToTransfer}
          onChange={(e) => setBytesToTransfer(e.target.value)}
          required
        />
        <Button type="submit">Send File</Button>
      </form>
    </Card>
  );
}

export default SenderPage;
