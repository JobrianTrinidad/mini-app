import React, {MouseEventHandler} from "react";
import {Button, ButtonProps} from "@chakra-ui/react";

interface ButtonComponentProps extends Omit<ButtonProps, "onClick"> {
    handleClick: MouseEventHandler<HTMLButtonElement>;
    buttonTitle: string;
}

function ButtonComponent({
                             handleClick,
                             buttonTitle,
                             backgroundColor,
                             borderRadius,
                             width,
                             ...props
                         }: ButtonComponentProps) {
    return (
        <Button
            onClick={handleClick}
            size="sm"
            backgroundColor={backgroundColor}
            borderRadius={borderRadius}
            width={width}
            _focusVisible={{outline: "none"}}
            _hover={{outline: "none"}}
            {...props}
        >
            {buttonTitle}
        </Button>
    );
}

export default ButtonComponent;