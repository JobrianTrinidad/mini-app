import React, {ChangeEvent, createRef} from "react";
import ReactDOMServer from "react-dom/server";
import {Input} from "@chakra-ui/react";
import {CellEditor, CellEditorProps} from 'tui-grid/types/editor';
import {HTMLChakraProps} from "@chakra-ui/system";
import {InputProps} from "@chakra-ui/input/dist/input";
import TuiGrid, {ColumnInfo} from 'tui-grid';

export interface InputComponentProps extends InputProps, CellEditorProps {
    grid: TuiGrid & { usageStatistics: boolean };
    value: string;
    handleChange: (event: ChangeEvent<HTMLInputElement>) => void;
    columnInfo: ColumnInfo
}

class InputComponent implements CellEditor {
    public el: ChildNode;
    private props: InputComponentProps;

    constructor(props: InputComponentProps) {
        this.props = props;
        this.el = this.renderInput();
    }

    getElement(): ChildNode {
        return this.el;
    }

    getValue(): string {
        return (this.el as HTMLInputElement).value;
    }

    mounted(): void {
        (this.el as HTMLInputElement).select();
    }

    renderInput(): ChildNode {
        const {
            grid,
            value,
            handleChange,
            columnInfo
        } = this.props;

        const inputRef = createRef<HTMLInputElement>();
        const inputElement = (
            <Input
                ref={inputRef}
                defaultValue={value?.toString()}
                type="text"
                maxLength={columnInfo.editor.options.maxLength}
                onChange={handleChange}
                size={columnInfo.editor.options.size}
                style={{
                    backgroundColor: columnInfo.editor.options.backgroundColor,
                    opacity: columnInfo.editor.options.opacity,
                    width: columnInfo.editor.options.width,
                    height: columnInfo.editor.options.height,
                    border: columnInfo.editor.options.border,
                    outline: columnInfo.editor.options.outline,
                }}
                _focusVisible={{outline: "none"}}
                _hover={{outline: "none"}}
            />
        );
        const parser: DOMParser = new DOMParser();
        const doc: Document = parser.parseFromString(ReactDOMServer.renderToString(inputElement), 'text/html');
        const parentElement: ParentNode = doc.body.firstChild.parentNode;
        return doc.body.firstChild;
    }
}

export default InputComponent;