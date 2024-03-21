import React, {useEffect, useRef} from 'react';
import ExcelSheet, {Cell} from "./ExcelSheet";
import TuiGrid, {GridEventName, Row} from 'tui-grid';
import {TuiGridEvent} from "tui-grid/types/event";
import {OptColumn, OptGrid, OptHeader, OptRow, OptRowHeader, OptSummaryData, OptTree} from "tui-grid/types/options";
import {CreateMenuGroups} from "tui-grid/types/store";
import {ColumnOptions} from "tui-grid/types/store/column";
import Pagination from 'tui-pagination';
import 'tui-pagination/dist/tui-pagination.css';

interface FeatureTableProps {
    getGridInstance: (gridInstance: TuiGrid) => void;
    TableData: OptRow[];
    columns: OptColumn[];
    contextMenu?: CreateMenuGroups;
    summary?: OptSummaryData;
    columnOptions?: ColumnOptions;
    header?: OptHeader;
    width?: number | 'auto';
    bodyHeight?: number | 'fitToParent' | 'auto';
    scrollX?: boolean;
    scrollY?: boolean;
    rowHeaders?: OptRowHeader[];
    treeColumnOptions?: OptTree;
    rowHeight?: number | 'auto';
    minBodyHeight?: number;
    pageSize?: number | 50;
    onEditingStart?: (ev: TuiGridEvent) => void;
    onEditingFinish?: (ev: TuiGridEvent) => void;
    onSelection?: (ev: TuiGridEvent) => void;
    onCheck?: (ev: TuiGridEvent) => void;
    onCheckAll?: (ev: TuiGridEvent) => void;
    onUncheck?: (ev: TuiGridEvent) => void;
    onUncheckAll?: (ev: TuiGridEvent) => void;
    onFocusChange?: (ev: TuiGridEvent) => void;
    onAfterChange?: (ev: TuiGridEvent) => void;
    onColumnResize?: (ev: TuiGridEvent) => void;
    handleSearchResult?: (result: Cell) => void;
}

const FeatureTable: React.FC<FeatureTableProps> = React.forwardRef<HTMLDivElement, FeatureTableProps>(
    (
        {
            getGridInstance,
            TableData,
            columns,
            contextMenu,
            summary,
            columnOptions,
            header,
            width,
            bodyHeight,
            scrollX,
            scrollY,
            rowHeaders,
            treeColumnOptions,
            rowHeight,
            minBodyHeight,
            pageSize,
            onSelection,
            onCheck,
            onCheckAll,
            onUncheck,
            onUncheckAll,
            onFocusChange,
            onAfterChange,
            onColumnResize,
            handleSearchResult
        }: FeatureTableProps,
        ref: React.Ref<HTMLDivElement>
    ) => {
        const gridRef = useRef<HTMLDivElement>(null);
        const excelRef = useRef<HTMLDivElement>(null);
        const gridInstanceRef = useRef<TuiGrid | null>(null);

        function loadRows(lengthOfLoaded: number, allData: OptRow[]): OptRow[] {
            const rows: OptRow[] = [];
            for (let i: number = lengthOfLoaded; i < allData.length; i += 1) {
                const row: OptRow = {};
                for (let j: number = 0; j < columns.length; j += 1) {
                    row[columns[j].name] = allData[i][columns[j].name];
                }
                rows.push(row);
            }
            return rows;
        }

        const data = loadRows(0, TableData);
        useEffect(() => {
            const grid = new TuiGrid({
                el: gridRef.current!,
                data: data,
                columns: columns,
                // ...(contextMenu && {contextMenu}),
                contextMenu: null,
                className: 'table-center',
                ...(summary && {summary}),
                ...(columnOptions && {columnOptions}),
                ...(header && {header}),
                ...(width && {width}),
                ...(bodyHeight && {bodyHeight}),
                scrollX: scrollX,
                scrollY: scrollY,
                ...(rowHeaders && {rowHeaders}),
                ...(treeColumnOptions && {treeColumnOptions}),
                // ...(rowHeight && {rowHeight}),
                rowHeight: 'auto',
                ...(minBodyHeight && {minBodyHeight}),
                pageOptions: {
                  useClient: true, // Enable client-side pagination
                  perPage: pageSize, // Items per page
                },
            });
            gridInstanceRef.current = grid;

            grid.on('focusChange' as GridEventName, (ev: TuiGridEvent): void => {
                if (onFocusChange) {
                    onFocusChange(ev);
                }
            });

            grid.on('selection' as GridEventName, (ev: TuiGridEvent): void => {
                if (onSelection) {
                    onSelection(ev);
                }
            });

            grid.on('check' as GridEventName, (ev: TuiGridEvent): void => {
                if (onCheck) {
                    onCheck(ev);
                }
            });

            grid.on('uncheck' as GridEventName, (ev: TuiGridEvent): void => {
                if (onUncheck) {
                    onUncheck(ev);
                }
            });

            grid.on('checkAll' as GridEventName, (ev: TuiGridEvent): void => {
                if (onCheckAll) {
                    onCheckAll(ev);
                }
            });

            grid.on('uncheckAll' as GridEventName, (ev: TuiGridEvent): void => {
                if (onUncheckAll) {
                    onUncheckAll(ev);
                }
            });

            grid.on('afterChange' as GridEventName, (ev: TuiGridEvent): void => {
                console.log("changed: ", ev);
                if (onAfterChange) {
                    onAfterChange(ev);
                }
            });

            grid.on('columnResize' as GridEventName, (ev: TuiGridEvent): void => {
                if (onColumnResize) {
                    onColumnResize(ev);
                }
            });

            grid.on('mousedown' as GridEventName, (ev: TuiGridEvent): void => {
            });

            grid.on('beforeFilter' as GridEventName, (ev: TuiGridEvent): void => {
            });

            getGridInstance(grid);
            function handleKeyDown(event) {
                // Check if the left arrow key (keyCode 37) is pressed with the Alt key
                if (event.keyCode === 37 && event.altKey) {
                    grid.getPagination().movePageTo(grid.getPagination()._getRelativePage("prev"));
                }
                // Check if the right arrow key (keyCode 39) is pressed with the Alt key
                else if (event.keyCode === 39 && event.altKey) {
                  grid.getPagination().movePageTo(grid.getPagination()._getRelativePage("next"));
                }
               // Check if the Up arrow key (keyCode 38) is pressed with the Alt key
               else if (event.keyCode === 38 && event.altKey) { // Up arrow key
                  grid.getPagination().movePageTo(grid.getPagination()._getMorePageIndex("next"));
               }
               // Check if the Down arrow key (keyCode 40) is pressed with the Alt key
               else if (event.keyCode === 40 && event.altKey) { // Down arrow key
                  grid.getPagination().movePageTo(grid.getPagination()._getMorePageIndex("prev"));
               }
               // Check if the key pressed is the page up key (keyCode 33) or the page down key (keyCode 34)
               if (event.keyCode === 33) { // Page up key
                 grid.getPagination().movePageTo(grid.getPagination()._getLastPage());
               }
               else if (event.keyCode === 34) { // Page down key
                 grid.getPagination().movePageTo(1);
               }
            }

            // Add event listener for keydown event
            document.addEventListener('keydown', handleKeyDown);

            return (): void => {
                if (grid) {
                    grid.destroy();
                    document.removeEventListener('keydown', handleKeyDown);
                }
            };
        }, []);

        function setOption(option: OptGrid): void {
            if (gridInstanceRef.current) {
                if (option.data)
                    gridInstanceRef.current.resetData(option.data);
                if (option.width)
                    gridInstanceRef.current.setWidth(option.width || 0);
                if (option.bodyHeight)
                    gridInstanceRef.current.setBodyHeight(option.bodyHeight || 0);
                if (option.scrollX)
                    gridInstanceRef.current.setScrollX(option.scrollX || false);
                if (option.scrollY)
                    gridInstanceRef.current.setScrollY(option.scrollY || false);
                if (option.rowHeaders)
                    gridInstanceRef.current.setRowHeaders(option.rowHeaders || []);
                if (option.summary)
                    gridInstanceRef.current.setSummaryColumnContent(option.summary || {});
                if (option.header)
                    gridInstanceRef.current.setHeader(option.header || {});
                if (option.treeColumnOptions)
                    gridInstanceRef.current.setTreeColumnOptions(option.treeColumnOptions || {});
                if (option.rowHeight)
                    gridInstanceRef.current.setRowHeight(option.rowHeight || 0);
                if (option.minBodyHeight)
                    gridInstanceRef.current.setMinBodyHeight(option.minBodyHeight || 0);
            } else {
                const grid = new TuiGrid(option);
                gridInstanceRef.current = grid;
                getGridInstance(grid);
            }
        }

        let range: Cell[][] = TableData.map((row: OptRow, rowIndex: number) => {
            return Object.keys(row).map((key: string, colIndex: number): Cell => {
                return {row: rowIndex, column: colIndex, value: row[key as keyof OptRow]};
            });
        });

        return (
            <div>
                <div id={"container"}></div>
                <div id={"target"} ref={gridRef}></div>
                <ExcelSheet
                    ref={excelRef}
                    range={range}
                    onSearchResult={handleSearchResult}
                />
            </div>
        );
    }
);

export default FeatureTable;