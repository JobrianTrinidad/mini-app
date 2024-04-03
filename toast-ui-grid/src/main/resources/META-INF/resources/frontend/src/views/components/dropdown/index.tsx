import React, { useState, useEffect, useRef } from "react";
import { Select, Input, Box } from "@chakra-ui/react";
import ReactDOM, { createPortal } from "react-dom";
import {CellEditor} from 'tui-grid/types/editor';

interface Option {
  value: string;
  text: string;
}

interface DropDownProps {
  height: string | number;
  value: number;
  placeholder: string;
  columnInfo: {
    editor: {
      options: {
        listItems: Option[];
        backgroundColor: string;
        opacity: number;
        width: number | string;
        height: number | string;
        border: string;
        outline: string;
        butBackground: string;
        size: object;
      };
    };
  };
}

import React, { useState, useRef, useEffect } from 'react';
import { createPortal } from 'react-dom';

const DropDown: React.FC<DropDownProps & { onValueChange: (newValue: number) => void }> = ({
  height,
  value,
  placeholder,
  onValueChange,
  columnInfo: {
    editor: {
      options: {
        listItems,
        backgroundColor,
        opacity,
        width,
        border,
        outline,
        butBackground,
      },
    },
  },
}) => {
  const [inputValue, setInputValue] = useState("");
  const [filteredItems, setFilteredItems] = useState(listItems);
  const [isDropdownOpen, setIsDropdownOpen] = useState(true);
  const dropdownRef = useRef<HTMLUListElement>(null);
  const [popupPosition, setPopupPosition] = useState<"bottom" | "top">("bottom");

  useEffect(() => {
    setInputValue(listItems.find(item => item.value === value)?.text || ''); // Set initial input value
  }, []); // Run only once on mount

  const handleInputChange = (event) => {
    const input = event.target.value.toLowerCase();
    setInputValue(input);

    // Filter the listItems based on the input value
    const filtered = listItems.filter((item) =>
      item.text.toLowerCase().includes(input)
    );
    setFilteredItems(filtered);
  };

  const handleOptionSelect = (selectedValue) => {
    const selectedItem = listItems.find((item) => item.value === selectedValue.value);
    if (selectedItem) {
      setInputValue(selectedItem.text);
      onValueChange(selectedItem.value); // Call onValueChange to update initialValue
    }
    setIsDropdownOpen(false); // Close dropdown after selecting option
  };

  const handleInputKeyDown = (event) => {
    // Prevent typing values not in the list
//     if (!listItems.some((item) => item.text.toLowerCase() === event.key.toLowerCase())) {
//       event.preventDefault();
//     }
  };

    useEffect(() => {
        // TODO - get the grid hide & position of the component to decide pop up position
      const inputRect = dropdownRef.current?.getBoundingClientRect();
      if (!inputRect)
      {
         setPopupPosition("bottom");
         return;
      }

      const spaceBelow = window.innerHeight - inputRect.bottom;
      const spaceAbove = inputRect.top;

      // Adjust popup position based on available space
      if (spaceBelow < 200 && spaceAbove > 200) {
        setPopupPosition("top");
      } else {
        setPopupPosition("bottom");
      }
    }, [isDropdownOpen]);

  return (
    <Box position="relative" style={{width: "100%" }} >
      <Input
        placeholder={placeholder}
        value={inputValue}
        onChange={handleInputChange}
        onKeyDown={handleInputKeyDown}
        onFocus={() => setIsDropdownOpen(true)}
        style={{
          width: width,
          height: height,
          opacity: opacity,
          backgroundColor: backgroundColor,
          border: border,
          outline: outline,
          butBackground: butBackground,
        }}
      />
      {isDropdownOpen && (
       <ul
         ref={dropdownRef}
         style={{
           position: "absolute",
           [popupPosition === "bottom" ? "top" : "bottom"]: popupPosition === "bottom" ? `calc(13% + ${height}px)` : `calc(-15% + 40px)`,
           left: 0,
         width: "91%",
           maxHeight: "200px",
           overflowY: "auto",
           backgroundColor: "#fff",
           border: "1px solid #ccc",
           borderRadius: "4px",
           boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
           zIndex: 999,
           margin: 0,
           padding: 0,
           listStyle: "none",
           transition: "all 0.3s ease",
           backgroundColor: "#f4f4f4",
         }}
       >
         {filteredItems.map((item: Option) => (
           <li
             key={item.value}
             onClick={() => handleOptionSelect(item)}
             style={{
              paddingLeft: "7px",
              cursor: "pointer",
              borderBottom: "solid 0.5px #326f70",
              zIndex: 1000,
             }}
           >
             {item.text}
           </li>
         ))}
       </ul>
      )}
    </Box>
  );
};

class DropDownEditor implements CellEditor {
  public el: HTMLElement;
  private initialValue: number;

  constructor(props: DropDownProps) {
    const {
      height,
      value,
      placeholder,
      columnInfo: {
        editor: {
          options: {
            listItems,
            backgroundColor,
            opacity,
            width,
            border,
            outline,
            butBackground,
          },
        },
      },
    } = props;

    const container = document.createElement("div");
    container.style.width = "90%";
    ReactDOM.render(
      <DropDown
        height={height}
        value={value}
        placeholder={placeholder}
        onValueChange={(newValue) => (this.initialValue = newValue)} // Update initialValue
        columnInfo={{
          editor: {
            options: {
              listItems,
              backgroundColor,
              opacity,
              width,
              border,
              outline,
              butBackground,
            },
          },
        }}
      />,
      container
    );
    this.el = container;
    this.initialValue = value;
  }

  getElement(): HTMLElement {
    return this.el;
  }

  getValue(): number {
    return this.initialValue;
  }

  mounted(): void {
    // You can perform any necessary initialization here
  }

  beforeDestroy(): void {
    // Cleanup code if necessary
  }
}

export default DropDownEditor;

