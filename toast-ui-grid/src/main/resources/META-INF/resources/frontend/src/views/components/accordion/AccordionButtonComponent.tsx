import { AccordionButton, AccordionIcon, Box } from "@chakra-ui/react";
import React, { FC } from "react";

interface AccordionButtonComponentProps {
  AccordionTitle: string;
  backgroundColor?: string;
}

const AccordionButtonComponent: FC<AccordionButtonComponentProps> = ({
  AccordionTitle,
  backgroundColor,
}) => {
  return (
    <AccordionButton
      _focusVisible={{ outline: "none" }}
      _hover={{ outline: "none" }}
      backgroundColor={backgroundColor}
    >
      <AccordionIcon />
      <Box as="span" flex="1" textAlign="left">
        {AccordionTitle}
      </Box>
    </AccordionButton>
  );
};

export default AccordionButtonComponent;