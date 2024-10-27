import { render, screen } from "@testing-library/react";
import App from "./App.js";

test("renders Sender Page heading", () => {
  render(<App />);
  const headingElement = screen.getByText(/Sender Page/i);
  expect(headingElement).toBeInTheDocument();
});
