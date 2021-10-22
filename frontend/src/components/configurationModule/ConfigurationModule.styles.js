import styled from "styled-components";
import { Button, Checkbox, Typography } from "antd";

const { Title } = Typography;

export const StyledButton = styled(Button)`
  margin: 10px 0;
  float: left;
`;

export const StyledCheckboxGroup = styled(Checkbox.Group)`
  text-align: left;
  width: 100%;
  margin: 10px 0;
`;

export const StyledTitle = styled(Title)`
  text-align: left;
`;
