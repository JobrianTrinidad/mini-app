import React from "react";
import { ChevronDownIcon } from "@chakra-ui/icons";
import { Button, Menu, MenuButton, MenuItem, MenuList, ButtonProps } from "@chakra-ui/react";

interface MenuButtonComponentProps extends Omit<ButtonProps, "rightIcon"> {
  MenuButtonTitle: string;
}

function MenuButtonComponent({ MenuButtonTitle,backgroundColor, ...props }: MenuButtonComponentProps) {
  return (
      <MenuButton 
        as={Button} 
        rightIcon={<ChevronDownIcon />} 
        _focusVisible={{outline: 'none'}} 
        _hover={{outline: 'none'}}
        _active={{outline: 'none'}}
        borderRadius={0}
        color={'#fff'} 
        {...props}
        backgroundColor={backgroundColor}
      >
        {MenuButtonTitle}
      </MenuButton>
  );
}

export default MenuButtonComponent;