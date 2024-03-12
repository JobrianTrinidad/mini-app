import React, { MouseEventHandler } from "react";
import { ChevronDownIcon } from "@chakra-ui/icons";
import { Button, MenuItem, MenuItemProps } from "@chakra-ui/react";

interface MenuItemComponentProps extends Omit<MenuItemProps, "rightIcon" | "onClick"> {
  MenuItemTitle: string;
  handleClick?: MouseEventHandler<HTMLButtonElement>;
}

function MenuItemComponent({ MenuItemTitle, handleClick,backgroundColor, ...props }: MenuItemComponentProps) {
  return (
    <MenuItem onClick={handleClick} backgroundColor={backgroundColor}  _focusVisible={{outline: 'none'}} _hover={{outline: 'none'}} {...props}>
      {MenuItemTitle}
    </MenuItem>
  );
}

export default MenuItemComponent;