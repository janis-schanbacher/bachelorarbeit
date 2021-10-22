import styled from "styled-components";
import { Layout } from "antd";
import { Link } from "react-router-dom";

const { Header, Content } = Layout;
export const AppWrapper = styled.div`
  text-align: center;
`;

export const StyledHeader = styled(Header)`
  background: white;
  border-bottom: 1px solid #f0f0f0;
`;

export const StyledContent = styled(Content)`
  padding: 0 50px;
`;

export const StyledLink = styled(Link)`
  float: left;
`;
