import React, { useState } from "react";
import styled from "styled-components";
import axios from "axios";

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
  padding: 12px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1em;
  &:focus {
    border-color: #28a745;
  }
`;

const Button = styled.button`
  width: 100%;
  padding: 12px;
  background-color: #28a745;
  color: white;
  font-size: 1.1em;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #1c7c2a;
  }
`;

function ReceiverPage() {
  const [port, setPort] = useState("");
  const [filename, setFilename] = useState("");
  const [fileStorageLocation, setFileStorageLocation] =
    useState("/default/downloads"); // default storage path

  const handleStartReceiver = async () => {
    try {
      const response = await axios.get("/file-transfer/receive", {
        params: {
          filename,
          port,
        },
        headers: {
          fileStorageLocation,
        },
        responseType: "blob", // Download file as a binary stream
      });

      // Create a URL for the received file and prompt download
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", filename); // Use filename specified by user
      document.body.appendChild(link);
      link.click();
      link.remove();

      alert(`File ${filename} received and downloaded successfully.`);
    } catch (error) {
      console.error("Error receiving file:", error);
      alert(
        "Failed to receive the file. Please check the server and parameters."
      );
    }
  };

  return (
    <Card>
      <Title>Receiver Page</Title>
      <Input
        type="number"
        placeholder="Port"
        value={port}
        onChange={(e) => setPort(e.target.value)}
        required
      />
      <Input
        type="text"
        placeholder="Filename to Save"
        value={filename}
        onChange={(e) => setFilename(e.target.value)}
        required
      />
      <Input
        type="text"
        placeholder="File Storage Location"
        value={fileStorageLocation}
        onChange={(e) => setFileStorageLocation(e.target.value)}
        required
      />
      <Button onClick={handleStartReceiver}>Start Receiver</Button>
    </Card>
  );
}

export default ReceiverPage;
