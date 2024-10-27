import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import SenderPage from "./components/SenderPage";
import ReceiverPage from "./components/ReceiverPage";

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
});
