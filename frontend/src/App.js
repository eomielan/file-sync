import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import SenderPage from "./components/SenderPage.js";
import ReceiverPage from "./components/ReceiverPage.js";
import styled from "styled-components";

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: Arial, sans-serif;
`;

const Navbar = styled.nav`
  background-color: #333;
  width: 100%;
  padding: 15px;
  display: flex;
  justify-content: center;
  gap: 30px;
`;

const StyledLink = styled(Link)`
  color: #ffffff;
  text-decoration: none;
  font-weight: bold;
  &:hover {
    color: #f0a500;
  }
`;

function App() {
  return (
    <Router>
      <Container>
        <Navbar>
          <StyledLink to="/">Sender</StyledLink>
          <StyledLink to="/receiver">Receiver</StyledLink>
        </Navbar>
        <Routes>
          <Route path="/" element={<SenderPage />} />
          <Route path="/receiver" element={<ReceiverPage />} />
        </Routes>
      </Container>
    </Router>
  );
}

export default App;
