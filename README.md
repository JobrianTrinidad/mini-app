# TuiGrid component for Vaadin Flow

TuiGrid component uses [tuigrid](https://github.com/rosauromatib/mini-app-core-addon/) library to display data in time.

This component is part of Vaadin Component Factory.
# Description
The TuiGrid class is a component definition for the mini-app-core component. 
mini-app-core is a grid component that uses the vis-timeline library to display data in a time-based manner. 
This class extends the Div component from the Vaadin Flow framework and provides functionality for creating and interacting with the mini-app-core component.

# Development Instructions
To develop or modify the TuiGrid component, follow these instructions:
- Build the project and install the add-on locally:
```
mvn clean install
```
- For starting the demo server go to mini-app-example and run:
```
mvn jetty:run
```
This deploys demo at http://localhost:8080


# How to Use It
To use the TuiGrid component in your Vaadin Flow application, follow these steps:

1. Add the TuiGrid component to your project's dependencies.
2. Import the TuiGrid class into your application.
3. Create an instance of the TuiGrid component.
4. Set the necessary properties and options for the grid.
5. Add the grid component to your application's layout or container.

Example usage:

    TuiGrid grid = new TuiGrid();
    grid.setItems(items);
    grid.setColumns(columns);
    grid.setSummaries(summaries);
    
    // Add the grid to your layout
    layout.add(grid);

# Examples
Here is an example of how to use the TuiGrid component:

    // Create a list of items
    List<Item> items = new ArrayList<>();
    List<String> headers = List.of("name", "artist", "type", "genre", "release", "price", "download", "listen");
    items.add(new GuiItem(List.of("Beautiful Lies", "Birdy", "Deluxe;", "Pop", "2016-03-26", "10000", "1000", "10050"), headers));
    items.add(new GuiItem(List.of("Beautiful Lies", "Birdy", "Deluxe;", "Pop", "2016-03-26", "10000", "1000", "10050"), headers));
    items.add(new GuiItem(List.of("Beautiful Lies", "Birdy", "Deluxe;", "Pop", "2016-03-26", "10000", "1000", "10050"), headers));
    
    // Create a list of columns
    List<Column> columns = new ArrayList<>();
    columns.add(new Column(new ColumnBaseOption(0, "Name", "name", 250, "center", ""));
    
    // Create a list of summaries
    List<Summary> summaries = new ArrayList<>();
    summaries.add(new Summary("price", Summary.OperationType.sum));

    // Create a list of complex header
     List<ComplexColumn> customHeaders = List.of(
                new ComplexColumn("Details Info", "Details Info", List.of("type", "genre", "release")),
                new ComplexColumn("Count", "Count", List.of("download", "listen")),
                new ComplexColumn("Extra Info", "Extra Info", List.of("price", "Count")));
    
    // Create a TuiGrid instance
    TuiGrid grid = new TuiGrid(customHeaders, items, columns, summaries);
    
    // Add the grid to your layout
    layout.add(grid);

# Missing Features or Bugs
Currently, there are no missing features or known bugs in the TuiGrid component. If you encounter any issues or have suggestions for improvements, please report them to the project maintainers.