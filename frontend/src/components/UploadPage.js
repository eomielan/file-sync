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

function UploadPage() {
  const [file, setFile] = useState(null);
  const [bucketName, setBucketName] = useState("");
  const [fileName, setFileName] = useState("");

  const handleUpload = async () => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("bucketName", bucketName);
    formData.append("fileName", fileName);

    try {
      await axios.post("/file-transfer/upload", formData);
      alert("File uploaded to S3 successfully!");
    } catch (error) {
      alert("Error uploading file to S3.");
    }
  };

  return (
    <Card>
      <Title>Upload to S3</Title>
      <input
        type="file"
        onChange={(e) => setFile(e.target.files[0])}
        required
      />
      <Input
        type="text"
        placeholder="Bucket Name"
        value={bucketName}
        onChange={(e) => setBucketName(e.target.value)}
        required
      />
      <Input
        type="text"
        placeholder="File Name in S3"
        value={fileName}
        onChange={(e) => setFileName(e.target.value)}
        required
      />
      <Button onClick={handleUpload}>Upload File</Button>
    </Card>
  );
}

export default UploadPage;
