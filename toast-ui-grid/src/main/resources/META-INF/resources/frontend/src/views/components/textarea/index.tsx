import React, {ChangeEvent, createRef} from "react";
import {Textarea, TextareaProps} from "@chakra-ui/react";
import {CellEditor, CellEditorProps, PortalEditingKeydown} from 'tui-grid/types/editor';
import TuiGrid, {ColumnInfo} from 'tui-grid';

interface TextareaComponentProps extends TextareaProps, CellEditorProps {
    grid: TuiGrid & { usageStatistics: boolean };
    value: string;
    handleInputChange: (event: ChangeEvent<HTMLTextAreaElement>) => void;
    PortalEditingKeydown: PortalEditingKeydown;
    columnInfo: ColumnInfo;
}

class TextareaComponent implements CellEditor {
    public el: HTMLTextAreaElement;
    private props: TextareaComponentProps;

    constructor(props: TextareaComponentProps) {
        this.props = props;
        this.el = document.createElement('textarea');
        this.el.value = props.value;
        this.el.style.height = 'auto'; // Set initial height to auto
        this.el.addEventListener('input', this.autoResize);
        this.el.addEventListener('blur', this.save);
    }

    getElement() {
        return this.el;
    }

    getValue(): string {
        console.log("Call getValue()1: ", this.el.value);
        console.log("Call getValue()2: ", this.el.value.replace(/\/n/g, "\n"));
        return this.el.value.replace(/\/n/g, "\n");
    }

    autoResize = (): void => {
        const newHeight = this.el.scrollHeight;
        this.el.style.height = 'auto'; // Reset height to auto
        this.el.style.height = this.el.scrollHeight + 'px'; // Set height based on content
        console.log("TextArea props: ", this.props.grid.store["rowCoords"]["heights"][this.props.grid.getFocusedCell()["rowKey"]]);
        console.log("TextArea columnInfo: ", this.props.grid.getFocusedCell());
        let row = this.props.grid.getRow(this.props.grid.getFocusedCell()["rowKey"]);
        // row.setHeight(newHeight); // Update row height
        row["_attributes"]["height"] = newHeight;
        // row._attributes.height = newHeight;
    }

    save = (): void => {
        this.props.grid.finishEditing(this.props.grid.getFocusedCell()["rowKey"], this.props.grid.getFocusedCell()["columnName"], this.el.value);
    }
}

export default TextareaComponent;
