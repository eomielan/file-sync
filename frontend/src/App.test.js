import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import SenderPage from "./components/SenderPage";
import ReceiverPage from "./components/ReceiverPage";
import UploadPage from "./components/UploadPage";
import DownloadPage from "./components/DownloadPage";

describe("App Navigation", () => {
  test("renders SenderPage when navigating to '/'", () => {
    render(
      <MemoryRouter initialEntries={["/"]}>
        <Routes>
          <Route path="/" element={<SenderPage />} />
        </Routes>
      </MemoryRouter>
    );
    const senderHeading = screen.getByText(/Sender Page/i);
    expect(senderHeading).toBeInTheDocument();
  });

  test("renders ReceiverPage when navigating to '/receiver'", () => {
    render(
      <MemoryRouter initialEntries={["/receiver"]}>
        <Routes>
          <Route path="/receiver" element={<ReceiverPage />} />
        </Routes>
      </MemoryRouter>
    );
    const receiverHeading = screen.getByText(/Receiver Page/i);
    expect(receiverHeading).toBeInTheDocument();
  });

  test("renders UploadPage when navigating to '/upload'", () => {
    render(
      <MemoryRouter initialEntries={["/upload"]}>
        <Routes>
          <Route path="/upload" element={<UploadPage />} />
        </Routes>
      </MemoryRouter>
    );
    const uploadHeading = screen.getByText(/Upload to S3/i);
    expect(uploadHeading).toBeInTheDocument();
  });

  test("renders DownloadPage when navigating to '/download'", () => {
    render(
      <MemoryRouter initialEntries={["/download"]}>
        <Routes>
          <Route path="/download" element={<DownloadPage />} />
        </Routes>
      </MemoryRouter>
    );
    const downloadHeading = screen.getByText(/Download from S3/i);
    expect(downloadHeading).toBeInTheDocument();
  });
});
