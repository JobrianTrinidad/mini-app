//To use this code, you would need to have the required dependencies installed
// and set up a container element in your HTML.
// Then, you can call the functions of the toastuigrid object to
// create, update, and manipulate the grid component.

import "@vaadin/button";
import "@vaadin/text-field";
import type {JSX} from 'react';
import React from 'react';
import {createRoot, Root} from 'react-dom/client';
import InputComponent from "../components/input/ada-input";
import {CheckboxRenderer, RowNumberRenderer} from '../renderer/renderer';
import DropDown from "../components/dropdown/index";
import FeatureTable from "../components/Table/FeaturesTable";
import TuiGrid, {ColumnInfo, FilterState, Row, RowKey, ModifiedRows} from 'tui-grid';
import ContextMenu from 'tui-context-menu';
import {OptColumn, OptRow} from 'tui-grid/types/options';
import {TuiGridEvent} from "tui-grid/types/event";
import {Cell} from "../components/Table/ExcelSheet";
import TextareaComponent from "../components/textarea";

declare global {
    interface Window {
        toastuigrid: { [key: string]: any };
    }
}

type MenuItem = {
    title?: string;
    command?: string;
    menu?: MenuItem[];
    separator?: boolean;
    disable?: boolean;
};

type FilterValue = {
    colName: string;
    filter: string;
}

window.toastuigrid = {
    // This function is responsible for creating a grid component.
    // It takes a container element, JSON data for items, JSON data for options, and an unused parameter _.
    // The function parses the JSON data, sets up event handlers, creates an instance of the FeatureTable component,
    // and updates the grid.
    _createGrid: function (container: HTMLElement & {
        $server: any,
        grid: JSX.Element & {
            table: TuiGrid
        },
        filterId: string,
    }, itemsJson: string, optionsJson: string, filterId: string): void {
        let parsedItems: OptRow[] = JSON.parse(itemsJson);
        console.log("parsedItems: ", parsedItems);
        let parsedOptions = JSON.parse(optionsJson);
        console.log(parsedOptions, "parsedOptions")
        let bAllowDelete: boolean = parsedOptions.allowDelete;
        let bAllowInsert: boolean = parsedOptions.allowInsert;
        let multiSelectStatus: boolean = parsedOptions.multiSelectStatus;
        console.log(multiSelectStatus, "multiSelectStatus ccccccccccccc")

        let editingRowKey: string | number = -1;
        const {columns, contextMenus, filterValues} = this.getColumns(container, JSON.parse(parsedOptions.columns));
        console.log("Columns: ", columns);
        let prevColumnName: string = "";
        let gridInst: TuiGrid;
        let rangeSelected: number[] = [];
        let resizedColumn: { columnName: string, width: number } | null = null;
        if (container && container.grid)
            return;

        let hostElement: Element | null = document.querySelector('vaadin-app-layout');
        let shadowRoot: ShadowRoot | null = hostElement!.shadowRoot;
        let contentElement: Element | null = shadowRoot!.querySelector('[content]');
        let toolbars: HTMLCollectionOf<Element> = document.getElementsByClassName("aat-toolbar");
        let bodyHeight: number = contentElement!.getBoundingClientRect().height;
        let contextMenu: ContextMenu;
        for (const toolbar of toolbars) {
            bodyHeight -= toolbar.getBoundingClientRect().height;
        }

        const onSelection = (ev: TuiGridEvent): void => {
            rangeSelected = [];
            const data = gridInst.getData();

            for (let i: number = Number(ev.range.row[0]); i <= Number(ev.range.row[1]); i++) {
                rangeSelected.push(Number(data[i]['rowKey']));
            }
        }
        const onCheck = (ev: TuiGridEvent): void => {
            let checkedRows: number[] = [];

            if (!multiSelectStatus) {
                const updatedData = gridInst.getData().map((row, rowIndex) => {
                    if (ev.rowKey === rowIndex) {
                        checkedRows.push(ev.rowKey);
                        return row
                    }
                    return { ...row, _attributes: { checked: false } };
                });
                gridInst.resetData(updatedData);
            } else {
                for (const row of gridInst.getCheckedRows()) {
                    checkedRows.push(row["id"]);
                }
                checkedRows.push(ev["rowKey"]);
            }
            container.$server.onCheck(checkedRows);
        };
        const onUncheck = (ev: TuiGridEvent): void => {
            let checkedRows: number[] = [];
            for (const row of gridInst.getCheckedRows()) {
                checkedRows.push(row["id"]);
            }
            container.$server.onCheck(checkedRows);
        };
        const onCheckAll = (ev: TuiGridEvent): void => {
            let checkedRows: number[] = [];

            if (!multiSelectStatus) {
                const updatedData = gridInst.getData().map((row, rowIndex) => {
                    return { ...row, _attributes: { checked: false } };
                });
                gridInst.resetData(updatedData);
            } else {
                for (const row of gridInst.getCheckedRows()) {
                    checkedRows.push(row["id"]);
                }
            }

            container.$server.onCheck(checkedRows);
        };
        const onUncheckAll = (ev: TuiGridEvent): void => {
            let cleanedObject = JSON.parse(JSON.stringify(ev, (key: string, value): null | string => {
                if (value instanceof Node) {
                    return null;
                }
                return value;
            }))
            container.$server.onUncheckAll(cleanedObject);
        };
        const onAfterChange = (ev: TuiGridEvent): void => {
            let cleanedObject: {
                changes: [{
                    columnName: String,
                    rowKey: number,
                    value: String
                }],
                record: Row
            } = JSON.parse(JSON.stringify(ev, (key: string, value): null | string => {
                if (value instanceof Node) {
                    return null;
                }
                return value;
            }));

            if (ev.changes.length > 0) {
                if (gridInst) {
                    let record: {} = {};
                    for (const column of columns) {
                        let key: string = column.name;
                        record = {...record, [key]: gridInst.getValue(ev.changes[0]['rowKey'], column.name)}
                    }
                    cleanedObject = {...cleanedObject, record: record};
                    this.onUpdateData(container, cleanedObject);
                }
            }
        };
        const onColumnResize = (ev: TuiGridEvent): void => {
            resizedColumn = ev.resizedColumns[0];
        };
        const onFocusChange = (ev: TuiGridEvent): void => {
            container.$server.setSelectItem(ev.columnName, container.grid.table.getRow(ev.rowKey)["id"]);
        };
        const handleSearchResult = (result: Cell): void => {
            gridInst = container.grid.table;
            gridInst.focusAt(result.row, result.column);
        }
        const handleKeyDown = (event: KeyboardEvent): void => {
            gridInst = container.grid.table;
            if (event.shiftKey === false
                && event.altKey === false
                && event.ctrlKey === false
                && (event.code === "ArrowDown"
                    || event.code === "ArrowUp"
                    || event.code === "ArrowLeft"
                    || event.code === "ArrowRight"))
                rangeSelected = [];
            else if (event.shiftKey === true
                && event.code === "Delete") {
                if (gridInst.getCheckedRowKeys().length > 0 && bAllowDelete) {
                    rangeSelected = [];
                    let checkedRows: number[] = [];
                    for (const row of gridInst.getCheckedRows()) {
                        checkedRows.push(row["id"]);
                    }
                    container.$server.deleteItems(checkedRows);
                }
            } else if (event.shiftKey === true
                && event.code === "Space") {
                event.preventDefault();
                if (rangeSelected.length === 0) {
                    let selectedIndex: string | number | null = gridInst.getFocusedCell()["rowKey"];
                    if (gridInst.getCheckedRowKeys().includes(selectedIndex))
                        gridInst.uncheck(selectedIndex);
                    else
                        gridInst.check(selectedIndex);
                } else {
                    let bChecked: boolean = true;
                    for (let i: number = 0; i <= rangeSelected.length; i++) {
                        if (gridInst.getCheckedRowKeys().includes(i))
                            bChecked = false;
                    }
                    for (let i: number = 0; i <= rangeSelected.length; i++) {
                        if (bChecked)
                            gridInst.check(rangeSelected[i]);
                        else {
                            gridInst.uncheck(rangeSelected[i]);
                        }
                    }
                }
            } else if (event.shiftKey === true
                && event.code === "Insert") {
                if(bAllowInsert){
                    event.preventDefault();
                    this.onAddRecord(container);
                }
            } else if (parsedOptions.autoSave === true
                && event.code === "Tab"
                && gridInst.getFocusedCell()['rowKey'] === gridInst.getRowCount() - 1
                && prevColumnName === columns[columns.length - 1].name) {
                prevColumnName = columns[0].name;
                let row: Row = gridInst.getRow(gridInst.getFocusedCell()['rowKey']);
                for (const column of columns) {
                    if (!(row[column.name] === "" || row[column.name] === null)) {
                        container.$server.addItem();
                        break;
                    }
                }
            } else if (gridInst)
                prevColumnName = gridInst.getFocusedCell()['columnName'];
        };

        const handleMouseDown = (event: MouseEvent): void => {

            if (event.button === 2) {
                let contextElement: HTMLElement = document.querySelector('.tui-contextmenu') as HTMLElement;
                let targetElement: HTMLElement = event.target as HTMLElement;
                let rectTarget: DOMRect = targetElement.getBoundingClientRect();
                if (contextElement) {
                    contextElement.style.left = (rectTarget.right - event.clientX) + "px";
                }
                for (const contextMenu1 of contextMenus) {
                    let element: Element | null = document.querySelector(`[data-column-name="${contextMenu1.title}"]`);
                    if (element !== null) {
                        let rect: DOMRect = element.getBoundingClientRect();
                        let left: number = rect.left + window.scrollX;

                        if (event.clientX >= left && event.clientX < rect.right) {

                            contextMenu.register("#target", (e: PointerEvent, cmd: string) =>
                                this._processContextMenu(e, cmd, filterValues, container), contextMenu1.menu);
                        }
                    }
                }
                return;
            }

            gridInst = container.grid.table;
            editingRowKey = gridInst.getFocusedCell()['rowKey'];
            const targetElement: HTMLElement = event.target as HTMLElement;
            if (targetElement.tagName === "VAADIN-APP-LAYOUT" || targetElement.tagName === "DIV") {
                gridInst.finishEditing(editingRowKey, prevColumnName);

                for (const column of columns) {
                    if (!(gridInst.getValue(editingRowKey, column.name) === "" ||
                        gridInst.getValue(editingRowKey, column.name) === null)) {
                        return
                    }
                }
                gridInst.removeRow(editingRowKey);
                editingRowKey = -1;
            }

            if (!event.shiftKey
                && !targetElement.className.includes('tui-grid-cell-header'))
                rangeSelected = [];
        };

        const handleMouseUp = (event: MouseEvent): void => {
            if (resizedColumn !== null) {
                container.$server.resizeColumn(resizedColumn.columnName, resizedColumn.width);
                resizedColumn = null;
            }
        }
        const handleGetGridInstance = (gridInstance: TuiGrid): void => {
            gridInst = gridInstance;
            if (Object.isExtensible(container.grid)) {
                container.grid.table = gridInstance;
            } else {
                container.grid = {
                    ...container.grid,
                    table: gridInstance,
                };
            }
        };

        container.grid = (
            <FeatureTable
                getGridInstance={handleGetGridInstance}
                el={document.getElementsByClassName("grid")[0]}
                TableData={this.getTableData(parsedItems)}
                columns={columns}
                // contextMenu={contextMenu}
                summary={this.getSummary(parsedOptions.summary)}
                columnOptions={parsedOptions.columnOptions}
                header={this.getHeader(parsedOptions.header)}
                width={parsedOptions.width}
                // bodyHeight={(parsedOptions.header.height && parsedOptions.header.height > 0)
                //     ? bodyHeight - parsedOptions.header.height
                //     : bodyHeight - 100}
                bodyHeight={"fitToParent"}
                scrollX={parsedOptions.scrollX}
                scrollY={parsedOptions.scrollY}
                // minRowHeight={"90%"}
                // minBodyHeight={120}
                rowHeaders={parsedOptions.rowHeaders ? this.getRowHeaders(parsedOptions.rowHeaders) : null}
                treeColumnOptions={parsedOptions.treeColumnOptions ? JSON.parse(parsedOptions.treeColumnOptions) : null}
                onAfterChange={onAfterChange}
                onColumnResize={onColumnResize}
                onSelection={onSelection}
                onCheck={onCheck}
                onCheckAll={onCheckAll}
                onUncheck={onUncheck}
                onUncheckAll={onUncheckAll}
                onFocusChange={onFocusChange}
                handleSearchResult={handleSearchResult}
            ></FeatureTable>
        );

        container.filterId = filterId;

        document.addEventListener("keydown", handleKeyDown);
        document.addEventListener("mousedown", handleMouseDown);
        document.addEventListener("mouseup", handleMouseUp);

        this.updateGrid(container);

        let contextMenusAdded: MenuItem[];
        if (parsedOptions.contextmenu)
            contextMenusAdded = this.convertToMenuItem(JSON.parse(parsedOptions.contextmenu));
        setTimeout((): void => {
            contextMenu = this.createContextMenu(container, contextMenusAdded);
        });
    },

//This function is a wrapper around _createGrid that delays the execution using setTimeout.
// It takes a container element, JSON data for items, and JSON data for options.
    _setColumnContentMatchedName(columnContent: any): void {
        const onSum = (): { template: (valueMap: any) => string } => {
            return {
                template: (valueMap: any): string => {
                    return `Sum: ${valueMap.sum}`;
                }
            }
        };
        const onAvg = (): { template: (valueMap: any) => string } => {
            return {
                template: (valueMap: any): string => {
                    return `AVG: ${valueMap.avg.toFixed(2)}`;
                }
            }
        };
        const onMax = (): { template: (valueMap: any) => string } => {
            return {
                template: (valueMap: any): string => {
                    return `MAX: ${valueMap.max}`;
                }
            }
        };
        const onMin = (): { template: (valueMap: any) => string } => {
            return {
                template: (valueMap: any): string => {
                    return `MIN: ${valueMap.min}`;
                }
            }
        };
        const onRowCount = (): { template: (valueMap: any) => string } => {
            return {
                template: (valueMap: any): string => {
                    return `Row Count: ${valueMap.cnt}`;
                }
            }
        };

        switch (columnContent[Object.keys(columnContent)[0]]) {
            case "avg" :
                columnContent[Object.keys(columnContent)[0]] = onAvg();
                break;
            case "max" :
                columnContent[Object.keys(columnContent)[0]] = onMax();
                break;
            case "min" :
                columnContent[Object.keys(columnContent)[0]] = onMin();
                break;
            case "rowcount" :
                columnContent[Object.keys(columnContent)[0]] = onRowCount();
                break;
            case "sum" :
            default:
                columnContent[Object.keys(columnContent)[0]] = onSum();
                break;
        }
    },

    defaultContextMenu: function (cmd: string
        , container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }): void {
        const focusedCell = container.grid.table.getFocusedCell();
        if (!focusedCell || !focusedCell.columnName) {
            return;
        }

        const startColumnIndex = container.grid.table.getColumns().findIndex((column: ColumnInfo): boolean => column.name === focusedCell.columnName);
        switch (cmd) {
            case "copy":
                container.grid.table.copyToClipboard();
                break;
            case "copyColumns":
                container.grid.table.setSelectionRange({
                    start: [0, startColumnIndex],
                    end: [container.grid.table.getRowCount() - 1, startColumnIndex]
                });
                container.grid.table.copyToClipboard();
                break;
            case "copyRows":
                container.grid.table.setSelectionRange({
                    start: [Number(focusedCell["rowKey"]), 0],
                    end: [Number(focusedCell["rowKey"]), container.grid.table.getColumns().length - 1]
                });
                container.grid.table.copyToClipboard();
                break;
            case "csvExport":
                container.grid.table.export('csv');
                break;
            case "excelExport":
                container.grid.table.export('xlsx');
                break;
            case "txtExport":
                container.grid.table.export('txt');
                break;
        }
    },

    _processContextMenu(e: PointerEvent, cmd: string, filterValues: FilterValue[]
        , container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }): void {
        let strArrayTemp: string[] = cmd.split("-");
        let tempFilterValues: FilterValue[] = filterValues;

        container.grid.table.setFilter(strArrayTemp[0], "select");
        let filterState: FilterState = {
            code: strArrayTemp[1] !== "Select" ? "eq" : "ne",
            value: strArrayTemp[1]
        };

        for (const tempFilterValue of tempFilterValues) {
            if (tempFilterValue.colName === strArrayTemp[0])
                tempFilterValue.filter = strArrayTemp[2];
        }
        filterValues = tempFilterValues;
        container.grid.table.filter(strArrayTemp[0], [filterState]);

        this.validateColumn(container, filterValues);
    },

    setFilter(colName: string, filter: string,
              container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }): void {
        setTimeout(() => {
            container.grid.table.setFilter(colName, "select");
            let filterState: FilterState = {
                code: filter !== "Select" ? "eq" : "ne",
                value: filter
            };
            container.grid.table.filter(colName, [filterState]);
            this.validateColumn(container, [{colName, filter}]);
        }, 300);
    },

    setDateFilter(colName: string, startDate: string, endDate: string,
                  container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }): void {
        setTimeout(() => {
            container.grid.table.setFilter(colName, "date");

            const start_date = new Date(startDate);
            const end_date = new Date(endDate);
            let filterStates: FilterState[] = [];

            let filterState1: FilterState = {
                code: "afterEq",
                value: start_date.toDateString()
            };
            if (startDate !== null && startDate !== "")
                filterStates.push(filterState1);

            let filterState2: FilterState = {
                code: "beforeEq",
                value: end_date.toDateString()
            };
            if (endDate !== null && endDate !== "")
                filterStates.push(filterState2);

            container.grid.table.filter(colName, filterStates);
        }, 300);
    },

    convertToMenuItem(menus: any[]): MenuItem[] {
        return menus.map((menu) => {
            if (typeof menu === 'string') {
                menu = JSON.parse(menu);
            }
            if (menu.menu && typeof menu.menu === 'string') {
                menu.menu = this.convertToMenuItem(JSON.parse(menu.menu));
            }
            return menu as MenuItem;
        });
    },

    createContextMenu(container: HTMLElement &
                          { $server: any, grid: JSX.Element & { table: TuiGrid } }
        , contextMenusAdded: MenuItem[]): ContextMenu {
        let contextMenu: ContextMenu = new ContextMenu(document.querySelector("#container"));
        let defaultContextMenu: MenuItem[] = [{title: 'Copy', command: 'copy'},
            {title: 'CopyColumns', command: 'copyColumns'},
            {title: 'CopyRows', command: 'copyRows'},
            {separator: true},
            {
                title: 'Expert',
                menu: [
                    {title: 'CsvExport', command: 'csvExport'},
                    {title: 'ExcelExport', command: 'excelExport'},
                    {title: 'TxtExport', command: 'txtExport'}
                ]
            }];
        if (contextMenusAdded === null || contextMenusAdded === undefined) {
            contextMenu.register("#target", (e: PointerEvent, cmd: string): void => {
                container.$server.onContextMenuAction(cmd, {});
            }, defaultContextMenu);
            return contextMenu;
        }
        for (const defaultContextMenu1 of defaultContextMenu) {
            contextMenusAdded.push(defaultContextMenu1);
        }

        contextMenu.register("#target", (e: PointerEvent, cmd: string): void => {
            let rowKey: RowKey = container.grid.table.getFocusedCell()['rowKey'] || 0;
            container.$server.onContextMenuAction(cmd, container.grid.table.getRow(rowKey))
        }, contextMenusAdded);

        return contextMenu;
    },

//This function updates the table data of an existing grid. It takes a container element with a grid property,
// and JSON data for the new data. The function updates the table data, and then updates the grid.
    onAddRecord(container: HTMLElement & {
        $server: any,
        grid: JSX.Element & { table: TuiGrid },
        filterId: string,
    }): void {
        let gridInst: TuiGrid = container.grid.table;
        let row: OptRow = {};
        let position: Number = 1;
        if (gridInst.getFilterState() !== null) {
            for (const filterValue of gridInst.getFilterState()) {
                switch (filterValue.type) {
                    case 'select':
                        row = {...row, [filterValue.columnName]: `${container.filterId}`};
                        break;
                    case 'text':
                    case 'number':
                    default:
                        row = {
                            ...row,
                            [filterValue.columnName]: filterValue.state[0] ? filterValue.state[0].value : ""
                        };
                }
            }
        }
        gridInst.appendRow(row);
        if (gridInst.getFilterState() !== null) {
            position = gridInst.getFilteredData().length - 1;
        } else {
            position = gridInst.getData().length - 1;
        }
        gridInst.startEditingAt(position, 0);
        container.$server.onAddRecord({data: row, rowIndex: gridInst.getFocusedCell()["rowKey"]});
    },

    onEnable(container: HTMLElement & {
        $server: any,
        grid: JSX.Element & { table: TuiGrid },
    }): void {
        setTimeout((): void => {
            container.grid.table.enable();
        }, 300);
    },

    onDisable(container: HTMLElement & {
        $server: any,
        grid: JSX.Element & { table: TuiGrid },
    }): void {
        setTimeout((): void => {
            // container.grid.table.disable();
            for (const columnInfo of container.grid.table.getColumns()) {
                container.grid.table.disableColumn(columnInfo.name);
            }
        }, 300);
    },

    onDisableFieldsAsReadOnly(container: HTMLElement & {
       $server: any,
       grid: JSX.Element & { table: TuiGrid },
    }, fieldsAsReadOnly: string): void {
       setTimeout((): void => {
           for (const fieldName of JSON.parse(fieldsAsReadOnly)) {
               container.grid.table.disableColumn(fieldName.name);
           }
       }, 300);
    },

    validateColumn(container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } },
                   filterValues: FilterValue[]): void {
        let gridInst: TuiGrid = container.grid.table;
        let columns: OptColumn[] = gridInst.getColumns();
        for (const column of columns) {
            for (const filterValue of filterValues) {
                if (column.name === filterValue.colName && filterValue.filter !== "All") {
                    gridInst.disableColumn(column.name);
                    break;
                }
            }
            if (gridInst.getFilterState() === null)
                gridInst.enableColumn(column.name);
        }
    },
//This function adds new data to the existing table data of a grid.
// It takes a container element with a grid property, and JSON data for the new data.
// The function appends the new data to the existing table data, and then updates the grid.
    create(container: HTMLElement, itemsJson: string, optionsJson: string, filterId: string): void {
        setTimeout(() => this._createGrid(container, itemsJson, optionsJson, filterId));
    },

    getColumns(container: HTMLElement & { $server: any }, parsedColumn: OptColumn[]):
        {
            columns: OptColumn[],
            contextMenus: ContextMenu[],
            filterValues: FilterValue[]
        } {
        let columns: OptColumn[] = parsedColumn;
        let tempColumns: OptColumn[] = [{name: "id", hidden: true}];
        let contextMenus: ContextMenu[] = [{title: 'Copy', command: 'copy'},
            {title: 'CopyColumns', command: 'copyColumns'},
            {title: 'CopyRows', command: 'copyRows'},
            {separator: true},
            {
                title: 'Expert',
                menu: [
                    {title: 'CsvExport', command: 'csvExport'},
                    {title: 'ExcelExport', command: 'excelExport'},
                    {title: 'TxtExport', command: 'txtExport'}
                ]
            }];

        let filterValues: FilterValue[] = [];

        const onLoadModifiedInfo = (row: Row, colName: String, value: boolean): void => {
            let cleanedObject: {
                changes: [{
                    columnName: String,
                    rowKey: number,
                    value: String
                }],
                record: Row
            } = {
                changes: [{
                    columnName: colName,
                    rowKey: row.rowKey,
                    value: `${value}`
                }],
                record: row
            };
            this.onUpdateData(container, cleanedObject);
        };

        for (let column of columns) {
            if (column.editor && column.editor.type === "input") {
                if (column.whiteSpace !== "nowrap")
                    column.editor.type = TextareaComponent;
                else
                    column.editor.type = InputComponent;
            } else if (column.editor && column.editor.type === "check") {
                column = {
                    header: column.header,
                    name: column.name,
                    renderer: {
                        type: CheckboxRenderer,
                        className: "tui-grid-checkbox",
                        callback: onLoadModifiedInfo,
                        options: {
                            checkedTemplate: 'true',
                            uncheckedTemplate: 'false',
                        },
                    },
                }
            }

            if (column.hasOwnProperty('editor') &&
                column.editor.hasOwnProperty('options') &&
                !column.editor.options.hasOwnProperty('maxLength')) {

                column.editor.options = JSON.parse(column.editor.options);

                if (column.editor.options.hasOwnProperty('fromYear') &&
                    parseInt(column.editor.options.fromYear) > 0) {
                    const fromYear: number = parseInt(column.editor.options.fromYear);
                    const fromMonth: number = parseInt(column.editor.options.fromMonth);
                    const fromDay: number = parseInt(column.editor.options.fromDay);
                    const toYear: number = parseInt(column.editor.options.toYear);
                    const toMonth: number = parseInt(column.editor.options.toMonth);
                    const toDay: number = parseInt(column.editor.options.toDay);
                    column.editor.options = {
                        selectableRanges: [[new Date(fromYear, fromMonth - 1, fromDay), new Date(toYear, toMonth - 1, toDay)]]
                    };
                }
            }

            if (column.editor && column.editor.type === "select") {
                const tempColumn = {
                    header: column.header,
                    name: column.name,
                    whiteSpace: column.whiteSpace,
                    align: column.align,
                    formatter: "listItemText",
                    editor: {
                        type: DropDown,
                        options: {
                            ...column.editor.options,
                            listItems: column["depth0"] ? JSON.parse(column["depth0"]) : []
                        }
                    },
                    ...(column["depth1"] != "[]" && {
                        relations: [{
                            targetNames: [column.targetNames],
                            listItems({value}: any) {
                                return column["depth1"][value] ? JSON.parse(column["depth1"][value]) : [];
                            },
                            disabled({value}: any) {
                                return !value;
                            }
                        }]
                    })
                };
                let items: MenuItem[] = [];
                for (const listItem of tempColumn.editor.options.listItems) {
                    let item: MenuItem = {
                        title: listItem.text !== "Select" ? listItem.text : "All",
                        command: listItem.text !== "Select" ?
                            tempColumn.name + "-" + listItem.text + "-" + listItem.value :
                            tempColumn.name + "-" + listItem.text + "-" + "All"

                    };
                    items.push(item);
                }
                items.push({separator: true},
                    {title: 'Copy', command: 'copy'},
                    {title: 'CopyColumns', command: 'copyColumns'},
                    {title: 'CopyRows', command: 'copyRows'},
                    {separator: true},
                    {
                        title: 'Expert',
                        menu: [
                            {title: 'CsvExport', command: 'csvExport'},
                            {title: 'ExcelExport', command: 'excelExport'},
                            {title: 'TxtExport', command: 'txtExport'}
                        ]
                    });
                // let contextMenu: MenuItem = {
                //     title: tempColumn.name,
                //     menu: items
                // }
                let filterValue: FilterValue = {
                    colName: tempColumn.name,
                    filter: "All"
                };
                tempColumns.push(tempColumn);
                // contextMenus.push(contextMenu);
                filterValues.push(filterValue);
            } else
                tempColumns.push(column);
        }
        return {
            columns: tempColumns,
            contextMenus: contextMenus,
            filterValues: filterValues,
        };
    },

    onUpdateData(container: HTMLElement & { $server: any },
                 cleanedObject: {
                     changes: [{
                         columnName: String,
                         rowKey: number,
                         value: String
                     }],
                     record: Row
                 }): void {
        container.$server.onUpdateData(cleanedObject);
    },

//This internal function is used to set the column content based on a matched name.
// It takes an object columnContent and modifies it based on its value.
// The modified object is used for displaying summary information in the grid.
    getComplexColumns(parsedColumnContent: any) {
        let complexColumns = JSON.parse(parsedColumnContent);
        for (const complexColumn of complexColumns) {
            complexColumn.childNames = complexColumn.childNames.slice(1, -1).split(", ").map((item: any) => item.trim());
        }
        return complexColumns;
    }
    ,
//This function parses the JSON data for the header and returns the header object.
// If the header does not have complex columns, it returns null.
// Otherwise, it constructs the header object with the specified height and complex columns.
    getData(container: HTMLElement & { $server: any, grid: JSX.Element & { table: TuiGrid } }): void {
        let cleanedObject = JSON.parse(JSON.stringify(container.grid.table, (key, value) => {
            if (value instanceof Node) {
                return null; // Remove the DOM node reference
            }
            return value;
        }))
        container.$server.onGetData(cleanedObject);
    },
    //This function retrieves the data from the grid and sends it to the server using the onGetData function.
    getHeader(parsedHeader: { height?: number, complexColumns: any[] }): {
        height?: number, complexColumns: any[]
    } | null {
        if (!parsedHeader.hasOwnProperty('complexColumns'))
            return null;
        else {
            return parsedHeader.height != 0 ? {
                height: parsedHeader.height,
                complexColumns: this.getComplexColumns(parsedHeader.complexColumns)
            } : {
                complexColumns: this.getComplexColumns(parsedHeader.complexColumns)
            };
        }
    }
    ,
//This function recursively parses the JSON data for the table data and returns the parsed data.
// It handles nested data structures by recursively calling itself.
    getRowHeaders(parsedRowHeaders: any) {
        return parsedRowHeaders.slice(1, -1).split(",").map((item: any) => {
            item.trim();
            if (item === 'rowNum') {
                item = {
                    type: 'rowNum',
                    renderer: {
                        type: RowNumberRenderer
                    }
                };
            }

            if (item === 'checkbox') {
                item = {
                    type: 'checkbox',
                    header: `
                              <label for="all-checkbox" class="checkbox" 
                                    style="display: flex;
                                    justify-content: center;"
                               >
                                <input type="checkbox" id="all-checkbox" class="hidden-input" name="_checked" />
                                <span class="custom-input"></span>
                              </label>
                            `,
                    renderer: {
                        type: CheckboxRenderer,
                        className: "tui-grid-row-header-checkbox",
                    }
                };
            }
            return item;
        });
    }
    ,
//This function parses the JSON data for row headers and returns an array of trimmed row header names.
    getSummary(parsedSummary: any) {
        if (parsedSummary == undefined || !parsedSummary.hasOwnProperty('columnContent'))
            return null;
        let summaries: any = parsedSummary;
        let columnContents = JSON.parse(summaries.columnContent);
        if (columnContents.length === 0)
            return null;
        for (const columnContent of columnContents) {
            this._setColumnContentMatchedName(columnContent);
        }

        return summaries = {
            height: summaries.height ? summaries.height : 40,
            position: summaries.position,
            columnContent: columnContents.reduce((acc: any, obj: any) => {
                const key: string = Object.keys(obj)[0];
                const value = obj[key];
                acc[key] = value;
                return acc;
            }, {})
        }
    },
//This function parses the JSON data for complex columns and returns the parsed complex columns.
// It also trims the child names.
    getTableData(parsedData: OptRow[]): OptRow[] {
        let listData: OptRow[] = parsedData;
        for (const data of listData) {

            if (data.hasOwnProperty('_attributes') &&
                data.hasOwnProperty('_children')) {
                data._children = this.getTableData(JSON.parse(data._children));
            }
        }
        return listData;
    },
//This function parses the JSON data for the summary and returns the parsed summary object.
// It handles column content by calling _setColumnContentMatchedName to modify the column content based on its value.
    refreshLayout(container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }): void {
        container.grid.table.refreshLayout();
    },

    restoreData(container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }): void {
        container.grid.table.restore();
    },

    reloadData(container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }): void {
        container.grid.table.reloadData();
    },

    modifiedData(container: HTMLElement &
        { $server: any, grid: JSX.Element & { table: TuiGrid } }): void {
        let gridInst: TuiGrid = container.grid.table;
        let modifiedRows: ModifiedRows = gridInst.getModifiedRows();
        gridInst.resetOriginData();
        container.$server.modifiedData(modifiedRows);
    },

    resetOriginData(container: HTMLElement &
        { $server: any, grid: JSX.Element & { table: TuiGrid } }): void {
        container.grid.table.resetOriginData();
        container.grid.table.restore();
    },
//This function parses the JSON data for the columns and returns the parsed columns.
// It handles special cases for input and select editors, and also handles depth0 and depth1 data for select editors.
    removeRows: function (container: HTMLElement & {
        $server: any
        grid: JSX.Element & {
            table: TuiGrid
        },
    }): void {

        let gridInst: TuiGrid = container.grid.table;
        const rows: RowKey[] = gridInst.getCheckedRowKeys();
        const rowLength: number = rows.length;
        if (rowLength === 0)
            return;
        let min: RowKey = 99999999999999;
        for (let i: number = 0; i < rowLength; i++) {
            if (Number(min) > Number(rows[i]))
                min = rows[i];
        }
        const focusedRow: number = gridInst.getIndexOfRow(min);
        gridInst.removeCheckedRows(false);
        if (gridInst.getData().length > 0)
            if (gridInst.getData().length > focusedRow)
                gridInst.focusAt(focusedRow, 0);
            else {
                gridInst.focusAt(gridInst.getData().length - 1, 0);
            }
    },
//This function updates the options of an existing grid.
// It takes a container element with a grid property, and JSON data for the new options.
// The function updates the options, and then updates the grid.
    setOptions: function (container: HTMLElement & {
        grid: JSX.Element & { table: TuiGrid }
    }, optionsJson: string): void {
        let parsedOptions = JSON.parse(optionsJson);
        container.grid.setOption(parsedOptions);
    },
    setTableData(container: HTMLElement & { grid: JSX.Element & { table: TuiGrid } }, data: string): void {
        let parsedItems: OptRow[] = JSON.parse(data);
        if (container && container.grid && container.grid.table) {
            container.grid.table.resetData(this.getTableData(parsedItems));
        }
    },

    setIdToRow: function (container: HTMLElement & {
        grid: JSX.Element & { table: TuiGrid }
    }, rowKey: number, itemId: number): void {
        let rowValue: OptRow = container.grid.table.getRow(rowKey);
        rowValue["id"] = itemId;
        container.grid.table.setRow(rowKey, rowValue);
    },

    setRowCount: function (container: HTMLElement & {
        grid: JSX.Element & { table: TuiGrid }
    }, elementId: string): void {
        setTimeout((): void => {
            const rowCount = container.grid.table.getRowCount();
            const targetElement = document.getElementById(elementId) as HTMLElement;
            if (targetElement) {
                targetElement.textContent = `# ${rowCount}`;
            }
        }, 300);
    },

    setTest: function (container: HTMLElement, content: any): void {
        console.log("Event Test: ", content);
    },
//This function updates the grid by rendering the grid component using ReactDOM.render.
    updateGrid: function (container: HTMLElement & {
        $server: any,
        grid: JSX.Element & { table: TuiGrid }
        $root?: Root
    }): void {
        if (container.$root) {
            container.$root.render(container.grid);
        } else {
            createRoot(container).render(container.grid);
        }
    },

    uncheckAll: function (container: HTMLElement & {
        grid: JSX.Element & { table: TuiGrid }
    }): void {
        container.grid.table.uncheckAll();
    }
}