import React, {ChangeEvent, FC} from "react";
import icon1 from "./images/icon1.png";

interface InputIconComponentProps {
    value: string;
    handleChange: (event: ChangeEvent<HTMLInputElement>) => void;
    backgroundColor: string;
    width: string;
    border: string;
    outline: string;
    butBackground: string;
}

const InputIconComponent: FC<InputIconComponentProps> = ({
                                                             value,
                                                             handleChange,
                                                             backgroundColor,
                                                             width,
                                                             border,
                                                             outline,
                                                             butBackground
                                                         }) => {
    return (
        <>
            <div>
                <input
                    type="text"
                    value={value}
                    onChange={handleChange}
                    style={{
                        width: `${width}`,
                        backgroundColor: `${backgroundColor}`,
                        border: `${border}`,
                        outline: `${outline}`
                    }}
                />
                <button style={{
                    border: `${border}`,
                    margin: '0',
                    padding: '0',
                    backgroundColor: `${butBackground}`,
                    outline: `${outline}`
                }}>
                    <img src={icon1} width={32} height={28} alt="icon"/>
                </button>
            </div>
        </>
    );
};

export default InputIconComponent;