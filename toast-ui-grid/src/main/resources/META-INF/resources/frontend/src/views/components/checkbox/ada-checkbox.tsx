import React, {ChangeEvent, createRef, useEffect} from "react";
import ReactDOMServer from "react-dom/server";
import {Input} from "@chakra-ui/react";
import {CellEditor, CellEditorProps} from 'tui-grid/types/editor';
import {CheckboxProps} from "@chakra-ui/checkbox/dist/checkbox";

interface CheckboxComponentProps extends CheckboxProps, CellEditorProps {
    container: any;
    options?: { maxLength?: number };
    value: string;
    handleChange: (event: ChangeEvent<HTMLInputElement>) => void;
    backgroundColor: string;
    width: number;
    height: number;
    border: string;
    outline: string;
    butBackground: string;
    opacity: any;
    size: any;
}

class CheckboxComponent implements CellEditor {
    public el: HTMLElement;
    private props: CheckboxComponentProps;

    constructor(props: CheckboxComponentProps) {
        this.props = props;
        this.el = this.renderCheckbox() as HTMLElement;
    }

    getElement() {
        return this.el;
    }

    getValue() {
        return (this.el.firstChild as HTMLInputElement).value;
    }

    mounted() {
        (this.el.firstChild as HTMLInputElement).select();
    }

    renderCheckbox() {
        const {
            value,
            handleChange,
            backgroundColor,
            opacity,
            width,
            height,
            size,
            border,
            options,
        } = this.props;

        const inputRef = createRef<HTMLInputElement>(); // Change the ref type to HTMLInputElement

        const inputElement = (
            <label className="checkbox tui-grid-row-header-checkbox">
                <input
                    ref={inputRef}
                    type="checkbox"
                    className="hidden-input"
                    defaultChecked={value}
                    onChange={handleChange}/>
                <span className="custom-input"></span>
            </label>

            // <Input
            //     ref={inputRef}
            //     type="checkbox"
            //     colorScheme="teal"
            //     defaultChecked={value}
            //     onChange={handleChange} // Add onChange prop instead of onBlur
            //     // Add other props as needed
            //     // ...
            // />
        );

        const parser = new DOMParser();
        const doc = parser.parseFromString(ReactDOMServer.renderToString(inputElement), 'text/html');
        const parentElement = doc.body.firstChild.parentNode;
        return doc.body.firstChild;
    }
}

export default CheckboxComponent;