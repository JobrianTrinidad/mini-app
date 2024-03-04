// MyComponent.js
class MyComponent extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
        this.items = []; // Initialize items array

    }

    connectedCallback() {
        if (this.items) {
            // Create an ordered list element
            const ol = document.createElement('ol');

            // Loop through the items and create list items for each item
            this.items.forEach(item => {
                const li = document.createElement('li');
                li.textContent = item;
                ol.appendChild(li);
            });

            // Append the ordered list to the shadow DOM
            this.shadowRoot.appendChild(ol);
        }
    }

    static get observedAttributes() {
        return ['items'];
    }

    attributeChangedCallback(name, oldValue, newValue) {
        if (name === 'items') {
            this.items = JSON.parse(newValue);
        }
    }
}

customElements.define('my-component', MyComponent);
