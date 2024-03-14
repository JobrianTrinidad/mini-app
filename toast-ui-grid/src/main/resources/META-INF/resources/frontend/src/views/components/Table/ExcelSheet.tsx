import React, {forwardRef, useEffect, useRef, useState} from 'react';
import {Input} from "@chakra-ui/react";

export interface Cell {
    row: number;
    column: number;
    value: string | number;
}

interface ExcelSheetProps {
    range: Cell[][];
    onSearchResult: (result: Cell) => void;
}

const getCellFromIndex = (index: number, range: Cell[][]): Cell | null => {
    const row: number = Math.floor(index / range[0].length);
    const column: number = index % range[0].length;
    return range[row][column];
};

const ExcelSheet = forwardRef<HTMLDivElement, ExcelSheetProps>((props: ExcelSheetProps, ref: HTMLDivElement) => {
    const [searchValue, setSearchValue] = useState('');
    const [searchResult, setSearchResult] = useState(null);
    const [showSearchInput, setShowSearchInput] = useState(false);
    const [currentSearchIndex, setCurrentSearchIndex] = useState(null);
    const searchInputRef = useRef<HTMLInputElement>(null);
    useEffect(() => {
        const handleKeyDown = (event: KeyboardEvent): void => {
            if (event.ctrlKey && event.key === 'f') {
                event.preventDefault();
                setShowSearchInput(true);
                setSearchValue('');
                setTimeout((): void => {
                    searchInputRef.current?.focus();
                }, 0);
            } else if (event.altKey && event.key === 'ArrowDown') {
                findNext(searchValue, currentSearchIndex, props.range);
            } else if (event.altKey && event.key === 'ArrowUp') {
                findBefore(searchValue, currentSearchIndex, props.range);
            } else {

            }
        };

        const handleKeyUp = (event: KeyboardEvent): void => {
            if (event.key === 'Escape') {
                setShowSearchInput(false);
            }
        };

        document.addEventListener('keydown', handleKeyDown);
        document.addEventListener('keyup', handleKeyUp);

        return (): void => {
            document.removeEventListener('keydown', handleKeyDown);
            document.removeEventListener('keyup', handleKeyUp);
        };
    }, [searchValue, currentSearchIndex, props.range]);

    const handleSearch = (e: React.FormEvent): void => {
        e.preventDefault();
        const searchKey = searchValue.toLowerCase();

        for (let row: number = 0; row < props.range.length; row++) {
            for (let col: number = 0; col < props.range[row].length; col++) {
                const cell = props.range[row][col];
                if (cell.value.toString().toLowerCase().includes(searchKey)) {
                    props.onSearchResult({row, column: props.range[row][col].column, value: cell.value});
                    setSearchResult({row, column: props.range[row][col].column, value: cell.value});
                    setCurrentSearchIndex(row * props.range[row].length + col);
                    return;
                }
            }
        }

        setSearchResult(''); // Value not found
        setCurrentSearchIndex(null);
    };

    const findNext = (searchValue: string, currentSearchIndex: number, range: Cell[][]): void => {
        const searchKey: string = searchValue.toLowerCase();
        if (searchValue && currentSearchIndex !== null) {
            let nextIndex: number = currentSearchIndex + 1;

            if (nextIndex >= range.length * range[0].length) {
                nextIndex = 0;
            }

            for (let index: number = nextIndex; index < range.length * range[0].length; index++) {
                const cell = getCellFromIndex(index, range);
                if (cell?.value.toString().toLowerCase().includes(searchKey)) {
                    props.onSearchResult({
                        row: cell.row,
                        column: props.range[cell.row][cell.column].column,
                        value: cell.value
                    });
                    setSearchResult(getCellFromIndex(index, range));
                    setCurrentSearchIndex(index);
                    return;
                }
            }
            for (let index: number = 0; index < nextIndex; index++) {
                const cell = getCellFromIndex(index, range);
                if (cell?.value.toString().toLowerCase().includes(searchKey)) {
                    props.onSearchResult({
                        row: cell.row,
                        column: props.range[cell.row][cell.column].column,
                        value: cell.value
                    });
                    setSearchResult(getCellFromIndex(index, range));
                    setCurrentSearchIndex(index);
                    return;
                }
            }
            setSearchResult(null);
            setCurrentSearchIndex(-1);
        }
    };

    const findBefore = (searchValue: string, currentSearchIndex: number, range: Cell[][]
    ): void => {
        const searchKey: string = searchValue.toLowerCase();
        if (searchValue && currentSearchIndex !== null) {
            let beforeIndex: number = currentSearchIndex - 1;

            if (beforeIndex < 0) {
                return;
                beforeIndex = range.length * range[0].length - 1;
            }
            for (let index: number = beforeIndex; index >= 0; index--) {
                const cell = getCellFromIndex(index, range);
                if (cell?.value.toString().toLowerCase().includes(searchKey)) {
                    props.onSearchResult({
                        row: cell.row,
                        column: props.range[cell.row][cell.column].column,
                        value: cell.value
                    });
                    setSearchResult(getCellFromIndex(index, range));
                    setCurrentSearchIndex(index);
                    return;
                }
            }
            for (let index: number = range.length * range[0].length - 1; index >= beforeIndex; index--) {
                const cell = getCellFromIndex(index, range);
                if (cell?.value.toString().toLowerCase().includes(searchKey)) {
                    setSearchResult(getCellFromIndex(index, range));
                    setCurrentSearchIndex(index);
                    return;
                }
            }
            setSearchResult(null);
            setCurrentSearchIndex(beforeIndex);
        }
    }

    return (
        <div ref={ref}>
            {showSearchInput && (
                <form onSubmit={handleSearch}>
                    <Input
                        ref={searchInputRef}
                        defaultValue={searchValue}
                        type="text"
                        onChange={e => setSearchValue(e.target.value)}
                        style={{
                            backgroundColor: '#66878858',
                            opacity: 1,
                            width: '20em',
                            height: '100%',
                            border: '1px solid #326f70',
                            outline: 'none',
                        }}
                        _focusVisible={{outline: "none"}}
                        _hover={{outline: "none"}}
                    />
                    <button type="submit">Find</button>
                </form>
            )}
        </div>
    );
});

export default ExcelSheet;