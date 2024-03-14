import React, {Component, createRef} from "react";
import ReactDOMServer from "react-dom/server";
import {CellEditor} from 'tui-grid/types/editor';
import {Select} from "@chakra-ui/react";

interface Option {
    value: string;
    text: string;
}

interface DropDownProps {
    height: string | number,
    value: number,
    placeholder: string;
    columnInfo: {
        editor: {
            options: {
                listItems: Option[],
                backgroundColor: string,
                opacity: number,
                width: number | string;
                height: number | string;
                border: string;
                outline: string;
                butBackground: string;
                size: object;
            }
        }
    };
}

class DropDown implements CellEditor {
    public el: HTMLElement;
    private props: DropDownProps;

    constructor(props: DropDownProps) {
        this.props = props;
        this.el = this.renderDropDown();
    }

    getElement(): HTMLElement {
        return this.el;
    }

    getValue(): string {
        return (this.el as HTMLSelectElement).value;
    }

    mounted(): void {
        const selectElement: HTMLSelectElement = this.el as HTMLSelectElement;

        // Set the selectedIndex to the desired value
        selectElement.selectedIndex = this.props.value;
    }

    renderDropDown() {
        const {
            value: value,
            height,
            placeholder,
            columnInfo: {
                editor: {
                    options: {
                        listItems,
                        backgroundColor,
                        opacity,
                        width,
                        // height,
                        border,
                        outline,
                        butBackground,
                    }
                }
            }
        } = this.props;
        console.log("props: ", this.props);
        const inputRef = createRef<HTMLSelectElement>();
        const inputElement = (
                <Select
                    ref={inputRef}
                    placeholder={placeholder}
                    borderRadius={"5px"}
                    boxShadow={"none !important"}
                    style={{
                        width: width,
                        height: height,
                        opacity: opacity,
                        backgroundColor: backgroundColor,
                        border: border,
                        outline: outline,
                        butBackground: butBackground
                    }}
                >
                    {
                        listItems.map((item: Option) => (
                            <option key={item.value} value={item.value}>
                                {item.text}
                            </option>
                        ))
                    }
                </Select>
            )
        ;
        const parser: DOMParser = new DOMParser();
        const doc: Document = parser.parseFromString(ReactDOMServer.renderToString(inputElement), 'text/html');
        const parentNode: HTMLElement = doc.body.firstChild as HTMLElement;
        parentNode.style.display = 'flex';
        return doc.body.firstChild.firstChild as HTMLElement;
    }
}

export default DropDown;