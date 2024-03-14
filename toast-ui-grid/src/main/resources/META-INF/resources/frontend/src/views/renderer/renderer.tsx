import TuiGrid, {ColumnInfo, Row, RowKey} from 'tui-grid';

export class RowNumberRenderer {
    public el: HTMLElement;

    constructor(props: { formattedValue: string }) {
        const el = document.createElement('span');
        el.innerHTML = `No.${props.formattedValue}`;
        this.el = el;
    }

    getElement(): HTMLElement {
        return this.el;
    }

    render(props: { formattedValue: string }): void {
        this.el.innerHTML = `No.${props.formattedValue}`;
    }
}

type CallbackFunction = (result: Row, colName: String, value: boolean) => void;

export class CheckboxRenderer {
    public el: HTMLElement;

    constructor(props: {
        value: boolean; grid: TuiGrid; rowKey: RowKey,
        columnInfo: ColumnInfo & {
            renderer: CheckboxRenderer &
                {
                    callback: CallbackFunction,
                    className: String,
                }
        }
    }) {
        const {grid, rowKey, columnInfo} = props;
        const label = document.createElement('label');
        label.className = 'checkbox ' + columnInfo.renderer.className;
        label.setAttribute('for', String(rowKey));

        const hiddenInput = document.createElement('input');
        hiddenInput.className = 'hidden-input';
        hiddenInput.id = String(rowKey);

        const customInput = document.createElement('span');
        customInput.className = 'custom-input';

        label.appendChild(hiddenInput);
        label.appendChild(customInput);

        hiddenInput.type = 'checkbox';
        label.addEventListener('click', (ev: MouseEvent): void => {
            if (columnInfo.disabled)
                return;
            ev.preventDefault();

            if (ev.shiftKey) {
                grid[!hiddenInput.checked ? 'checkBetween' : 'uncheckBetween'](rowKey);
                return;
            }

            let colName: string | null = grid.getFocusedCell().columnName;
            let row: Row | null = grid['getRow'](rowKey);
            if (columnInfo.renderer.callback !== undefined) {
                if (row !== null) {
                    if (colName !== null) {
                        row[colName] = !hiddenInput.checked;
                    }
                    grid['setRow'](rowKey, row);

                    columnInfo.renderer.callback(row, colName, !hiddenInput.checked);
                }
            } else if (columnInfo.renderer.className.includes("header"))
                grid[!hiddenInput.checked ? 'check' : 'uncheck'](rowKey);
        });

        this.el = label;

        this.render(props);
    }

    getElement(): HTMLElement {
        return this.el;
    }

    render(props: { value: boolean }): void {
        const hiddenInput = this.el.querySelector('.hidden-input') as HTMLInputElement;
        const checked = Boolean(props.value);

        hiddenInput.checked = checked;
    }
}

export class SignatureRenderer {
    public el: HTMLElement;
    public value: number;
    public tgrid: TuiGrid;

    constructor(props: {
        value: number; // Assuming the value is a base64 encoded image or signature data
        grid: TuiGrid;
        rowKey: RowKey;
        columnInfo: ColumnInfo & {
            renderer: SignatureRenderer & {
                callback: CallbackFunction;
                className: string;
            };
        };
    }) {
        this.value = Number(props.value);
        this.tgrid = props.grid;
        // Create a container element for the signature
        const container = document.createElement('div');
        container.classList.add('signature-container-grid');
        const signatureDialog = document.createElement('signature-dialog');
        signatureDialog.loadSignatureImage(this.value);
        container.appendChild(signatureDialog);
        this.el = container;
        signatureDialog.addEventListener('image-save-db', (event) => {
            this.value = event.detail.imageID
            this.save()
        });
    }

    getElement(): HTMLElement {
        return this.el;
    }

    save = (): void => {
           var dispatch = this.tgrid.dispatch;
           var rowKey = this.tgrid.getFocusedCell()["rowKey"];
           var columnName = this.tgrid.getFocusedCell()["columnName"];
           var columnValue = (this.value <= 0 ? null : this.value.toString());
           dispatch("setValue", rowKey , columnName, columnValue);
           dispatch("finishEditing", rowKey, columnName, columnValue, { save: true, triggeredByKey: false });
    }
}

